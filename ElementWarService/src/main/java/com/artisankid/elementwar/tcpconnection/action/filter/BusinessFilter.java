package com.artisankid.elementwar.tcpconnection.action.filter;

import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.utils.MagicianManager;
import com.artisankid.elementwar.common.utils.TokenManager;
import com.artisankid.elementwar.ewmessagemodel.*;
import com.artisankid.elementwar.tcpconnection.action.impl.Login;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 业务逻辑过滤器
 * <p>
 * Created by WangShaohua on 2017/4/30.
 */
public class BusinessFilter {
    private static Logger logger = LoggerFactory.getLogger(Login.class);

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
        String messageName = null;
        switch (container.getMessageType().getNumber()) {
            case ContainerOuterClass.Container.LOGIN_MESSAGE_FIELD_NUMBER: {
                messageName = "LOGIN_MESSAGE";

                LoginMessageOuterClass.LoginMessage message = container.getLoginMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();

                senderID = message.getSenderId();
                if(senderID == null || senderID.length() == 0) {
                    logger.error("LOGIN_MESSAGE senderID为空");
                    return Boolean.FALSE;
                }

                if(MagicianManager.VerifyOpenID(senderID) == Boolean.FALSE) {
                    logger.error("LOGIN_MESSAGE senderID无效");
                    return Boolean.FALSE;
                }

                String accessToken = message.getAccessToken();
                if (accessToken == null || accessToken.length() == 0) {
                    logger.error("LOGIN_MESSAGE accessToken为空");
                    return Boolean.FALSE;
                }

                if(TokenManager.VerifyAccessToken(accessToken) == Boolean.FALSE) {
                    //token验证失败，等待客户端自然超时
                    logger.error("LOGIN_MESSAGE accessToken无效");
                    return Boolean.FALSE;
                }

                if (TokenManager.VerifyAccessToken(senderID, accessToken) == Boolean.FALSE) {
                    logger.error("LOGIN_MESSAGE accessToken不匹配");
                    return Boolean.FALSE;
                }
                break;
            }
            case ContainerOuterClass.Container.MATCH_MESSAGE_FIELD_NUMBER: {
                messageName = "MATCH_MESSAGE";
                MatchMessageOuterClass.MatchMessage message = container.getMatchMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.INVITE_MESSAGE_FIELD_NUMBER: {
                messageName = "INVITE_MESSAGE";
                InviteMessageOuterClass.InviteMessage message = container.getInviteMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.INVITE_REPLY_MESSAGE_FIELD_NUMBER: {
                messageName = "INVITE_REPLY_MESSAGE";
                InviteReplyMessageOuterClass.InviteReplyMessage message = container.getInviteReplyMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.USE_CARD_MESSAGE_FIELD_NUMBER: {
                messageName = "USE_CARD_MESSAGE";
                UseCardMessageOuterClass.UseCardMessage message = container.getUseCardMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            case ContainerOuterClass.Container.USE_SCROLL_MESSAGE_FIELD_NUMBER: {
                messageName = "USE_SCROLL_MESSAGE";
                UseScrollMessageOuterClass.UseScrollMessage message = container.getUseScrollMessage();
                expiredTime = new Double(message.getExpiredTime() * 1000).longValue();
                senderID = message.getSenderId();
                break;
            }
            default:
                break;
        }

        if(senderID == null || senderID.length() == 0) {
            logger.error(messageName + " senderID为空");
            return Boolean.FALSE;
        }

        if(MagicianManager.VerifyOpenID(senderID) == Boolean.FALSE) {
            logger.error(messageName + " senderID无效");
            return Boolean.FALSE;
        }

        if(expiredTime <= System.currentTimeMillis()) {
            logger.error(messageName + " 超时");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
