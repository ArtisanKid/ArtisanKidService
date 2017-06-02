package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.PlaySwitchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.RoomManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserManager;
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
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class PlaySwitch {
    private static Logger logger = LoggerFactory.getLogger(PlaySwitch.class);

    static public void PlaySwitchNotice(final String playerID) {
        logger.debug("DealNotice" + " playerID:" + playerID + " 开始发送...");

        PlaySwitchNoticeOuterClass.PlaySwitchNotice.Builder notice = PlaySwitchNoticeOuterClass.PlaySwitchNotice.newBuilder();
        long now = System.currentTimeMillis();
        long expiredTime = now + 30 * 1000;
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setPlayerId(playerID);

        UserManager.getUser(playerID).setGameState(User.GameState.Playing);
        UserManager.getUser(playerID).setPlayExpiredTime(expiredTime);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.PlaySwitchNotice);
        container.setPlaySwitchNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                //用户出牌时间可能会被延长
                if(UserManager.getUser(playerID).getPlayExpiredTime() > System.currentTimeMillis()) {
                    logger.debug("DealNotice" + " playerID:" + playerID + " 用户出牌时间被延长");
                    return;
                }

                logger.debug("DealNotice" + " playerID:" + playerID + " 切换出牌超时，状态变更为Waiting，准备下一轮发牌...");

                //出牌结束或者通知超时，换下一个用户出牌
                UserManager.getUser(playerID).setGameState(User.GameState.Waiting);
                Deal.DealNoticeNextUser(playerID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(playerID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                timer.cancel();

                logger.debug("DealNotice" + " playerID:" + playerID + " 切换出牌超时成功");
            }
        });
    }
}
