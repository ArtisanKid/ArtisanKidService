package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.ewmodel.Scroll;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class UseScroll {
    private Logger logger = LoggerFactory.getLogger(UseCard.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.USE_SCROLL_MESSAGE_FIELD_NUMBER)
    public void UseScrollMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        UseScrollMessageOuterClass.UseScrollMessage message = container.getUseScrollMessage();

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        MagicianDao magicianDao = new MagicianDao();
        Magician receiver = magicianDao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        String scrollID = message.getScrollId();
        if(scrollID == null) {
            return;
        }

        ScrollDao scrollDao = new ScrollDao();
        Scroll scroll = scrollDao.selectByScrollID(scrollID);
        if(scroll == null) {
            return;
        }

        //TODO:根据卷轴效果处理血量

        ChannelHandlerContext targetContext = null;//TODO:获取对应用户的Context
        UseScrollNotice(targetContext, message.getMessageId(), message.getSenderId(), receiverID, scrollID);
    }

    public void UseScrollNotice(ChannelHandlerContext ctx, String messageID, String senderID, String receiveID, String scrollID) {
        UseScrollNoticeOuterClass.UseScrollNotice.Builder notice = UseScrollNoticeOuterClass.UseScrollNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setSenderId(senderID);
        notice.setReceiverId(receiveID);
        notice.setScrollId(scrollID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setUseScrollNotice(notice);
        ctx.writeAndFlush(container);
    }
}
