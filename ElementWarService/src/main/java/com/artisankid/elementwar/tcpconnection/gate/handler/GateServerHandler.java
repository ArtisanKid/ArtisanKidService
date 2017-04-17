package com.artisankid.elementwar.tcpconnection.gate.handler;

import com.alibaba.fastjson.JSON;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnectionMap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by shaohua.wang on 2017/04/16.
 */
public class GateServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GateServerHandler.class);

//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
//        logger.error("receive msg:" + JSON.toJSONString(o));
//        //ClientConnection conn = ClientConnectionMap.getClientConnection(channelHandlerContext);=
//        //ClientMessage.processTransferHandler(message, conn);
//    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.error("receive msg:" + JSON.toJSONString(msg));
        ctx.write("hello ,i'm server");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClientConnectionMap.addClientConnection(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        ClientConnectionMap.removeClientConnection(ctx);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
