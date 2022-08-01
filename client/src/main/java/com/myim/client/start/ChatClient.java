package com.myim.client.start;

import com.myim.client.handler.ExceptionHandler;
import com.myim.client.handler.LoginResponseHandler;
import com.myim.common.codec.SimpleProtobufDecoder;
import com.myim.common.codec.SimpleProtobufEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * <p>
 * ChatClient
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
@Component
@Slf4j
@Data
public class ChatClient {
    private Bootstrap bootstrap;
    private NioEventLoopGroup group;
    private ChannelFutureListener channelFutureListener;

    @Autowired
    LoginResponseHandler loginResponseHandler;

    @Autowired
    ExceptionHandler exceptionHandler;

    @Value("${server.address}")
    private String address;
    @Value("${server.port}")
    private Integer port;

    public void connect(){
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .remoteAddress(address,port)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.ALLOCATOR,PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleProtobufDecoder());
                        ch.pipeline().addLast(new SimpleProtobufEncoder());
                        ch.pipeline().addLast("loginResponse",loginResponseHandler);
                        ch.pipeline().addLast(exceptionHandler);
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect();
        channelFuture.addListener(channelFutureListener);
    }
}
