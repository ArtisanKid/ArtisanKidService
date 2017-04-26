package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class Deal {
    private Logger logger = LoggerFactory.getLogger(Deal.class);

    public void DealNotice(ChannelHandlerContext ctx, String receiverID, List<String> cardIDs) {
        DealNoticeOuterClass.DealNotice.Builder notice = DealNoticeOuterClass.DealNotice.newBuilder();
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setReceiverId(receiverID);
        for(Integer i = 0; i < cardIDs.size(); i++) {
            notice.setCardIds(i, cardIDs.get(i));
        }

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setDealNotice(notice);
        ctx.writeAndFlush(container);
    }
}
