package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.ewmessagemodel.PlaySwitchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class PlaySwitch {
    private static Logger logger = LoggerFactory.getLogger(PlaySwitch.class);

    static public void PlaySwitchNotice(final String playerID) {
        final Long expiredTime = System.currentTimeMillis() + 30 * 1000L;
        ContainerOuterClass.Container.Builder container = MakePlaySwitchNotice(playerID, expiredTime);
        for( User user : RoomManager.getRoom(playerID).getUsers()) {
            if (user.getUserID().equals(playerID)) {
                logger.debug("PlaySwitchNotice" + " playerID:" + playerID + " 开始发送...");

                final Timer timer = new Timer(true);
                TimerTask task = new TimerTask() {
                    public void run() {
                        //用户出牌时间可能会被延长
                        if(UserManager.getUser(playerID).getPlayExpiredTime() > System.currentTimeMillis()) {
                            logger.debug("PlaySwitchNotice" + " playerID:" + playerID + " 用户出牌时间被延长");
                            return;
                        }

                        logger.debug("PlaySwitchNotice" + " playerID:" + playerID + " 切换出牌，状态变更为Waiting，准备下一轮发牌...");

                        //出牌结束或者通知超时，换下一个用户出牌
                        User currentPlayer = UserManager.getUser(playerID);
                        currentPlayer.setGameState(User.GameState.Waiting);
                        currentPlayer.setPlayExpiredTime(0L);

                        Deal.DealNoticeNextUser(playerID);
                    }
                };
                timer.schedule(task, expiredTime - System.currentTimeMillis());

                ChannelHandlerContext ctx = UserContextManager.getContext(playerID);
                ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.debug("PlaySwitchNotice" + " playerID:" + playerID + " 切换出牌成功");

                        User currentPlayer = UserManager.getUser(playerID);
                        currentPlayer.setGameState(User.GameState.Playing);
                        currentPlayer.setPlayExpiredTime(expiredTime);
                    }
                });
            } else {
                OnlyPlaySwitchNotice(user.getUserID(), container);
            }
        }
    }

    static private ContainerOuterClass.Container.Builder MakePlaySwitchNotice(final String playerID, Long expiredTime) {
        PlaySwitchNoticeOuterClass.PlaySwitchNotice.Builder notice = PlaySwitchNoticeOuterClass.PlaySwitchNotice.newBuilder();
        notice.setSendTime(System.currentTimeMillis() / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setPlayerId(playerID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.PlaySwitchNotice);
        container.setPlaySwitchNotice(notice);

        return container;
    }

    static private void OnlyPlaySwitchNotice(final String receiverID, ContainerOuterClass.Container.Builder container) {
        ChannelHandlerContext ctx = UserContextManager.getContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                logger.debug("OnlyPlaySwitchNotice" + " receiverID:" + receiverID + " 发送成功");
            }
        });
    }
}
