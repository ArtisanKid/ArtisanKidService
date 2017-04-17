package com.artisankid.elementwar.tcpconnection.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ClientHandler());
                    }
                });

        // Start the client.
            startConnection(b, 0);
    }

    /**
     *
     * @param b
     * @param index
     */
    private static void startConnection(Bootstrap b, final int index) {
        b.connect("localhost", 51681)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future)
                            throws Exception {
                        if (future.isSuccess()) {
                            //init registry
                            logger.info("Client[{}] connected Gate Successed...", index);
                        } else {
                            logger.error("Client[{}] connected Gate Failed", index);
                        }
                    }
                });
    }
}
