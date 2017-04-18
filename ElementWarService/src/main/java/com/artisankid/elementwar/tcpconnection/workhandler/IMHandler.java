package com.artisankid.elementwar.tcpconnection.workhandler;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

/**
 */
public abstract class IMHandler {
    protected final String _userid;
    protected final long  _netid;
    protected final Message _msg;
    protected ChannelHandlerContext _ctx;

    protected IMHandler(String userid, long netid, Message msg, ChannelHandlerContext ctx) {
        _userid = userid;
        _netid = netid;
        _msg = msg;
        _ctx = ctx;
    }

    protected abstract void excute(Worker worker) throws Exception;
}
