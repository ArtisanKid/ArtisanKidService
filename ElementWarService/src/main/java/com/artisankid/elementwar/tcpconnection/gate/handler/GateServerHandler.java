package com.artisankid.elementwar.tcpconnection.gate.handler;

import com.artisankid.elementwar.ewmessagemodel.ContainerOuterClass;
import com.artisankid.elementwar.tcpconnection.action.ActionMapUtil;
import com.artisankid.elementwar.tcpconnection.gate.utils.User;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserContextManager;
import com.artisankid.elementwar.tcpconnection.gate.utils.UserManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 网关服务处理器，请求转发
 * <p>
 * Created by shaohua.wang on 2017/04/16.
 */
@ChannelHandler.Sharable
public class GateServerHandler extends SimpleChannelInboundHandler {
    private static final Logger logger = LoggerFactory.getLogger(GateServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        ContainerOuterClass.Container container = (ContainerOuterClass.Container) msg;
        int messageType = container.getMessageCase().getNumber();
        ActionMapUtil.invote(messageType, ctx, container);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("Connection In...");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.debug("Connection Out...");

        User user = UserContextManager.getUser(ctx);
        if(user == null) {
            return;
        }

        user.setConnectState(User.ConnectState.Disconnected);
        UserContextManager.removeContext(user.getUserID());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }
}
