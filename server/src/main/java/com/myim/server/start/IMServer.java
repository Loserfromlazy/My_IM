package com.myim.server.start;

import com.myim.common.codec.SimpleProtobufDecoder;
import com.myim.common.codec.SimpleProtobufEncoder;
import com.myim.server.handler.LoginRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * <p>
 * ServerStart
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/25
 */
@Service("IMServer")
@Slf4j
public class IMServer {

    @Autowired
    LoginRequestHandler loginRequestHandler;

    @Value("${server.port}")
    private Integer port;

    private ServerBootstrap bootstrap = new ServerBootstrap();
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public void start() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        try {
            bootstrap
                    .group(bossGroup, workerGroup)
                    .localAddress(new InetSocketAddress(port))
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new SimpleProtobufDecoder());
                            socketChannel.pipeline().addLast(new SimpleProtobufEncoder());
                            socketChannel.pipeline().addLast("login",loginRequestHandler);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind();
            channelFuture.sync();
            log.info("服务启动，正在监听端口{}", port);
            ChannelFuture closeFuture = channelFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
