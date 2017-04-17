package com.artisankid.elementwar.tcpconnection.gate;

import com.alibaba.fastjson.JSON;
import com.artisankid.elementwar.ewmessagemodel.DealNoticeOuterClass;
import com.artisankid.elementwar.tcpconnection.gate.handler.GateServerHandler;
import com.artisankid.elementwar.tcpconnection.protobuf.code.PacketDecoder;
import com.artisankid.elementwar.tcpconnection.protobuf.code.PacketEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 网关消费
 *
 * @author shaohua.wang
 * @since 20170416
 */
@Component("gateServer")
public class GateServer {
    private static final Logger logger = LoggerFactory.getLogger(GateServer.class);

    private final static int CONNECTION_PORT = 51681;

    public static void main(String[] args){
        init();
    }

    /**
     * 初始化方法
     */
    public static void init() {
        startGateServer(CONNECTION_PORT);
    }

    /**
     * 启动网关服务
     *
     * @param port
     */
    public static void startGateServer(int port) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel)
                            throws Exception {

                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new ProtobufDecoder(DealNoticeOuterClass.DealNotice.getDefaultInstance()));
                        pipeline.addLast(new ProtobufEncoder());
                        pipeline.addLast(new GateServerHandler());
                    }
                });

        bindConnectionOptions(bootstrap);

        bootstrap.bind(new InetSocketAddress(port)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future)
                    throws Exception {
                if (future.isSuccess()) {
                    logger.info("[GateServer] Started Successed, registry is complete, waiting for client connect...");
                } else {
                    logger.error("[GateServer] Started Failed, registry is incomplete");
                }
            }
        });
    }

    protected static void bindConnectionOptions(ServerBootstrap bootstrap) {

        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.childOption(ChannelOption.SO_LINGER, 0);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        bootstrap.childOption(ChannelOption.SO_REUSEADDR, true); //调试用
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //心跳机制暂时使用TCP选项，之后再自己实现

    }
}
