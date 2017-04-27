package com.artisankid.elementwar.tcpconnection.gate.utils;

import com.sun.tools.javac.util.List;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Dell on 2016/2/2.
 */
public class ClientConnectionMap {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnectionMap.class);

    //保存一个gateway上所有的客户端连接
    public static ConcurrentHashMap<Long, ClientConnection> allClientMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, Long> userid2netidMap = new ConcurrentHashMap<>();

    public static ClientConnection getClientConnection(ChannelHandlerContext ctx) {
        Long netId = ctx.attr(ClientConnection.NETID).get();

        ClientConnection conn = allClientMap.get(netId);
        if(conn != null)
            return conn;
        else {
            logger.error("ClientConenction not found in allClientMap, netId: {}", netId);
        }
        return null;
    }

    public static ClientConnection getClientConnection(long netId) {
        ClientConnection conn = allClientMap.get(netId);
        if(conn != null)
            return conn;
        else {
            logger.error("ClientConenction not found in allClientMap, netId: {}", netId);
        }
        return null;
    }

    /**
     * 客户端连接加入map
     * 
     * @param c
     */
    public static void addClientConnection(ChannelHandlerContext c) {
        ClientConnection conn = new ClientConnection(c);
        if (ClientConnectionMap.allClientMap.putIfAbsent(conn.getNetId(), conn) != null) {
            logger.error("ClientConnection has existed,not need putIfAbsent!");
        }
    }

    public static void removeClientConnection(ChannelHandlerContext c) {
        ClientConnection conn = getClientConnection(c);
        long netid = conn.getNetId();
        String userId = conn.getUserId();
        if(ClientConnectionMap.allClientMap.remove(netid) != null) {
            unRegisterUserId(userId);
        } else {
            logger.error("NetId: {} is not exist in allClientMap", netid);
        }

        logger.info("Client disconnected, netid: {}, userId: {}", netid, userId);
    }

    public static void registerUserId(String userId, long netId) {
        if(userid2netidMap.putIfAbsent(userId, netId) == null) {
            ClientConnection conn = ClientConnectionMap.getClientConnection(netId);
            if(conn != null) {
                conn.setUserId(userId);
            } else {
                logger.error("ClientConnection is null");
                return;
            }
        } else {
            logger.error("userId: {} has registered in userid2netidMap", userId);
        }
    }

    protected static void unRegisterUserId(String userId) {
        if(ClientConnectionMap.userid2netidMap.remove(userId) == null) {
            logger.error("UserId: {} is not exist in userid2netidMap", userId);
        }
    }

    public static Long userid2netid(String userId) {
        Long netid = userid2netidMap.get(userId);
        if(netid != null)
            return netid;
        else {
            logger.error("User not login, userId: {}",userId);
        }
        return null;
    }
}
