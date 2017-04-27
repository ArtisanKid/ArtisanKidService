package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.InRoomNoticeOuterClass;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
public class InRoom {
    private Logger logger = LoggerFactory.getLogger(Finish.class);

    public void InRoomNotice(ChannelHandlerContext ctx, String roomID) {
        InRoomNoticeOuterClass.InRoomNotice.Builder notice = InRoomNoticeOuterClass.InRoomNotice.newBuilder();
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setRoomId(roomID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setInRoomNotice(notice);
        ctx.writeAndFlush(container);
    }
}
