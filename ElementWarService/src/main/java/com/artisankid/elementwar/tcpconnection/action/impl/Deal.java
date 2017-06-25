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

    static public void PlayDealNotice(final String cardReceiverID) {
        logger.debug("PlayDealNotice" + " cardReceiverID:" + cardReceiverID + " 开始发送...");

        //这里需要随机算法来获取卡牌ID
        final List<String> cardIDs = new ArrayList<>();
        cardIDs.add(randomCardID());

        UserManager.getUser(cardReceiverID).addCardIDs(cardIDs);

        ContainerOuterClass.Container.Builder container = MakeDealNotice(cardReceiverID, cardIDs);
        for(final User user : RoomManager.getRoom(cardReceiverID).getUsers()) {
            if(user.getUserID().equals(cardReceiverID)) {
                final Timer timer = new Timer(true);
                TimerTask task = new TimerTask() {
                    public void run() {
                        logger.error("PlayDealNotice" + " receiverID:" + cardReceiverID + " cardIDs:" + cardIDs + " 发送超时，给下个用户发牌");
                        //发牌没有收到，换下一个用户收牌
                        DealNoticeNextUser(cardReceiverID);
                    }
                };
                timer.schedule(task, 10 * 1000L);

                ChannelHandlerContext ctx = UserContextManager.getUserContext(cardReceiverID);
                ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        timer.cancel();

                        logger.debug("PlayDealNotice" + " receiverID:" + cardReceiverID + " cardIDs:" + cardIDs + " 发送成功，准备出牌...");

                        //发牌成功，通知出牌
                        PlaySwitch.PlaySwitchNotice(cardReceiverID);
                    }
                });
            } else {
                OnlyDealNotice(user.getUserID(), container);
            }
        }
    }

    static public void DealNotice(final String cardReceiverID) {
        logger.debug("DealNotice" + " cardReceiverID:" + cardReceiverID + " 开始发送...");

        //这里需要随机算法来获取卡牌ID
        final List<String> cardIDs = new ArrayList<>();
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());

        UserManager.getUser(cardReceiverID).addCardIDs(cardIDs);

        ContainerOuterClass.Container.Builder container = MakeDealNotice(cardReceiverID, cardIDs);
        for(final User user : RoomManager.getRoom(cardReceiverID).getUsers()) {
            OnlyDealNotice(user.getUserID(), container);
        }
    }

    /**
     * 用户再次抽牌
     * @param cardReceiverID 收牌用户
     */
    static public void ExtraDealNotice(final String cardReceiverID) {
        logger.debug("ExtraDealNotice" + " cardReceiverID:" + cardReceiverID + " 开始发送...");

        //这里需要随机算法来获取卡牌ID
        final List<String> cardIDs = new ArrayList<>();
        cardIDs.add(randomCardID());

        UserManager.getUser(cardReceiverID).addCardIDs(cardIDs);

        ContainerOuterClass.Container.Builder container = MakeDealNotice(cardReceiverID, cardIDs);
        for(final User user : RoomManager.getRoom(cardReceiverID).getUsers()) {
            OnlyDealNotice(user.getUserID(), container);
        }
    }

    static private ContainerOuterClass.Container.Builder MakeDealNotice(final String cardReceiverID, final List<String> cardIDs) {
        DealNoticeOuterClass.DealNotice.Builder notice = DealNoticeOuterClass.DealNotice.newBuilder();
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 10 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setReceiverId(cardReceiverID);
        notice.addAllCardIds(cardIDs);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.DealNotice);
        container.setDealNotice(notice);
        return container;
    }

    static private void OnlyDealNotice(final String receiverID, ContainerOuterClass.Container.Builder container) {
        ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                logger.debug("OnlyDealNotice" + " receiverID:" + receiverID + " 发送成功");
            }
        });
    }

    static private String randomCardID() {
        Long index = System.currentTimeMillis() % 181;
        index += 1;
        Element element = new ElementDao().selectByIndex(index);
        Card card = new CardDao().selectByElementID(element.getElementID());
        return card.getCardID();
    }

    /*查找下一个可以发牌的用户*/
    static public void DealNoticeNextUser(String currentPlayerID) {
        User currentUser  = UserManager.getUser(currentPlayerID);
        if(currentUser.getStillDealTimes() > 0) {
            currentUser.minusStillDealTimes();
            PlayDealNotice(currentUser.getUserID());
            return;
        }

        List<User> users = RoomManager.getRoom(currentPlayerID).getUsers();
        Integer index = users.indexOf(currentUser);
        index += 1;
        if(index >= users.size()) {
            index = 0;
        }

        User nextUser = users.get(index);
        if(nextUser.getIgnoreDealTimes() > 0) {
            nextUser.minusIgnoreDealTimes();
            DealNoticeNextUser(nextUser.getUserID());
            return;
        }
        PlayDealNotice(nextUser.getUserID());
    }
}
