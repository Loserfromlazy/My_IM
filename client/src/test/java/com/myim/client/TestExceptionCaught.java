package com.myim.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * TestExecptionCaught
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/28
 */
@SpringBootTest
@Slf4j
public class TestExceptionCaught {

    static class MockInboundExceptionHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            throw new Exception("throw error!!!");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }

    static class MockOutboundExceptionHandler extends MessageToByteEncoder<String> {
        @Override
        protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {
            throw new Exception("throw error!!!");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }
    }

    static class MyExceptionHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.error(cause.getMessage());
            ctx.close();
        }
    }

    static class SendHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            //这两种是从尾部开始查找出站处理器
            ChannelFuture channelFuture = ctx.channel().writeAndFlush("something");
            //ctx.pipeline().writeAndFlush("something");
            //这一种是从当前节点查找上一个出站处理器
            //ctx.writeAndFlush("something");
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("执行成功");
                    } else if (future.isCancelled()) {
                        log.info("执行失败");
                    } else if (future.cause() != null) {
                        log.info("执行出错，错误信息{}", future.cause().getMessage());
                    }
                }
            });

        }
    }

    @Test
    public void testInBoundExceptionCaught() {
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new MockInboundExceptionHandler());
                channel.pipeline().addLast(new MyExceptionHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
        channel.writeInbound("something");
        channel.flush();
    }

    @Test
    public void testOutBoundExceptionCaught() {
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast(new SendHandler());
                channel.pipeline().addLast(new MockOutboundExceptionHandler());
                channel.pipeline().addLast(new MyExceptionHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(channelInitializer);
    }
}
