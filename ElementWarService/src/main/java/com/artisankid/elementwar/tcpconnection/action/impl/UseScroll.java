package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.*;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import com.artisankid.elementwar.tcpconnection.gate.utils.Room;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class UseScroll {
    private Logger logger = LoggerFactory.getLogger(UseCard.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.USE_SCROLL_MESSAGE_FIELD_NUMBER)
    public void useScrollMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        UseScrollMessageOuterClass.UseScrollMessage message = container.getUseScrollMessage();
        String messageID = message.getMessageId();
        String senderID = message.getSenderId();

        Room room = RoomManager.getRoom(senderID);
        if(room == null) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " room为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        if(UserManager.getUser(senderID).getGameState() != User.GameState.Playing) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " senderID和当前出卷轴用户不一致";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        MagicianDao magicianDao = new MagicianDao();
        Magician receiver = magicianDao.selectByOpenID(receiverID);
        if(receiver == null) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        String scrollID = message.getScrollId();
        if(scrollID == null) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        ScrollDao scrollDao = new ScrollDao();
        Scroll scroll = scrollDao.selectByScrollID(scrollID);
        if(scroll == null) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        List<String> cardIDs = new ArrayList<>();
        CardDao cardDao = new CardDao();
        for(Balance balance : scroll.getFormula().getReactants()) {
            Card card = cardDao.selectByElementID(balance.getElementID());
            if(!UserManager.getUser(senderID).existCardID(card.getCardID())) {
                String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID:" + scrollID + " 用户不具有cardID";
                Error.ErrorNotice(senderID, messageID, 0, error);
                return;
            }

            cardIDs.add(card.getCardID());
        }

        logger.debug("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID:" + scrollID + " 开始处理出卷轴...");

        for(String cardID : cardIDs) {
            //使用掉此卷轴
            UserManager.getUser(senderID).removeCardID(cardID);
        }

        //根据卷轴效果处理血量
        //卷轴不区分对别人使用还是自己使用
        for(Effect effect : scroll.getEffects()) {
            switch (effect.getType()) {
                case Cure: {
                    Integer hp = UserManager.getUser(senderID).getHp();
                    hp += effect.getValue();
                    UserManager.getUser(senderID).setHp(hp);
                    break;
                }
                case Hurt: {
                    Integer hp = UserManager.getUser(receiverID).getHp();
                    hp -= effect.getValue();
                    UserManager.getUser(receiverID).setHp(hp);
                    break;
                }
                case Jump: {
                    Deal.DealNotice(senderID);
                    break;
                }
                case Draw:
                    Deal.DealNotice(senderID);
                    break;
            }
        }

        logger.debug("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID:" + scrollID + " 准备发送UseScrollNotice");

        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            useScrollNotice(user.getUserID(), messageID, senderID, receiverID, scrollID);
        }

        if(UserManager.getUser(receiverID).getHp() <= 0) {
            Finish.FinishNotice(senderID);
        } else if(UserManager.getUser(senderID).getHp() <= 0) {
            Finish.FinishNotice(receiverID);
        }
    }

    public void useScrollNotice(final String receiverID, final String messageID, final String senderID, final String effectReceiverID, final String scrollID) {
        logger.debug("UseScrollMessage " + " messageID:" + messageID + " receiverID:" + receiverID + " senderID:" + senderID + " effectReceiverID:" + effectReceiverID + " scrollID:" + scrollID + " 正在发送...");

        UseScrollNoticeOuterClass.UseScrollNotice.Builder notice = UseScrollNoticeOuterClass.UseScrollNotice.newBuilder();
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 10 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);

        notice.setSenderId(senderID);
        notice.setReceiverId(effectReceiverID);
        notice.setScrollId(scrollID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.UseScrollNotice);
        container.setUseScrollNotice(notice);

        //出牌操作的效果没有超时的概念
        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("UseScrollNotice " + " messageID:" + messageID + " receiverID:" + receiverID + " senderID:" + senderID + " effectReceiverID:" + effectReceiverID + " scrollID:" + scrollID + " 发送成功");
            }
        });
    }
}
