package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.MagicianDao;
import com.artisankid.elementwar.common.ewmodel.BaseMagician;
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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LiXiangYu on 2017/4/25.
 */
@NettyAction
public class Login {
    static private Logger logger = LoggerFactory.getLogger(Login.class);

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

        MagicianDao dao = new MagicianDao();
        Magician magician = dao.selectByOpenID(senderID);
        Integer strength = magician.getStrength();

        User user = UserManager.getUser(senderID);
        if(user == null) {
            //新登录用户
            user = new User();
        }
        user.setUserID(senderID);
        user.setStrength(strength);
        UserManager.addUser(user);

        //保存用户与上下文关系
        UserContextManager.setUserContext(senderID, context);

        Long expiredTime = new Double(message.getExpiredTime() * 1000L).longValue();
        Login.LoginNotice(senderID, messageID, expiredTime);
    }

    static private void LoginNotice(final String loginedUserID, final String messageID, final Long expiredTime) {
        logger.debug("LoginMessage" + " messageID:" + messageID + " loginedUserID:" + loginedUserID + " 开始发送...");

        //需要将登录通知返回给用户，确定登录
        final ContainerOuterClass.Container.Builder container = MakeLoginNotice(messageID, loginedUserID, expiredTime);

        final Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            public void run() {
                logger.error("LoginNotice" + " messageID:" + messageID + " loginedUserID:" + loginedUserID + " 发送超时");
                UserContextManager.removeContext(loginedUserID);
                UserManager.removeUser(loginedUserID);
            }
        };
        timer.schedule(task, expiredTime - System.currentTimeMillis());

        final ChannelHandlerContext ctx = UserContextManager.getContext(loginedUserID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(expiredTime <= System.currentTimeMillis()) {
                    return;
                }

                timer.cancel();

                if(future.isCancelled()
                        || !future.isSuccess()) {
                    logger.error("LoginNotice" + " messageID:" + messageID + " loginedUserID:" + loginedUserID  + " 发送失败");
                    UserContextManager.removeContext(loginedUserID);
                    UserManager.removeUser(loginedUserID);
                    return;
                }

                logger.debug("LoginNotice" + " messageID:" + messageID + " loginedUserID:" + loginedUserID + " 发送成功");

                User user = UserManager.getUser(loginedUserID);
                user.setConnectState(User.ConnectState.Connected);

                //给所有的朋友或者对手发送信息？
                RelationLoginNotice(loginedUserID, BaseMagician.UserRelation.Friend, container);
                RelationLoginNotice(loginedUserID, BaseMagician.UserRelation.Opponent, container);

                logger.debug("LoginNotice" + " messageID:" + messageID + " loginedUserID:" + loginedUserID + " 开始处理失去连接的错误");

                if(user.getState() == User.State.Free
                        || user.getState() == User.State.Matching
                        || user.getState() == User.State.Inviting) {
                    return;
                }

                Finish.FinishNotice("其他用户");
            }
        });
    }

    static private void RelationLoginNotice(String baseUserID, BaseMagician.UserRelation relation, ContainerOuterClass.Container.Builder container) {
        MagicianDao dao = new MagicianDao();
        List<BaseMagician> relationUsers =  dao.selectByRelation(baseUserID, relation);
        for(BaseMagician magician : relationUsers) {
            String userID = magician.getOpenID();
            if(userID == null) {
                continue;
            }

            User friend = UserManager.getUser(userID);
            if(friend == null) {
                continue;
            }

            if(friend.getConnectState() == User.ConnectState.Disconnected) {
                continue;
            }

            Login.OnlyLoginNotice(userID, container);
        }
    }

    static private void OnlyLoginNotice(final String receiverID, ContainerOuterClass.Container.Builder container) {
        logger.debug("OnlyLoginNotice" + " receiverID:" + receiverID + " 开始发送...");

        final ChannelHandlerContext ctx = UserContextManager.getContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("OnlyLoginNotice" + " receiverID:" + receiverID + " 发送成功");
            }
        });
    }

    static private ContainerOuterClass.Container.Builder MakeLoginNotice(final String messageID, final String loginUserID, final Long expiredTime) {
        //需要将登录通知返回给用户，确定登录
        LoginNoticeOuterClass.LoginNotice.Builder notice = LoginNoticeOuterClass.LoginNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(System.currentTimeMillis() / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setUserId(loginUserID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.LoginNotice);
        container.setLoginNotice(notice);

        return container;
    }
}
