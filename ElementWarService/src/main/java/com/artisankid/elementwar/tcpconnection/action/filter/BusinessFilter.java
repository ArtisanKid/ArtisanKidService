package com.artisankid.elementwar.tcpconnection.action.filter;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import io.netty.channel.ChannelHandlerContext;

/**
 * 业务逻辑过滤器
 *
 * Created by WangShaohua on 2017/4/30.
 */
public class BusinessFilter {
    /**
     * 业务逻辑是否需要进行过滤
     *
     * @return
     */
    public static boolean isIn(int key, Object... args){
        ChannelHandlerContext ctx = (ChannelHandlerContext)args[0];
        ContainerOuterClass.Container container = (ContainerOuterClass.Container)args[1];

        switch (container.getMessageType()) {
            case ContainerOuterClass.Container.MessageType.LoginMessage(100):
                break;
            case ContainerOuterClass.Container.MessageType.MatchMessage(120):
                break;
            case ContainerOuterClass.Container.MessageType.InviteMessage(121):
                break;
            case ContainerOuterClass.Container.MessageType.InviteReplyMessage(122):
                break;
            case ContainerOuterClass.Container.MessageType.UseCardMessage(123):
                break;
            case ContainerOuterClass.Container.MessageType.UseScrollMessage(124):
                break;
            default:
                break;;
        }

        return Boolean.TRUE;
    }
}
