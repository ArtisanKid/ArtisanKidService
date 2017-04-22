package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * 消息处理机制
 * Created by ws on 2017/4/22.
 */
@NettyAction
public class Notice {

    private Logger logger = LoggerFactory.getLogger(Notice.class);
    /**
     * 处理消息逻辑
     *
     * @param o
     * @return
     */
    @ActionRequestMap(actionKey = ContainerOuterClass.Container.DEAL_NOTICE_FIELD_NUMBER)
    public void dealNotice(ChannelHandlerContext ctx, Object o) {
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) o;
        DealNoticeOuterClass.DealNotice dealNotice = container.getDealNotice();

        String messageId = dealNotice.getMessageId();
        logger.error("receive msg:" + messageId);


        ContainerOuterClass.Container.Builder returnContainer = ContainerOuterClass.Container.newBuilder();
        DealNoticeOuterClass.DealNotice.Builder returnDealNotice = DealNoticeOuterClass.DealNotice.newBuilder();
        returnDealNotice.setMessageId("0000_000002");

        Calendar nowDate = Calendar.getInstance();
        double sendTime = nowDate.getTimeInMillis() / 1000;
        returnDealNotice.setSendTime(sendTime);

        nowDate.add(Calendar.SECOND, 3000);
        returnDealNotice.setExpiredTime(nowDate.getTimeInMillis() / 1000);
        returnDealNotice.setNeedResponse(Boolean.TRUE);
        returnDealNotice.setCalibrationTime(nowDate.getTimeInMillis() / 1000);
        ctx.writeAndFlush(returnContainer.setDealNotice(returnDealNotice));
    }
}
