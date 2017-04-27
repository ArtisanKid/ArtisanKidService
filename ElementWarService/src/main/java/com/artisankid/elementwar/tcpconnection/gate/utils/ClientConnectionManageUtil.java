package com.artisankid.elementwar.tcpconnection.gate.utils;

import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端管理utils
 * <p>
 * Created by ws on 2017/4/27.
 */
public class ClientConnectionManageUtil {

    /**
     * 添加客户端连接
     *
     * @param ctx
     * @param userId
     * @return
     */
    public static Boolean addClientConnection(ChannelHandlerContext ctx, String userId) {
        ClientConnectionMap.addClientConnection(ctx);
        ClientConnection conn = ClientConnectionMap.getClientConnection(ctx);
        ClientConnectionMap.registerUserId(userId, conn.getNetId());
        return Boolean.TRUE;
    }

    /**
     * 根据userId获取客户端连接
     *
     * @param userId
     * @return
     */
    public static ChannelHandlerContext getClientConnection(String userId) {
        Long netId = ClientConnectionMap.userid2netid(userId);
        ClientConnection clientConnection = ClientConnectionMap.getClientConnection(netId);
        ChannelHandlerContext returnCtx = clientConnection.getChannelHandlerContext();
        return returnCtx;
    }

}
