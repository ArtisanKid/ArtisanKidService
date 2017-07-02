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

        User sender = UserManager.getUser(senderID);
        if(sender.getGameState() != User.GameState.Playing) {
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
        Magician magician = magicianDao.selectByOpenID(receiverID);
        if(magician == null) {
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

        if(sender.existCardID(cardID) == 0) {
            String error = "UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " 用户不具有cardID";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        logger.debug("UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " cardID:" + cardID + " 开始处理出牌...");

        //使用掉此卡牌
        sender.removeCardID(cardID);

        User receiver = UserManager.getUser(receiverID);

        String effectInfo = "元素效果：";
        //根据卡牌效果处理血量
        //卡牌区分对别人使用还是自己使用
        for(Effect effect : card.getEffects()) {
            switch (effect.getType()) {
                case Cure: {
                    receiver.addHp(effect.getValue());
                    effectInfo += "\n" + receiverID + "恢复" + effect.getValue() + "点生命";
                    break;
                }
                case Hurt: {
                    receiver.minusHp(effect.getValue());
                    effectInfo += "\n" + receiverID + "受到" + effect.getValue() + "点伤害\n";
                    break;
                }
                case Jump: {
                    receiver.addIgnoreDealTimes(effect.getValue());
                    effectInfo += "\n" + receiverID + "失去" + effect.getValue() + "次攻击机会\n";
                    break;
                }
                case Draw:
                    for(Integer i = 0; i < effect.getValue(); i++) {
                        Deal.ExtraDealNotice(senderID);
                    }
                    effectInfo += "\n" + receiverID + "意外获得" + effect.getValue() + "个的魔法元素\n";
                    break;
            }
        }

        logger.debug("UseCardMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " cardID:" + cardID + " 准备发送UseCardNotice...");

        EpicManager.WriteEpic(RoomManager.getRoom(senderID).getRoomID(), "使用" + card.getElement().getCname() + "元素，" + effectInfo);

        UseCard.UseCardNotice(messageID, senderID, receiverID, cardID);

        if(receiver.getHp() <= 0) {
            Dead.DeadNotice(receiverID);
        }

        boolean everyoneDead = true;
        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            String userID = user.getUserID();
            if(userID.equals(senderID)) {
                continue;
            } else {
                if(user.getHp() > 0) {
                    everyoneDead = false;
                }
            }
        }

        if(everyoneDead) {
            Finish.FinishNotice(senderID);
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

        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            if(user.getUserID().equals(senderID)) {
                ChannelHandlerContext ctx = UserContextManager.getContext(senderID);
                ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.debug("UseCardNotice " + " messageID:" + messageID + " senderID:" + senderID + " effectReceiverID:" + effectReceiverID + " cardID:" + cardID + " 发送成功");
                    }
                });
            } else {
                //其他用户的通知没有消息ID
                notice.setMessageId(new String());
                ChannelHandlerContext otherCtx = UserContextManager.getContext(user.getUserID());
                otherCtx.writeAndFlush(container);
            }
        }
    }
}
