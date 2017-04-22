package com.artisankid.elementwar.tcpconnection.gate.handler;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.tcpconnection.action.ActionMapUtil;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnectionMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 网关服务处理器，请求转发
 *
 * Created by shaohua.wang on 2017/04/16.
 */
public class GateServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GateServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) msg;
        ActionMapUtil.invote("dealNotice", ctx, container);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClientConnectionMap.addClientConnection(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
//        ClientConnectionMap.removeClientConnection(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }
}
