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
                final Long expiredTime = System.currentTimeMillis() + 10 * 1000L;
                timer.schedule(task, expiredTime - System.currentTimeMillis());

                ChannelHandlerContext ctx = UserContextManager.getContext(cardReceiverID);
                ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if(expiredTime <= System.currentTimeMillis()) {
                            return;
                        }

                        timer.cancel();

                        if(future.isCancelled()
                                || !future.isSuccess()) {
                            logger.error("PlayDealNotice" + " receiverID:" + cardReceiverID + " cardIDs:" + cardIDs + " 发送失败");
                            DealNoticeNextUser(cardReceiverID);
                            return;
                        }

                        logger.debug("PlayDealNotice" + " receiverID:" + cardReceiverID + " cardIDs:" + cardIDs + " 发送成功，准备出牌...");

                        EpicManager.WriteEpic(RoomManager.getRoom(cardReceiverID).getRoomID(), "获得了魔法元素" + cardIDs);

                        //发牌成功，通知出牌
                        PlaySwitch.PlaySwitchNotice(cardReceiverID);
                    }
                });
            } else {
                final String userID = user.getUserID();
                ChannelHandlerContext ctx = UserContextManager.getContext(userID);
                ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        logger.debug("PlayDealNotice" + " receiverID:" + userID + " 发送成功");
                        EpicManager.WriteEpic(RoomManager.getRoom(cardReceiverID).getRoomID(), cardReceiverID + "获得了" + cardIDs.size() + "个魔法元素");
                    }
                });
            }
        }
    }

    static public void InitDealNotice(final String cardReceiverID) {
        logger.debug("InitDealNotice" + " cardReceiverID:" + cardReceiverID + " 开始发送...");

        //这里需要随机算法来获取卡牌ID
        final List<String> cardIDs = new ArrayList<>();
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());
        cardIDs.add(randomCardID());

        UserManager.getUser(cardReceiverID).addCardIDs(cardIDs);

        OnlyDealNotice(cardReceiverID, cardIDs);
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

        OnlyDealNotice(cardReceiverID, cardIDs);
    }

    static private void OnlyDealNotice(final String cardReceiverID, final List<String> cardIDs) {
        ContainerOuterClass.Container.Builder container = MakeDealNotice(cardReceiverID, cardIDs);
        for(final User user : RoomManager.getRoom(cardReceiverID).getUsers()) {
            ChannelHandlerContext ctx = UserContextManager.getContext(user.getUserID());
            if(user.getUserID().equals(cardReceiverID)) {
                ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.debug("OnlyDealNotice" + " receiverID:" + cardReceiverID + " 发送成功");
                        EpicManager.WriteEpic(RoomManager.getRoom(cardReceiverID).getRoomID(), "获得了魔法元素" + cardIDs);
                    }
                });
            } else {
                ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        logger.debug("OnlyDealNotice" + " receiverID:" + cardReceiverID + " 发送成功");
                        EpicManager.WriteEpic(RoomManager.getRoom(cardReceiverID).getRoomID(), cardReceiverID + "获得了" + cardIDs.size() + "个魔法元素");
                    }
                });
            }
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

        Room room = RoomManager.getRoom(currentPlayerID);
        if(room == null) {
            return;
        }

        List<User> users = room.getUsers();
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
