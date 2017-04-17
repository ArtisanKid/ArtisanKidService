package com.artisankid.elementwar.tcpconnection.gate.handler;

import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnectionMap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;


/**
 * Created by shaohua.wang on 2017/04/16.
 */
public class GateServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GateServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        DealNoticeOuterClass.DealNotice dealNotice = (DealNoticeOuterClass.DealNotice) msg;
        String messageId = dealNotice.getMessageId();
        logger.error("receive msg:" + messageId);

        DealNoticeOuterClass.DealNotice.Builder returnDealNotice= DealNoticeOuterClass.DealNotice.newBuilder();
        returnDealNotice.setMessageId("0000_000002");

        Calendar nowDate=Calendar.getInstance();
        double sendTime = nowDate.getTimeInMillis() / 1000;
        returnDealNotice.setSendTime(sendTime);

        nowDate.add(Calendar.SECOND,3000);
        returnDealNotice.setExpiredTime(nowDate.getTimeInMillis()/1000);
        returnDealNotice.setNeedResponse(Boolean.TRUE);
        returnDealNotice.setCalibrationTime(nowDate.getTimeInMillis()/1000);
        ctx.write(returnDealNotice);
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
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
}
