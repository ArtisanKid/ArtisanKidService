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
    public static ChannelHandlerContext getContext(String userID) {
        return userContextMap.get(userID);
    }

    /**
     * 获取指定Socket上下文的用户
     * @param ctx
     * @return 用户
     */
    public static User getUser(ChannelHandlerContext ctx) {
        String userID = contextUserMap.get(ctx);
        if(userID == null) {
            return null;
        }
        return UserManager.getUser(userID);
    }

    /**
     * 移除指定用户关系
     * @param userID
     */
    public static void removeContext(String userID) {
        contextUserMap.remove(userContextMap.get(userID));
        userContextMap.remove(userID);
    }
}
