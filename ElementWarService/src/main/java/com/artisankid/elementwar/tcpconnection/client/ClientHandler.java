package com.artisankid.elementwar.tcpconnection.client;

import com.artisankid.elementwar.ewmodel.UseCardMessageOuterClass;
import com.artisankid.elementwar.tcpconnection.protobuf.Utils;
import com.artisankid.elementwar.tcpconnection.protobuf.generate.cli2srv.login.Auth;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Dell on 2016/2/15.
 * 模拟客户端聊天：自己给自己发消息
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object>  {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ByteBuf byteBuf = Unpooled.buffer(1024);
        byteBuf.writeInt(112);

        byteBuf =Unpooled.copiedBuffer("hello  fuck !!!!!!!", CharsetUtil.US_ASCII);

        System.out.println("Client send start" );
        ctx.writeAndFlush(byteBuf);
        System.out.println("Client send end ");    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead0  end ");
        logger.error("channelRead0  end");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete send end ");
        logger.error("channelReadComplete send end");
    }
}
