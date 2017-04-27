package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.FinishNoticeOuterClass;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
public class Finish {
    private Logger logger = LoggerFactory.getLogger(Finish.class);

    public void FinishNotice(ChannelHandlerContext ctx, String winnerID) {
        FinishNoticeOuterClass.FinishNotice.Builder notice = FinishNoticeOuterClass.FinishNotice.newBuilder();
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setWinnerId(winnerID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setFinishNotice(notice);
        ctx.writeAndFlush(container);
    }
}
