package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.ScrollDao;
import com.artisankid.elementwar.common.ewmodel.Effect;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.ewmodel.Scroll;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.UseScrollNoticeOuterClass;
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
public class UseScroll {
    private Logger logger = LoggerFactory.getLogger(UseCard.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.USE_SCROLL_MESSAGE_FIELD_NUMBER)
    public void useScrollMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        UseScrollMessageOuterClass.UseScrollMessage message = container.getUseScrollMessage();
        String messageID = message.getMessageId();
        String senderID = message.getSenderId();

        Room room = RoomManager.getRoom(senderID);
        if(room == null) {
            logger.error("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " room为空");
            return;
        }

        if(UserManager.getUser(senderID).getGameState() != User.GameState.Playing) {
            logger.error("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " senderID和当前出卷轴用户不一致");
            return;
        }

        String receiverID = message.getReceiverId();
        if(receiverID == null) {
            logger.error("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID为空");
            return;
        }

        MagicianDao magicianDao = new MagicianDao();
        Magician receiver = magicianDao.selectByOpenID(receiverID);
        if(receiver == null) {
            logger.error("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID无效");
            return;
        }

        String scrollID = message.getScrollId();
        if(scrollID == null) {
            logger.error("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID为空");
            return;
        }

        ScrollDao scrollDao = new ScrollDao();
        Scroll scroll = scrollDao.selectByScrollID(scrollID);
        if(scroll == null) {
            logger.error("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID无效");
            return;
        }

        logger.debug("UseScrollMessage " + " messageID:" + messageID + " senderID:" + senderID + " receiverID:" + receiverID + " scrollID:" + scrollID + " 开始处理出卷轴...");

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

    public void useScrollNotice(String receiverID, String messageID, String senderID, String effectReceiverID, String scrollID) {
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
        ctx.writeAndFlush(container);
    }
}
