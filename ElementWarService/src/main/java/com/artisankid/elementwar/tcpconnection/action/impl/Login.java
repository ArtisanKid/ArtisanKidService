package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.Magician;
import com.artisankid.elementwar.common.utils.TokenManager;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.LoginMessageOuterClass;
import com.artisankid.elementwar.ewmessagemodel.LoginNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.gate.utils.*;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
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
        LoginMessageOuterClass.LoginMessage message = container.getLoginMessage();
        String messageID = message.getMessageId();
        String senderID = message.getSenderId();

        String accessToken = message.getAccessToken();
        if (accessToken == null || accessToken.length() == 0) {
            String error = "LoginMessage" + " messageID:" + messageID + " senderID:" + senderID + " accessToken为空";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        if(TokenManager.VerifyAccessToken(accessToken) == Boolean.FALSE) {
            String error = "LoginMessage" + " messageID:" + messageID + " senderID:" + senderID + " accessToken无效";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        if (TokenManager.VerifyAccessToken(senderID, accessToken) == Boolean.FALSE) {
            String error = "LoginMessage" + " messageID:" + messageID + " senderID:" + senderID + " accessToken不匹配";
            Error.ErrorNotice(senderID, messageID, 0, error);
            return;
        }

        logger.debug("LoginMessage " + " messageID:" + messageID + " senderID:" + senderID + " 开始登录...");

        UserContextManager.setUserContext(senderID, context);

        MagicianDao dao = new MagicianDao();
        Magician magician = dao.selectByOpenID(senderID);
        Integer strength = magician.getStrength();

        User user = new User();
        user.setUserID(senderID);
        user.setStrength(strength);
        UserManager.addUser(user);

        //TODO:给所有的朋友或者对手发送信息？

        Long expiredTime = new Double(message.getExpiredTime() * 1000L).longValue();
        loginNotice(senderID, messageID, senderID, expiredTime);
    }

    public void loginNotice(final String receiverID, final String messageID, final String userID, final Long expiredTime) {
        logger.debug("LoginMessage" + " messageID:" + messageID + " receiverID:" + receiverID + " targetID:" + userID + " 开始发送...");

        //需要将登录通知返回给用户，确定登录
        LoginNoticeOuterClass.LoginNotice.Builder notice = LoginNoticeOuterClass.LoginNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setUserId(userID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.LoginNotice);
        container.setLoginNotice(notice);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("LoginNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " targetID:" + userID + " 发送超时");
                UserContextManager.removeUserContext(userID);
                UserManager.removeUser(userID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        final ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                timer.cancel();

                logger.debug("LoginNotice" + " messageID:" + messageID + " receiverID:" + receiverID + " targetID:" + userID + " 发送成功");
            }
        });
    }
}
