package com.artisankid.elementwar.tcpconnection.gate.utils;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by LiXiangYu on 2017/4/27.
 */
public class UserContextManager {
    private static ConcurrentHashMap<String, ChannelHandlerContext> userContextMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<ChannelHandlerContext, String> contextUserMap = new ConcurrentHashMap<>();

    /**
     * 添加客户端连接
     *
     * @param userID
     * @param ctx
     * @return
     */
    public static void setUserContext(String userID, ChannelHandlerContext ctx) {
        userContextMap.put(userID, ctx);
        contextUserMap.put(ctx, userID);
    }

    /**
     * 获取指定用户的Socket上下文
     * @param userID
     * @return Socket上下文
     */
    public static ChannelHandlerContext getUserContext(String userID) {
        return userContextMap.get(userID);
    }

    public static User getUser(ChannelHandlerContext ctx) {
        String userID = contextUserMap.get(ctx);
        if(userID == null) {
            return null;
        }
        return UserManager.getUser(userID);
    }

    public static void removeContext(String userID) {
        contextUserMap.remove(userContextMap.get(userID));
        userContextMap.remove(userID);
    }
}
