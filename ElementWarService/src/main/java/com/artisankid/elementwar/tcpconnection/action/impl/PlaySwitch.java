package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.PlaySwitchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class PlaySwitch {
    private Logger logger = LoggerFactory.getLogger(PlaySwitch.class);

    public void PlaySwitchNotice(ChannelHandlerContext ctx, String playerID) {
        PlaySwitchNoticeOuterClass.PlaySwitchNotice.Builder notice = PlaySwitchNoticeOuterClass.PlaySwitchNotice.newBuilder();
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setPlayerId(playerID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setPlaySwitchNotice(notice);
        ctx.writeAndFlush(container);
    }
}
