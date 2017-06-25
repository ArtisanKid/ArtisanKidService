package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Card;
import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseCardMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseCardNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class UseCard {
    static private Logger logger = LoggerFactory.getLogger(UseCard.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.USE_CARD_MESSAGE_FIELD_NUMBER)
    public void useCardMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        UseCardMessageOuterClass.UseCardMessage message = container.getUseCardMessage();
        String messageID = message.getMessageId();
        String senderID = message.getSenderId();

        Room room = RoomManager.getRoom(senderID);
        if(room == null) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " room为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        if(UserManager.getUser(senderID).getGameState() != User.GameState.Playing) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " senderID和当前出牌用户不一致";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        MagicianDao magicianDao = new MagicianDao();
        Magician receiver = magicianDao.selectByOpenID(receiverID);
        if(receiver == null) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        String cardID = message.getCardId();
        if(cardID == null) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " cardID为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        CardDao cardDao = new CardDao();
        Card card = cardDao.selectByCardID(cardID);
        if(card == null) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " cardID无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        if(!UserManager.getUser(senderID).existCardID(cardID)) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 用户不具有cardID";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        logger.debug("UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " cardID:" + cardID + " 开始处理出牌...");

        //使用掉此卡牌
        UserManager.getUser(senderID).removeCardID(cardID);

        //根据卡牌效果处理血量
        //卡牌区分对别人使用还是自己使用
        for(Effect effect : card.getEffects()) {
            switch (effect.getType()) {
                case Cure: {
                    UserManager.getUser(receiverID).addHp(effect.getValue());
                    break;
                }
                case Hurt: {
                    UserManager.getUser(receiverID).minusHp(effect.getValue());
                    break;
                }
                case Jump: {
                    UserManager.getUser(receiverID).addIgnoreDealTimes();
                    break;
                }
                case Draw:
                    Deal.ExtraDealNotice(senderID);
                    break;
            }
        }

        logger.debug("UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " cardID:" + cardID + " 准备发送UseCardNotice...");

        UseCard.UseCardNotice(messageID, senderID, receiverID, cardID);

        if(UserManager.getUser(receiverID).getHp() <= 0) {
            Finish.FinishNotice(senderID);
        } else if(UserManager.getUser(senderID).getHp() <= 0) {
            Finish.FinishNotice(receiverID);
        }
    }

    static public void UseCardNotice(final String messageID, final String senderID, final String effectReceiverID, final String cardID) {
        logger.debug("UseCardNotice " + " messageID:" + messageID + " senderID:" + senderID + " effectReceiverID:" + effectReceiverID + " cardID:" + cardID + " 开始发送...");

        UseCardNoticeOuterClass.UseCardNotice.Builder notice = UseCardNoticeOuterClass.UseCardNotice.newBuilder();
        notice.setMessageId(messageID);
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 10 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);

        notice.setSenderId(senderID);
        notice.setReceiverId(effectReceiverID);
        notice.setCardId(cardID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.UseCardNotice);
        container.setUseCardNotice(notice);

        ChannelHandlerContext ctx = UserContextManager.getContext(senderID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("UseCardNotice " + " messageID:" + messageID + " senderID:" + senderID + " effectReceiverID:" + effectReceiverID + " cardID:" + cardID + " 发送成功");
            }
        });

        //其他用户的通知没有消息ID
        notice.setMessageId(new String());
        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            if(user.getUserID().equals(senderID)) {
                return;
            }

            ChannelHandlerContext otherCtx = UserContextManager.getContext(user.getUserID());
            otherCtx.writeAndFlush(container);
        }
    }
}
