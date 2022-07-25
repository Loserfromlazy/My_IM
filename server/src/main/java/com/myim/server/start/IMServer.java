package com.myim.server.start;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Component;

/**
 * <p>
 * ServerStart
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/25
 */
@Component("IMServer")
public class IMServer {


    private ServerBootstrap bootstrap;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    public void start(){
        bootstrap.channel(NioServerSocketChannel.class);
    }

}
