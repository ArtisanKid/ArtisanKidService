package com.artisankid.elementwar.tcpconnection.gate.handler;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnectionMap;
import com.artisankid.elementwar.tcpconnection.workhandler.HandlerManager;
import com.artisankid.elementwar.tcpconnection.workhandler.IMHandler;
import com.artisankid.elementwar.tcpconnection.workhandler.Worker;
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
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) msg;


//        IMHandler handler;
//
//        String  userId = null;
//        handler = HandlerManager.getHandler(msg, ctx);
//
//        Worker.dispatch(userId, handler);



        DealNoticeOuterClass.DealNotice dealNotice = container.getDealNotice();

        String messageId = dealNotice.getMessageId();
        logger.error("receive msg:" + messageId);


        ContainerOuterClass.Container.Builder returnContainer = ContainerOuterClass.Container.newBuilder();
        DealNoticeOuterClass.DealNotice.Builder returnDealNotice= DealNoticeOuterClass.DealNotice.newBuilder();
        returnDealNotice.setMessageId("0000_000002");

        Calendar nowDate=Calendar.getInstance();
        double sendTime = nowDate.getTimeInMillis() / 1000;
        returnDealNotice.setSendTime(sendTime);

        nowDate.add(Calendar.SECOND,3000);
        returnDealNotice.setExpiredTime(nowDate.getTimeInMillis()/1000);
        returnDealNotice.setNeedResponse(Boolean.TRUE);
        returnDealNotice.setCalibrationTime(nowDate.getTimeInMillis()/1000);
        ctx.writeAndFlush(returnContainer.setDealNotice(returnDealNotice));
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
