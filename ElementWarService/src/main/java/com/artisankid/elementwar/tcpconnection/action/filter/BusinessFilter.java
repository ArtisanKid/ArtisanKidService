package com.artisankid.elementwar.tcpconnection.action.filter;

import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.utils.MagicianManager;
import com.artisankid.elementwar.common.utils.TokenManager;
import com.artisankid.elementwar.ewmessagemodel.*;
import io.netty.channel.ChannelHandlerContext;

/**
 * 业务逻辑过滤器
 * <p>
 * Created by WangShaohua on 2017/4/30.
 */
public class BusinessFilter {
    /**
     * 业务逻辑是否需要进行过滤
     *
     * @return
     */
    public static boolean isIn(int key, Object... args) {

        ChannelHandlerContext ctx = (ChannelHandlerContext) args[0];
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) args[1];

        long expiredTime = 0;
        String senderID = null;
        switch (container.getMessageType().getNumber()) {
            case ContainerOuterClass.Container.LOGIN_MESSAGE_FIELD_NUMBER: {
                LoginMessageOuterClass.LoginMessage message = container.getLoginMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();

                senderID = message.getSenderId();
                if(senderID == null || senderID.length() == 0) {
                    return Boolean.FALSE;
                }

                if(MagicianManager.VerifyOpenID(senderID) == Boolean.FALSE) {
                    return Boolean.FALSE;
                }

                String accessToken = message.getAccessToken();
                if (accessToken == null || accessToken.length() == 0) {
                    return Boolean.FALSE;
                }

                TokenDao dao = new TokenDao();
                if(dao.selectByAccessToken(accessToken) == null) {
                    //token验证失败，等待客户端自然超时
                    return Boolean.FALSE;
                }

                if (TokenManager.VerifyAccessToken(senderID, accessToken) == Boolean.FALSE) {
                    return Boolean.FALSE;
                }
                break;
            }
            case ContainerOuterClass.Container.MATCH_MESSAGE_FIELD_NUMBER: {
                MatchMessageOuterClass.MatchMessage message = container.getMatchMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.INVITE_MESSAGE_FIELD_NUMBER: {
                InviteMessageOuterClass.InviteMessage message = container.getInviteMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.INVITE_REPLY_MESSAGE_FIELD_NUMBER: {
                InviteReplyMessageOuterClass.InviteReplyMessage message = container.getInviteReplyMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.USE_CARD_MESSAGE_FIELD_NUMBER: {
                UseCardMessageOuterClass.UseCardMessage message = container.getUseCardMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.USE_SCROLL_MESSAGE_FIELD_NUMBER: {
                UseScrollMessageOuterClass.UseScrollMessage message = container.getUseScrollMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            default:
                break;
        }

        if(expiredTime <= System.currentTimeMillis()) {
            return Boolean.FALSE;
        }

        if(senderID == null) {
            return Boolean.FALSE;
        }
        if(MagicianManager.VerifyOpenID(senderID) == Boolean.FALSE) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
