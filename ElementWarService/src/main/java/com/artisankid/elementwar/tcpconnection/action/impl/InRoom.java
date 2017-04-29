package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.InRoomNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
public class InRoom {
    private Logger logger = LoggerFactory.getLogger(Finish.class);

    static public void InRoomNotice(final String receiverID, String roomID) {
        UserManager.getUser(receiverID).setState(User.State.InRooming);

        InRoomNoticeOuterClass.InRoomNotice.Builder notice = InRoomNoticeOuterClass.InRoomNotice.newBuilder();
        long now = System.currentTimeMillis();
        long expiredTime = now + 10 * 1000;
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setRoomId(roomID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setInRoomNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                //超时则说明进入房间失败了
                Room room = RoomManager.getRoom(receiverID);
                if(room == null) {
                    return;
                }

                for(User user : room.getUsers()) {
                    user.setState(User.State.Free);
                }
                RoomManager.destroyRoom(receiverID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                timer.cancel();

                if(UserManager.getUser(receiverID).getState() == User.State.Free) {
                    return;
                }

                UserManager.getUser(receiverID).setState(User.State.InRoomed);

                Room room = RoomManager.getRoom(receiverID);
                for(User user : room.getUsers()) {
                    User.State state = user.getState();
                    if(state != User.State.InRoomed) {
                        return;
                    }
                }

                for(User user : room.getUsers()) {
                    user.setState(User.State.Gaming);
                    Deal.DealNotice(user.getUserID(), Arrays.asList("C"));
                }
            }
        });
    }
}
