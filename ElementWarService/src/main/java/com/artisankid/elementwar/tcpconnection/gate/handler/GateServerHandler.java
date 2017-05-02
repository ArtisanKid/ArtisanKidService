package com.artisankid.elementwar.tcpconnection.gate.handler;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.tcpconnection.action.ActionMapUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 网关服务处理器，请求转发
 * <p>
 * Created by shaohua.wang on 2017/04/16.
 */
@Component
public class GateServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GateServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) msg;
        int messageType = container.getMessageCase().getNumber();
        ActionMapUtil.invote(messageType, ctx, container);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.error("USER LOGIN-------------------------------------------------");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.error("CONNECTION OUT-------------------------------------------------");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }
}
