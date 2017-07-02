package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.InRoomNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
public class InRoom {
    private static Logger logger = LoggerFactory.getLogger(InRoom.class);

    static public void InRoomNotice(final String receiverID, final String roomID) {
        logger.debug("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 开始发送...");

        EpicManager.WriteEpic(receiverID, null, "这是一场战争，需要勇气与智慧来取得胜利。勇士！进入古老的战场，等待英雄凯旋！");

        InRoomNoticeOuterClass.InRoomNotice.Builder notice = InRoomNoticeOuterClass.InRoomNotice.newBuilder();
        Long now = System.currentTimeMillis();
        final Long expiredTime = now + 10 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setRoomId(roomID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.InRoomNotice);
        container.setInRoomNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                //超时则说明进入房间失败了
                logger.error("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 发送超时");
                InRoomNoticeFailed(receiverID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(expiredTime <= System.currentTimeMillis()) {
                    return;
                }

                timer.cancel();

                if(future.isCancelled()
                        || !future.isSuccess()) {
                    logger.error("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 发送失败");
                    InRoomNoticeFailed(receiverID);
                    return;
                }

                Room room = RoomManager.getRoom(receiverID);
                if(room == null) {
                    logger.debug("InRoomNotice" + " receiverID:" + receiverID + " 已有用户发送失败");
                    return;
                }

                logger.debug("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 发送成功，状态为InRoomed");

                EpicManager.WriteEpic(receiverID, roomID, "终将会是一场残酷的战争！");
                EpicManager.WriteEpic(roomID, "对面，就是敌人！战争的号角已经吹响，邪恶的一方终究会受到神的制裁！");

                UserManager.getUser(receiverID).setState(User.State.InRoomed);

                List<User> users = room.getUsers();
                for(User user : users) {
                    if(user.getState() != User.State.InRoomed) {
                        logger.debug("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 等待其他人状态变更");
                        return;
                    }
                }

                logger.debug("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 准备发牌...");

                for(Integer i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    user.setState(User.State.Gaming);

                    Deal.InitDealNotice(user.getUserID());
                    if(i == 0) {
                        Deal.PlayDealNotice(user.getUserID());
                    }
                }
            }
        });
    }

    private static void InRoomNoticeFailed(String userID) {
        Room room = RoomManager.getRoom(userID);
        if(room == null) {
            return;
        }

        for(User user : room.getUsers()) {
            user.setState(User.State.Free);
            user.setMatchMessageID(null);
            user.setMatchExpiredTime(0L);
        }
        RoomManager.removeRoom(userID);
    }
}
