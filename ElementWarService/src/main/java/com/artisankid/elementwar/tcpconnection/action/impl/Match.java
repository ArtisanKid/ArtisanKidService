package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiXiangYu on 2017/4/24.
 */
@NettyAction
public class Match {
    private Logger logger = LoggerFactory.getLogger(Match.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.MATCH_MESSAGE_FIELD_NUMBER)
    public void matchMessage(ChannelHandlerContext ctx, ContainerOuterClass.Container container) {
        MatchMessageOuterClass.MatchMessage message = container.getMatchMessage();
        //获取匹配信息发送者ID
        final String senderID = message.getSenderId();

        //首先判断是否超时
        long expiredTime = (long) (message.getExpiredTime() * 1000);
        if(expiredTime <= System.currentTimeMillis()) {
            UserManager.getUser(senderID).setState(User.State.Free);
            return;
        }

        Integer senderStrength = UserManager.getUser(senderID).getStrength();

        //查询和sender实力相当的用户
        List<String> userIDs = UserManager.getMatchUserIDs();
        String receiverID = null;
        for(String userID : userIDs) {
            Integer strength = UserManager.getUser(userID).getStrength();
            if(Math.abs(senderStrength - strength) < 20) {
                receiverID = userID;
                break;
            }
        }

        //如果未找到实力相当的用户，那么就排队
        if(receiverID == null) {
            UserManager.getUser(senderID).setState(User.State.Matching);
            UserManager.getUser(senderID).setMatchExpiredTime(expiredTime);

            Timer timer = new Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    //用户等待的时间超时
                    if(UserManager.getUser(senderID).getState() == User.State.Matching) {
                        UserManager.getUser(senderID).setState(User.State.Free);
                    }
                }
            };
            timer.schedule(task, expiredTime - System.currentTimeMillis());
            return;
        }

        RoomManager.createRoom(Arrays.asList(senderID, receiverID));
        UserManager.getUser(senderID).setState(User.State.Matched);
        UserManager.getUser(senderID).setState(User.State.Matched);

        String messageID = message.getMessageId();

        //给双方发送通知
        MagicianDao dao = new MagicianDao();
        matchNotice(senderID, messageID, dao.selectByOpenID(receiverID), expiredTime);
        matchNotice(receiverID, messageID, dao.selectByOpenID(senderID), UserManager.getUser(receiverID).getMatchExpiredTime());
    }

    public void matchNotice(final String receiverID, String messageID, Magician user, long expiredTime) {
        if(expiredTime <= System.currentTimeMillis()) {
            matchNoticeFailed(receiverID);
            return;
        }

        MatchNoticeOuterClass.MatchNotice.Builder notice = MatchNoticeOuterClass.MatchNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.FALSE);

        notice.setUserId(user.getOpenID());
        notice.setUserName(user.getNickname());
        notice.setUserPortraitUrl(user.getSmallPortrait());

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMatchNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                matchNoticeFailed(receiverID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(UserManager.getUser(receiverID).getState() == User.State.Free) {
                    return;
                }

                if(UserManager.getUser(receiverID).getState() == User.State.Matching) {
                    return;
                }

                timer.cancel();

                UserManager.getUser(receiverID).setState(User.State.WaitingInRoom);

                Room room = RoomManager.getRoom(receiverID);
                for(User user : room.getUsers()) {
                    User.State state = user.getState();
                    if(state != User.State.WaitingInRoom) {
                        return;
                    }
                }

                for(User user : room.getUsers()) {
                    InRoom.InRoomNotice(user.getUserID(), room.getRoomID());
                }
            }
        });
    }

    private void matchNoticeFailed(String userID) {
        Room room = RoomManager.getRoom(userID);
        if(room == null) {
            UserManager.getUser(userID).setState(User.State.Free);
            return;
        }

        for(User user : room.getUsers()) {
            if(user.getUserID() == userID) {
                user.setState(User.State.Free);
            } else {
                if(user.getMatchExpiredTime() > System.currentTimeMillis()) {
                    user.setState(User.State.Matching);
                } else {
                    user.setState(User.State.Free);
                }
            }
        }
        RoomManager.destroyRoom(userID);
    }
}
