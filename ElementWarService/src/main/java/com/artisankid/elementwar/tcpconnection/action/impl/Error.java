package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.ewmessagemodel.ErrorNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.utils.RoomManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by LiXiangYu on 2017/6/19.
 */
public class Error {
    private static Logger logger = LoggerFactory.getLogger(Error.class);

    static public void ErrorNotice(final String receiverID, final String messageID, int code, String message) {
        logger.debug("ErrorNotice" + " receiverID:" + receiverID + " messageID:" + messageID + " 开始发送...");
        logger.error(message);

        ErrorNoticeOuterClass.ErrorNotice.Builder notice = ErrorNoticeOuterClass.ErrorNotice.newBuilder();
        notice.setMessageId(messageID);
        Long now = System.currentTimeMillis();
        Long expiredTime = now + 60 * 60 * 1000L;
        notice.setSendTime(now / 1000.);
        notice.setExpiredTime(expiredTime / 1000.);
        notice.setCode(code);
        notice.setMessage(message);

        ContainerOuterClass.Container.Builder container = ContainerOuterClass.Container.newBuilder();
        container.setMessageType(ContainerOuterClass.Container.MessageType.ErrorNotice);
        container.setErrorNotice(notice);

        ChannelHandlerContext ctx = UserContextManager.getContext(receiverID);
        ctx.writeAndFlush(container).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("ErrorNotice" + " receiverID:" + receiverID + " messageID:" + messageID + " 发送成功");
            }
        });
    }
}
