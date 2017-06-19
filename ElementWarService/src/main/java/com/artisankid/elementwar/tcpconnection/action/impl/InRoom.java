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

        UserManager.getUser(receiverID).setState(User.State.InRooming);

        InRoomNoticeOuterClass.InRoomNotice.Builder notice = InRoomNoticeOuterClass.InRoomNotice.newBuilder();
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 10 * 1000L;
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
                Room room = RoomManager.getRoom(receiverID);
                if(room == null) {
                    return;
                }

                logger.error("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 发送超时，状态变为Free");

                for(User user : room.getUsers()) {
                    user.setState(User.State.Free);
                }
                RoomManager.removeRoom(receiverID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                timer.cancel();

                if(UserManager.getUser(receiverID).getState() == User.State.Free) {
                    return;
                }

                logger.debug("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 发送成功");

                UserManager.getUser(receiverID).setState(User.State.InRoomed);

                List<User> users = RoomManager.getRoom(receiverID).getUsers();
                for(User user : users) {
                    User.State state = user.getState();
                    if(state != User.State.InRoomed) {
                        logger.debug("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 等待其他人状态变更");
                        return;
                    }
                }

                logger.debug("InRoomNotice" + " receiverID:" + receiverID + " roomID:" + roomID + " 准备发牌...");

                for(Integer i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    user.setState(User.State.Gaming);

                    if(i == 0) {
                        Deal.DealNotice(user.getUserID());
                        Deal.PlayDealNotice(user.getUserID());
                    } else {
                        Deal.DealNotice(user.getUserID());
                    }
                }
            }
        });
    }
}
