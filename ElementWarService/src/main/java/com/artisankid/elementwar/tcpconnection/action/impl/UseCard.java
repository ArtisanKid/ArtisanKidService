package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Card;
import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseCardMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseCardNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class UseCard {
    private Logger logger = LoggerFactory.getLogger(UseCard.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.USE_CARD_MESSAGE_FIELD_NUMBER)
    public void useCardMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        UseCardMessageOuterClass.UseCardMessage message = container.getUseCardMessage();
        String senderID = message.getSenderId();

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            logger.error("UseCardMessage " + " senderID:" + senderID + " receiverID为空");
            return;
        }

        MagicianDao magicianDao = new MagicianDao();
        Magician receiver = magicianDao.selectByOpenID(receiverID);
        if(receiver == null) {
            logger.error("UseCardMessage " + " senderID:" + senderID + " receiverID无效");
            return;
        }

        String cardID = message.getCardId();
        if(cardID == null) {
            logger.error("UseCardMessage " + " senderID:" + senderID + " receiverID:" + receiverID + " cardID为空");
            return;
        }

        CardDao cardDao = new CardDao();
        Card card = cardDao.selectByCardID(cardID);
        if(card == null) {
            logger.error("UseCardMessage " + " senderID:" + senderID + " receiverID:" + receiverID + " cardID无效");
            return;
        }

        logger.debug("UseCardMessage " + " senderID:" + senderID + " receiverID:" + receiverID + " cardID:" + cardID + " 开始处理出牌...");

        //根据卡牌效果处理血量
        //卡牌区分对别人使用还是自己使用
        for(Effect effect : card.getEffects()) {
            switch (effect.getType()) {
                case Cure: {
                    Integer hp = UserManager.getUser(receiverID).getHp();
                    hp += effect.getValue();
                    UserManager.getUser(receiverID).setHp(hp);
                    break;
                }
                case Hurt: {
                    Integer hp = UserManager.getUser(receiverID).getHp();
                    hp -= effect.getValue();
                    UserManager.getUser(receiverID).setHp(hp);
                    break;
                }
                case Jump: {
                    for (User user : RoomManager.getRoom(senderID).getUsers()) {
                        if (user.getUserID() == receiverID) {
                            continue;
                        }
                        Deal.DealNotice(user.getUserID(), Arrays.asList("CO2"));
                    }
                    break;
                }
                case Draw:
                    Deal.DealNotice(senderID, Arrays.asList("CO2"));
                    break;
            }
        }

        logger.debug("UseCardMessage " + " senderID:" + senderID + " receiverID:" + receiverID + " cardID:" + cardID + " 准备发送UseCardNotice...");

        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            useCardNotice(user.getUserID(), senderID, receiverID, cardID);
        }

        if(UserManager.getUser(receiverID).getHp() <= 0) {
            Finish.FinishNotice(senderID);
        } else if(UserManager.getUser(senderID).getHp() <= 0) {
            Finish.FinishNotice(receiverID);
        }
    }

    public void useCardNotice(String receiverID, String senderID, String effectReceiverID, String cardID) {
        logger.debug("UseCardNotice " + " senderID:" + senderID + " receiverID:" + receiverID + " effectReceiverID:" + effectReceiverID + " cardID:" + cardID + " 开始发送...");

        UseCardNoticeOuterClass.UseCardNotice.Builder notice = UseCardNoticeOuterClass.UseCardNotice.newBuilder();
        long now = System.currentTimeMillis();
        long expiredTime = now + 10 * 1000;
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.FALSE);

        notice.setSenderId(senderID);
        notice.setReceiverId(effectReceiverID);
        notice.setCardId(cardID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.UseCardNotice);
        container.setUseCardNotice(notice);

        //出牌操作的效果没有超时的概念
        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container);
    }
}
