package com.artisankid.elementwar.tcpconnection.gate;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.artisankid.elementwar.tcpconnection.protobuf.code.PacketDecoder;
import com.artisankid.elementwar.tcpconnection.protobuf.code.PacketEncoder;

/**
 * Created by Dell on 2016/2/2.
 */
public class GateLogicConnection {
    private static final Logger logger = LoggerFactory.getLogger(GateLogicConnection.class);

    public static void startGateLogicConnection(String ip, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel)
                            throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast("MessageDecoder", new PacketDecoder());
                        pipeline.addLast("MessageEncoder", new PacketEncoder());

                    }
                });

        bootstrap.connect(ip, port);

    }
}
