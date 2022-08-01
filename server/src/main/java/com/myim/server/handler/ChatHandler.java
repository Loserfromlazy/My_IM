package com.myim.server.handler;

import com.myim.common.concurrent.CallableTask;
import com.myim.common.concurrent.GuavaTaskScheduler;
import com.myim.common.exception.InvalidException;
import com.myim.common.pojo.ProtoMsgOuterClass;
import com.myim.server.process.ChatProcessor;
import com.myim.server.session.ServerSession;
import com.myim.server.session.ServerSessionMap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * ChatHandler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class ChatHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    ChatProcessor chatProcessor;

    static final AttributeKey<ServerSession> SESSION = AttributeKey.valueOf("SESSION");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtoMsgOuterClass.ProtoMsg.Message message = (ProtoMsgOuterClass.ProtoMsg.Message) msg;

        if (message.getType() != ProtoMsgOuterClass.ProtoMsg.MessageType.MESSAGE_REQUEST){
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsgOuterClass.ProtoMsg.MessageRequest messageRequest = message.getMessageRequest();
        ServerSession serverSession = ctx.channel().attr(SESSION).get();
        if (serverSession==null|| !serverSession.getIsLogin()){
            log.error("发送消息失败，用户{}未登录",messageRequest.getFrom());
            return;
        }

        GuavaTaskScheduler.addTask(new CallableTask<Boolean>() {
            @Override
            public Boolean run() {
                return chatProcessor.action(serverSession,message);
            }
            @Override
            public void onSuccess(Boolean o) {
                if (o){
                    log.info("消息发送成功");
                }else {
                    log.info("消息发送失败");
                }

            }
            @Override
            public void onError(Throwable t) {
                log.info("消息发送失败，失败原因{}",t.getMessage());
            }
        });

    }
}
