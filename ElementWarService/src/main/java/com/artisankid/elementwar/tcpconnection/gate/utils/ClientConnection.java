package com.artisankid.elementwar.tcpconnection.gate.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 客户端连接的封装类
 *
 * @since 2017-04-18
 */

public class ClientConnection {

    /**
     * 进客户端ID
     */
    private static final AtomicLong netidGenerator = new AtomicLong(0);

    ClientConnection(ChannelHandlerContext c) {
        _netId = netidGenerator.incrementAndGet();
        _ctx = c;
        _ctx.attr(ClientConnection.NETID).set(_netId);
    }

    /**
     * 客户端连接ID KEY
     */
    public static AttributeKey<Long> NETID = AttributeKey.valueOf("netid");

    private String _userId;
    private long _netId;
    private ChannelHandlerContext _ctx;

    public long getNetId() {
        return _netId;
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        _userId = userId;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return _ctx;
    }

    public void setChannelHandlerContext(ChannelHandlerContext ctx) {
        _ctx = ctx;
    }
}
