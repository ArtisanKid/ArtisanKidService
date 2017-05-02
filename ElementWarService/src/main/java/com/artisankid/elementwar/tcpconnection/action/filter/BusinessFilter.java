package com.artisankid.elementwar.tcpconnection.action.filter;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
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

        switch (container.getMessageType().getNumber()) {
            case ContainerOuterClass.Container.LOGIN_MESSAGE_FIELD_NUMBER:
                break;
            case ContainerOuterClass.Container.MATCH_MESSAGE_FIELD_NUMBER:
                break;
            case ContainerOuterClass.Container.INVITE_MESSAGE_FIELD_NUMBER:
                break;
            case ContainerOuterClass.Container.INVITE_REPLY_MESSAGE_FIELD_NUMBER:
                break;
            case ContainerOuterClass.Container.USE_CARD_MESSAGE_FIELD_NUMBER:
                break;
            case ContainerOuterClass.Container.USE_SCROLL_MESSAGE_FIELD_NUMBER:
                break;
            default:
                break;
        }

        return Boolean.TRUE;
    }
}
