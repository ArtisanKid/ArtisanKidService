package com.artisankid.elementwar.tcpconnection.action.impl;

import com.artisankid.elementwar.common.dao.TokenDao;
import com.artisankid.elementwar.common.ewmodel.Token;
import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass.Container;
import com.artisankid.elementwar.ewmessagemodel.LoginMessageOuterClass.LoginMessage;
import com.artisankid.elementwar.ewmessagemodel.LoginNoticeOuterClass.LoginNotice;
import com.artisankid.elementwar.tcpconnection.annotations.ActionRequestMap;
import com.artisankid.elementwar.tcpconnection.annotations.NettyAction;
import com.artisankid.elementwar.tcpconnection.client.Client;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnection;
import com.artisankid.elementwar.tcpconnection.gate.utils.ClientConnectionMap;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by LiXiangYu on 2017/4/25.
 */
@NettyAction
public class Login {
    private Logger logger = LoggerFactory.getLogger(Notice.class);
    /**
     * 处理消息逻辑
     *
     * @param o
     * @return
     */
    @ActionRequestMap(actionKey = Container.MATCH_MESSAGE_FIELD_NUMBER)
    public void loginMessage(ChannelHandlerContext ctx, Object o) {
        Container container = (Container) o;
        LoginMessage message = container.getLoginMessage();
        String messageID = message.getMessageId();
        String userID = message.getMessageId();
        String accessToken = message.getAccessToken();
        logger.debug("loginMessage:" + messageID + " userID:" + userID + " accessToken:" + accessToken);

        TokenDao dao = new TokenDao();
        if(dao.selectByAccessToken(accessToken) != null) {
            //TODO:将userID和ctx进行绑定
            ClientConnectionMap.addClientConnection(ctx);
            ClientConnection conn = ClientConnectionMap.getClientConnection(ctx);
            ClientConnectionMap.registerUserId(userID, conn.getNetId());


            //TODO 获取逻辑根据用户Id获取对应的连接从而获取上下文
            Long netId = ClientConnectionMap.userid2netid(userID);
            ClientConnection clientConnection = ClientConnectionMap.getClientConnection(netId);
            ChannelHandlerContext returnCtx = clientConnection.getChannelHandlerContext();

            LoginNotice.Builder notice = LoginNotice.newBuilder();
            notice.setMessageId(messageID);
            long now = System.currentTimeMillis();
            notice.setSendTime(now / 1000);
            notice.setExpiredTime(now / 1000 + 200);
            notice.setNeedResponse(Boolean.FALSE);
            notice.setUserId(userID);

            Container.Builder noticeContainer = Container.newBuilder();
            noticeContainer.setLoginNotice(notice);
            ctx.writeAndFlush(noticeContainer);
        } else {
            //此处等待客户端自然超时
        }
    }
}
