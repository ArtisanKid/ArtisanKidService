package com.artisankid.elementwar.tcpconnection.gate.handler;

import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created by shaohua.wangshaohu on 2017/4/18.
 */
public class GatewayServerHandler extends ChannelInboundHandlerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(GatewayServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
        ctx.writeAndFlush(returnDealNotice);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }
}
