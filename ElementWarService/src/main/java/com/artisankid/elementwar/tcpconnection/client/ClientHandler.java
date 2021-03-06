package com.artisankid.elementwar.tcpconnection.client;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.ewmessagemodel.MatchMessageOuterClass;
import com.artisankid.elementwar.tcpconnection.action.impl.Match;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;


/**
 * Created by Dell on 2016/2/15.
 * 模拟客户端聊天：自己给自己发消息
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object>  {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String message="ssss";
        DealNoticeOuterClass.DealNotice.Builder dealNotice=  DealNoticeOuterClass.DealNotice.newBuilder();
        dealNotice.setMessageId(message);

        Calendar nowDate=Calendar.getInstance();
        double sendTime = nowDate.getTimeInMillis() / 1000;
        dealNotice.setSendTime(sendTime);

        nowDate.add(Calendar.SECOND,3000);
        dealNotice.setExpiredTime(nowDate.getTimeInMillis() / 1000);

        ContainerOuterClass.Container.Builder containerBuilder = ContainerOuterClass.Container.newBuilder();
        containerBuilder.setDealNotice(dealNotice);
        ctx.writeAndFlush(containerBuilder);

        Thread.sleep(1000);
        ctx.writeAndFlush(containerBuilder);

        ContainerOuterClass.Container.Builder matchContainer = ContainerOuterClass.Container.newBuilder();
        MatchMessageOuterClass.MatchMessage.Builder matchMessage= MatchMessageOuterClass.MatchMessage.newBuilder();
        matchMessage.setMessageId("MatchMessageOuterClass.MatchMessage.Builder");

        matchContainer.setMatchMessage(matchMessage);
        ctx.writeAndFlush(matchContainer);

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) msg;
        DealNoticeOuterClass.DealNotice dealNotice = container.getDealNotice();

        String messageId = dealNotice.getMessageId();
        logger.error("receive msg:" + messageId);

        DealNoticeOuterClass.DealNotice.Builder returnDealNotice=  DealNoticeOuterClass.DealNotice.newBuilder();
        returnDealNotice.setMessageId("0000_000001_00000000000000000000000000000000000");

        Calendar nowDate=Calendar.getInstance();
        double sendTime = nowDate.getTimeInMillis() / 1000;
        returnDealNotice.setSendTime(sendTime);

        nowDate.add(Calendar.SECOND,3000);
        returnDealNotice.setExpiredTime(nowDate.getTimeInMillis() / 1000);

        ContainerOuterClass.Container.Builder containerBuilder = ContainerOuterClass.Container.newBuilder();
        containerBuilder.setDealNotice(returnDealNotice);
        ctx.writeAndFlush(containerBuilder);


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught-------------------,cause"+cause.getMessage());
    }
}
