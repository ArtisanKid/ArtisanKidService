package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.CardDao;
import com.artisankid.elementwar.common.dao.ElementDao;
import com.artisankid.elementwar.common.ewmodel.Card;
import com.artisankid.elementwar.common.ewmodel.Element;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by LiXiangYu on 2017/4/26.
 */
@NettyAction
public class Deal {
    private static Logger logger = LoggerFactory.getLogger(Deal.class);

    static public void PlayDealNotice(final String receiverID) {
        logger.debug("PlayDealNotice" + " receiverID:" + receiverID + " 开始发送...");

        //这里需要随机算法来获取卡牌ID
        final List<String> cardIDs = new ArrayList<>();
        cardIDs.add(randomCardID());

        DealNoticeOuterClass.DealNotice.Builder notice = DealNoticeOuterClass.DealNotice.newBuilder();
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 10 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setReceiverId(receiverID);
        notice.addAllCardIds(cardIDs);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.DealNotice);
        container.setDealNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("PlayDealNotice" + " receiverID:" + receiverID + " cardIDs:" + cardIDs + " 发送超时，给下个用户发牌");
                //发牌没有收到，换下一个用户收牌
                DealNoticeNextUser(receiverID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                timer.cancel();

                logger.debug("PlayDealNotice" + " receiverID:" + receiverID + " cardIDs:" + cardIDs + " 发送成功，准备出牌...");

                //发牌成功，通知出牌
                PlaySwitch.PlaySwitchNotice(receiverID);
            }
        });
    }

    static public void DealNotice(final String receiverID) {
        logger.debug("DealNotice" + " receiverID:" + receiverID + " 开始发送...");

        //这里需要随机算法来获取卡牌ID
        final List<String> cardIDs = new ArrayList<>();
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());

        DealNoticeOuterClass.DealNotice.Builder notice = DealNoticeOuterClass.DealNotice.newBuilder();
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 10 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setReceiverId(receiverID);
        notice.addAllCardIds(cardIDs);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.DealNotice);
        container.setDealNotice(notice);

        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("DealNotice" + " receiverID:" + receiverID + " cardIDs:" + cardIDs + " 发送成功");
            }
        });;
    }

    static private String randomCardID() {
        Long index = System.currentTimeMillis() % 181;
        Element element = new ElementDao().selectByIndex(index);
        Card card = new CardDao().selectByElementID(element.getElementID());
        return card.getCardID();
    }

    static public void DealNoticeNextUser(String currentPlayerID) {
        User currentUser  = UserManager.getUser(currentPlayerID);
        List<User> users = RoomManager.getRoom(currentPlayerID).getUsers();
        Integer index = users.indexOf(currentUser);
        index++;
        if(index >= users.size()) {
            index = 0;
        }

        User nextUser = users.get(index);
        DealNotice(nextUser.getUserID());
    }
}
