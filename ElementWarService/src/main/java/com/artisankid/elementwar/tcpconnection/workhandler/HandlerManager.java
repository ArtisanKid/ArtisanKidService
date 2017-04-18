package com.artisankid.elementwar.tcpconnection.workhandler;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;


public class HandlerManager {

    private static final Map<Integer, IMHandler> _handlers = new HashMap<>();

    public static IMHandler getHandler(Message msg,
                                       ChannelHandlerContext ctx) throws Exception {
        IMHandler handler = _handlers.get(msg);
        return handler;
    }
}
