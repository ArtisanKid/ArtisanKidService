package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Card;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseCardMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseCardNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class UseCard {
    private Logger logger = LoggerFactory.getLogger(UseCard.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.USE_CARD_MESSAGE_FIELD_NUMBER)
    public void UseCardMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        UseCardMessageOuterClass.UseCardMessage message = container.getUseCardMessage();

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            return;
        }

        MagicianDao magicianDao = new MagicianDao();
        Magician receiver = magicianDao.selectByOpenID(receiverID);
        if(receiver == null) {
            return;
        }

        String cardID = message.getCardId();
        if(cardID == null) {
            return;
        }

        CardDao cardDao = new CardDao();
        Card card = cardDao.selectByCardID(cardID);
        if(card == null) {
            return;
        }

        //TODO:根据卡牌效果处理血量

        ChannelHandlerContext targetContext = null;//TODO:获取对应用户的Context
        UseCardNotice(targetContext, message.getMessageId(), message.getSenderId(), receiverID, cardID);
    }

    public void UseCardNotice(ChannelHandlerContext ctx, String messageID, String senderID, String receiveID, String cardID) {
        UseCardNoticeOuterClass.UseCardNotice.Builder notice = UseCardNoticeOuterClass.UseCardNotice.newBuilder();
        notice.setMessageId(messageID);
        long now = System.currentTimeMillis();
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(now / 1000 + 200);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setSenderId(senderID);
        notice.setReceiverId(receiveID);
        notice.setCardId(cardID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setUseCardNotice(notice);
        ctx.writeAndFlush(container);
    }
}
