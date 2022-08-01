package com.myim.server.handler;

import com.myim.common.concurrent.TaskScheduler;
import com.myim.common.pojo.ProtoMsgOuterClass;
import com.myim.server.session.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@ChannelHandler.Sharable
public class HeartBeatServerHandler extends IdleStateHandler {

    /**
     * 最大空闲时长
     */
    private static final int READER_IDLE_MAX = 150;

    /**
     * 调用父类构造函数
     *
     * @param readerIdleTime 入站空闲时长，一定时间内没有数据入站就判定连接假死
     * @param writerIdleTime 出站空闲时长，一定时间内如果没有数据出战，就判定连接假死
     * @param allIdleTime    出/入站检测时长，一段时间内如果没有入站或出站，就判定连接假死
     * @param unit           时间单位
     */
    public HeartBeatServerHandler(long readerIdleTime, long writerIdleTime, long allIdleTime, TimeUnit unit) {
        super(readerIdleTime, writerIdleTime, allIdleTime, unit);
    }

    public HeartBeatServerHandler() {
        super(READER_IDLE_MAX, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info(READER_IDLE_MAX + "秒内未读到数据，关闭连接");
        ServerSession.closeSession(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtoMsgOuterClass.ProtoMsg.Message message = (ProtoMsgOuterClass.ProtoMsg.Message) msg;
        if (ProtoMsgOuterClass.ProtoMsg.MessageType.HEARTBEAT != message.getType()) {
            super.channelRead(ctx, msg);
            return;
        }
        //直接将心跳包会给客户端
        TaskScheduler.addTask(() -> ctx.pipeline().channel().writeAndFlush(msg));
    }
}
