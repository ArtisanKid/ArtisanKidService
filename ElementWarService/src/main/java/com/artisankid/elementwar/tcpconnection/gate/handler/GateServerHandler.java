package com.artisankid.elementwar.tcpconnection.gate.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Dell on 2016/2/1.
 */
public class GateServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GateServerHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.error("----------------------channelInactive");
    }

    /*
     * 覆盖了 channelRead0() 事件处理方法。
     * 每当从服务端读到客户端写入信息时，
     * 其中如果你使用的是 Netty 5.x 版本时，
     * 需要把 channelRead0() 重命名为messageReceived()
     */

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        logger.error("----------------------channelRead0");

    }

    /*
             * 覆盖channelActive 方法在channel被启用的时候触发（在建立连接的时候）
             * 覆盖了 channelActive() 事件处理方法。服务端监听到客户端活动
             */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.error("----------------------channelActive");
        super.channelActive(ctx);
    }

    /*
     * (non-Javadoc)
     * 覆盖了 handlerAdded() 事件处理方法。
     * 每当从服务端收到新的客户端连接时
     */
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        logger.error("----------------------handlerAdded");
        super.handlerAdded(ctx);
    }

    /*
     * (non-Javadoc)
     * .覆盖了 handlerRemoved() 事件处理方法。
     * 每当从服务端收到客户端断开时
     */
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.handlerRemoved(ctx);
    }

    /*
     * exceptionCaught() 事件处理方法是当出现 Throwable 对象才会被调用，
     * 即当 Netty 由于 IO 错误或者处理器在处理事件时抛出的异常时。
     * 在大部分情况下，捕获的异常应该被记录下来并且把关联的 channel 给关闭掉。
     * 然而这个方法的处理方式会在遇到不同异常的情况下有不同的实现，
     * 比如你可能想在关闭连接之前发送一个错误码的响应消息。
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        logger.error("----------------------exceptionCaught");
        super.exceptionCaught(ctx, cause);
    }
}
