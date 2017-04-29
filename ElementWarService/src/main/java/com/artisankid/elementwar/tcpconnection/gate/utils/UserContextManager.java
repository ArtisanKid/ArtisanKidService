package com.artisankid.elementwar.tcpconnection.gate.utils;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiXiangYu on 2017/4/27.
 */
public class UserContextManager {
    private static ConcurrentHashMap<String, ChannelHandlerContext> userContextMap = new ConcurrentHashMap<>();

    /**
     * 添加客户端连接
     *
     * @param userID
     * @param ctx
     * @return
     */
    public static void setUserContext(String userID, ChannelHandlerContext ctx) {
        userContextMap.put(userID, ctx);
    }

    /**
     * 获取指定用户的Socket上下文
     * @param userID
     * @return
     */
    public static ChannelHandlerContext getUserContext(String userID) {
        return userContextMap.get(userID);
    }

    public static void removeUserContext(String userID) {
        userContextMap.remove(userID);
    }
}
