package com.artisankid.elementwar.tcpconnection.action.impl;

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
    public void loginMessage(ChannelHandlerContext ctx, ContainerOuterClass.Container container) {
        LoginMessageOuterClass.LoginMessage message = container.getLoginMessage();

        //首先判断是否超时
        long expiredTime = (long) (message.getExpiredTime() * 1000);
        long now = System.currentTimeMillis();
        if(expiredTime <= now) {
            return;
        }

        String messageID = message.getMessageId();
        String userID = message.getMessageId();
        String accessToken = message.getAccessToken();
        logger.debug("loginMessage:" + messageID + " userID:" + userID + " accessToken:" + accessToken);

        TokenDao dao = new TokenDao();
        if(dao.selectByAccessToken(accessToken) == null) {
            //token验证失败，等待客户端自然超时
            return;
        }

        matchNotice(userID, messageID, userID, expiredTime);
    }

    public void matchNotice(final String receiverID, String messageID, final String userID, final long expiredTime) {
        final long now = System.currentTimeMillis();
        if(expiredTime <= now) {
            return;
        }

        //需要将登录通知返回给用户，确定登录
        LoginNoticeOuterClass.LoginNotice.Builder notice = LoginNoticeOuterClass.LoginNotice.newBuilder();
        notice.setMessageId(messageID);
        notice.setSendTime(now / 1000);
        notice.setExpiredTime(expiredTime / 1000);
        notice.setNeedResponse(Boolean.FALSE);
        notice.setUserId(userID);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setLoginNotice(notice);

        final ChannelHandlerContext ctx = UserContextManager.getUserContext(receiverID);
        ctx.writeAndFlush(container).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(expiredTime > System.currentTimeMillis()) {
                    //将userID和ctx进行绑定
                    UserContextManager.setUserContext(userID, ctx);
                }
            }
        });
    }
}
