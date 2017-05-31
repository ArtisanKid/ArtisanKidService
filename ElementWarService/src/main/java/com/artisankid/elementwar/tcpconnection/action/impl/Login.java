package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.LoginMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.LoginNoticeOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.client.Client;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiXiangYu on 2017/4/25.
 */
@NettyAction
public class Login {
    private Logger logger = LoggerFactory.getLogger(Login.class);

    @ActionRequestMap(actionKey = ContainerOuterClass.Container.LOGIN_MESSAGE_FIELD_NUMBER)
    public void loginMessage(ChannelHandlerContext context, ContainerOuterClass.Container container) {
        logger.debug("LoginMessage 处理...");

        LoginMessageOuterClass.LoginMessage message = container.getLoginMessage();

        String openID = message.getSenderId();
        UserContextManager.setUserContext(openID, context);

        MagicianDao dao = new MagicianDao();
        Magician magician = dao.selectByOpenID(openID);
        Integer strength = magician.getStrength();

        User user = new User();
        user.setUserID(openID);
        user.setStrength(strength);
        UserManager.addUser(user);

        //TODO:给所有的朋友或者对手发送信息？

        String messageID = message.getMessageId();
        Long expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
        loginNotice(openID, messageID, openID, expiredTime);
    }

    public void loginNotice(final String receiverID, String messageID, final String userID, final Long expiredTime) {
        //需要将登录通知返回给用户，确定登录
        LoginNoticeOuterClass.LoginNotice.Builder notice = LoginNoticeOuterClass.LoginNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setUserId(userID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.LoginNotice);
        container.setLoginNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("LoginNotice 超时");
                UserContextManager.removeUserContext(userID);
                UserManager.removeUser(userID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        final ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                logger.debug("LoginNotice 成功");
                timer.cancel();
            }
        });
    }
}
