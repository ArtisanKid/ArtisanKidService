package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
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
        final String messageID = message.getMessageId();
        final String senderID = message.getSenderId();

        if(UserManager.getUser(senderID).getState() != User.State.Free) {
            logger.error("InviteMessage" + " messageID:" + messageID + " senderID:" + senderID + " sender状态错误");
            return;
        }

        logger.debug("MatchMessage" + " messageID:" + messageID + " senderID:" + senderID + " 开始匹配...");

        //查询和sender实力相当的用户
        Integer senderStrength = UserManager.getUser(senderID).getStrength();
        List<String> userIDs = UserManager.getMatchUserIDs();
        String receiverID = null;
        for(String userID : userIDs) {
            Integer strength = UserManager.getUser(userID).getStrength();
            if(Math.abs(senderStrength - strength) < 20) {
                logger.debug("MatchMessage" + " messageID:" + messageID + " senderID:" + senderID + " 找到匹配用户:" + userID);
                receiverID = userID;
                break;
            }
        }

        final Long expiredTime =  new Double(message.getExpiredTime() * 1000L).longValue();

        //如果未找到实力相当的用户，那么就排队
        if(receiverID == null) {
            logger.debug("MatchMessage" + " messageID:" + messageID + " senderID:" + senderID + " 状态变为Matching");

            UserManager.getUser(senderID).setState(User.State.Matching);
            UserManager.getUser(senderID).setMatchMessageID(messageID);
            UserManager.getUser(senderID).setMatchExpiredTime(expiredTime);

            Timer timer = new Timer(true);
            TimerTask task = new TimerTask() {
                public void run() {
                    //用户等待的时间超时
                    if(UserManager.getUser(senderID).getState() != User.State.Matching) {
                        return;
                    }

                    logger.error("MatchMessage" + " messageID:" + messageID + " senderID:" + senderID + " 未找到匹配用户");

                    logger.error("MatchMessage" + " messageID:" + messageID + " senderID:" + senderID + " 状态变为Free");
                    UserManager.getUser(senderID).setState(User.State.Free);
                }
            };
            timer.schedule(task, expiredTime - System.currentTimeMillis());
            return;
        }

        UserManager.getUser(senderID).setState(User.State.Matched);
        UserManager.getUser(receiverID).setState(User.State.Matched);

        //使用两个UserID创建房间
        RoomManager.createRoom(Arrays.asList(senderID, receiverID));

        //给双方发送通知
        MagicianDao dao = new MagicianDao();

        logger.debug("MatchMessage" + " messageID:" + messageID + " senderID:" + senderID + " 准备发送MatchNotice...");
        matchNotice(senderID, messageID, dao.selectByOpenID(receiverID), expiredTime);

        String receiverMessageID = UserManager.getUser(receiverID).getMatchMessageID();
        logger.debug("MatchMessage" + " messageID:" + receiverMessageID + " senderID:" + senderID + " 准备发送MatchNotice...");
        matchNotice(receiverID, receiverMessageID, dao.selectByOpenID(senderID), UserManager.getUser(receiverID).getMatchExpiredTime());
    }

    public void matchNotice(final String receiverID, final String messageID, Magician user, Long expiredTime) {
        logger.debug("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 开始发送...");

        MatchNoticeOuterClass.MatchNotice.Builder notice = MatchNoticeOuterClass.MatchNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);

        notice.setUserId(user.getOpenID());
        notice.setUserName(user.getNickname());
        notice.setUserPortraitUrl(user.getSmallPortrait());

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.MatchNotice);
        container.setMatchNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID  + " 发送超时");
                matchNoticeFailed(receiverID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(UserManager.getUser(receiverID).getState() == User.State.Free) {
                    logger.error("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 发送已经超时，状态为Free");
                    return;
                }

                if(UserManager.getUser(receiverID).getState() == User.State.Matching) {
                    logger.error("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 发送已经超时，状态为Matching");
                    return;
                }

                logger.debug("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 发送成功，状态变为WaitingInRoom");

                timer.cancel();

                UserManager.getUser(receiverID).setState(User.State.WaitingInRoom);

                final Room room = RoomManager.getRoom(receiverID);
                for(User user : room.getUsers()) {
                    User.State state = user.getState();
                    if(state != User.State.WaitingInRoom) {
                        logger.debug("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 等待其他人状态变更");
                        return;
                    }
                }

                logger.debug("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 准备进入房间...");

                if(future.isDone()) {
                    if (future.isSuccess()) {
                        for(User user : room.getUsers()) {
                            InRoom.InRoomNotice(user.getUserID(), room.getRoomID());
                        }
                    } else if (future.isCancelled()) {
                        logger.error("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 写入被取消");
                    } else {
                        logger.error("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 写入失败 " + future.cause());
                    }
                } else {
                    logger.error("MatchNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " 写入未完成");
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
        RoomManager.removeRoom(userID);
    }
}
