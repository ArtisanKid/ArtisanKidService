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
import com.sun.org.apache.xpath.internal.operations.Bool;
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
    static private Logger logger = LoggerFactory.getLogger(UseScroll.class);

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

        User sender = UserManager.getUser(senderID);
        if(sender.getGameState() != User.GameState.Playing) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " senderID和当前出卷轴用户不一致";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        String scrollID = message.getScrollId();
        if(scrollID == null) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " scrollID为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        ScrollDao scrollDao = new ScrollDao();
        Scroll scroll = scrollDao.selectByScrollID(scrollID);
        if(scroll == null) {
            String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " scrollID无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        List<String> cardIDs = new ArrayList<>();
        CardDao cardDao = new CardDao();
        for(Balance balance : scroll.getFormula().getReactants()) {
            Card card = cardDao.selectByElementID(balance.getElementID());
            if(sender.existCardID(card.getCardID()) != balance.getCount()) {
                String error = "UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " scrollID:" + scrollID + " 用户不具有cardID";
                Error.ErrorNotice(senderID, messageID, 0, error);
                return;
            }

            for(Integer i = 0; i < balance.getCount(); i++) {
                cardIDs.add(card.getCardID());
            }
        }

        logger.debug("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " scrollID:" + scrollID + " 开始处理出卷轴...");

        for(String cardID : cardIDs) {
            //使用掉此卷轴
            UserManager.getUser(senderID).removeCardID(cardID);
        }

        String effectInfo = "卷轴效果：";
        //根据卷轴效果处理血量
        //卷轴不区分对别人使用还是自己使用
        for(Effect effect : scroll.getEffects()) {
            switch (effect.getType()) {
                case Cure: {
                    sender.addHp(effect.getValue());
                    effectInfo += "\n" + senderID + "恢复" + effect.getValue() + "点生命";
                    break;
                }
                case Hurt: {
                    for(User user : RoomManager.getRoom(senderID).getUsers()) {
                        user.minusHp(effect.getValue());
                        effectInfo += "\n" + user.getUserID() + "受到" + effect.getValue() + "点伤害\n";
                    }
                    break;
                }
                case Jump: {
                    for(User user : RoomManager.getRoom(senderID).getUsers()) {
                        user.addIgnoreDealTimes(effect.getValue());
                        effectInfo += "\n" + user.getUserID() + "失去" + effect.getValue() + "次攻击机会\n";
                    }
                    break;
                }
                case Draw:
                    for(Integer i = 0; i < effect.getValue(); i++) {
                        Deal.ExtraDealNotice(senderID);
                    }
                    effectInfo += "\n" + senderID + "意外获得" + effect.getValue() + "个的元素\n";
                    break;
            }
        }

        logger.debug("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " scrollID:" + scrollID + " 准备发送UseScrollNotice");

        EpicManager.WriteEpic(RoomManager.getRoom(senderID).getRoomID(), "使用" + scroll.getName() + "元素，" + effectInfo);

        UseScroll.UseScrollNotice(messageID, senderID, scrollID);

        boolean everyoneDead = true;
        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            String userID = user.getUserID();
            if(userID.equals(senderID)) {
                continue;
            } else {
                if(user.getHp() <= 0) {
                    Dead.DeadNotice(userID);
                } else {
                    everyoneDead = false;
                }
            }
        }

        if(everyoneDead) {
            Finish.FinishNotice(senderID);
        }
    }

    static public void UseScrollNotice(final String messageID, final String senderID, final String scrollID) {
        logger.debug("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " scrollID:" + scrollID + " 正在发送...");

        UseScrollNoticeOuterClass.UseScrollNotice.Builder notice = UseScrollNoticeOuterClass.UseScrollNotice.newBuilder();
        notice.setMessageId(messageID);
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 10 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);

        notice.setSenderId(senderID);
        notice.setScrollId(scrollID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.UseScrollNotice);
        container.setUseScrollNotice(notice);

        for(User user : RoomManager.getRoom(senderID).getUsers()) {
            if(user.getUserID().equals(senderID)) {
                //出牌操作的效果没有超时的概念
                ChannelHandlerContext ctx = UserContextManager.getContext(senderID);
                ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.debug("UseScrollNotice " + " messageID:" + messageID + " senderID:" + senderID + " scrollID:" + scrollID + " 发送成功");
                    }
                });
            } else {
                //其他用户不需要关注messageID
                notice.setMessageId(new String());
                ChannelHandlerContext otherCtx = UserContextManager.getContext(user.getUserID());
                otherCtx.writeAndFlush(container);
            }
        }
    }
}
