package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.PlaySwitchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.RoomManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserManager;
import io.netty.channel.ChannelHandlerContext;
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
    private Logger logger = LoggerFactory.getLogger(PlaySwitch.class);

    static public void PlaySwitchNotice(final String playerID) {
        PlaySwitchNoticeOuterClass.PlaySwitchNotice.Builder notice = PlaySwitchNoticeOuterClass.PlaySwitchNotice.newBuilder();
        long now = System.currentTimeMillis();
        long expiredTime = now + 30 * 1000;
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setPlayerId(playerID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setPlaySwitchNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                //出牌结束或者通知超时，换下一个用户发牌
                Deal.DealNoticeNextUser(playerID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(playerID);
        ctx.writeAndFlush(container);
    }
}