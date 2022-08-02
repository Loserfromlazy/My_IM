package com.myim.client.handler;

import com.myim.client.converter.HeartBeatConverter;
import com.myim.client.session.ClientSession;
import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private long HEARTBEAT_TIME = 50L;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtoMsgOuterClass.ProtoMsg.Message message = (ProtoMsgOuterClass.ProtoMsg.Message) msg;
        if (message.getType() != ProtoMsgOuterClass.ProtoMsg.MessageType.HEARTBEAT) {
            super.channelRead(ctx, msg);
        } else {
            log.info("收到心跳回复");
            super.channelRead(ctx, msg);
        }

    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ClientSession clientSession = ClientSession.getClientSession(ctx);
        HeartBeatConverter converter = new HeartBeatConverter(ProtoMsgOuterClass.ProtoMsg.MessageType.HEARTBEAT, clientSession, clientSession.getUser());
        final ProtoMsgOuterClass.ProtoMsg.Message message = converter.getHeartBeatMsg();
        heartBeat(ctx, message);
    }

    public void heartBeat(ChannelHandlerContext ctx, ProtoMsgOuterClass.ProtoMsg.Message message) {
        final EventExecutor executor = ctx.executor();
        executor.schedule(() -> {
            if (ctx.channel().isActive()) {
                log.info("发送消息给服务端");
                ctx.pipeline().writeAndFlush(message);
                heartBeat(ctx, message);
            }
        }, HEARTBEAT_TIME, TimeUnit.SECONDS);
    }
}
