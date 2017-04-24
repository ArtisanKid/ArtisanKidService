package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass.Container;
import com.artisankid.elementwar.ewmessagemodel.MatchMessageOuterClass.MatchMessage;
import com.artisankid.elementwar.ewmessagemodel.MatchNoticeOuterClass.MatchNotice;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by LiXiangYu on 2017/4/24.
 */
public class Match {
    private Logger logger = LoggerFactory.getLogger(Notice.class);
    /**
     * 处理消息逻辑
     *
     * @param o
     * @return
     */
    @ActionRequestMap(actionKey = Container.MATCH_MESSAGE_FIELD_NUMBER)
    public void matchNotice(ChannelHandlerContext ctx, Object o) {

        Container container = (Container) o;
        MatchMessage message = container.getMatchMessage();
        String messageId = message.getMessageId();
        logger.error("receive msg:" + messageId);

        MatchNotice.Builder notice = MatchNotice.newBuilder();
        notice.setMessageId("0000_000002");
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setCalibrationTime(now);

        Container.Builder noticeContainer = Container.newBuilder();
        noticeContainer.setMatchNotice(notice);
        ctx.writeAndFlush(noticeContainer);
    }
}
