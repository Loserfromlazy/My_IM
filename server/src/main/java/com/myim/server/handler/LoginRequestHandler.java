package com.myim.server.handler;

import com.myim.common.concurrent.CallableTask;
import com.myim.common.concurrent.GuavaTaskScheduler;
import com.myim.common.pojo.ProtoMsgOuterClass;
import com.myim.server.process.LoginCheckProcessor;
import com.myim.server.session.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * LoginRequestHandler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    LoginCheckProcessor loginCheckProcessor;

    @Autowired
    HeartBeatServerHandler heartBeatServerHandler;

    @Autowired
    ChatHandler chatHandler;

    static final AttributeKey<ServerSession> SESSION = AttributeKey.valueOf("SESSION");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ProtoMsgOuterClass.ProtoMsg.Message message = (ProtoMsgOuterClass.ProtoMsg.Message) msg;
        if (message.getType() != ProtoMsgOuterClass.ProtoMsg.MessageType.LOGIN_REQUEST){
            super.channelRead(ctx, msg);
            return;
        }
        GuavaTaskScheduler.addTask(new CallableTask<Boolean>() {
            @Override
            public Boolean run() {
                ServerSession serverSession = new ServerSession(ctx.channel());
                return loginCheckProcessor.action(serverSession,message);
            }

            @Override
            public void onSuccess(Boolean b) {
                if (b){
                    //添加心跳等处理器，删除登录处理器，进行流水线处理器的热插拔
                    ctx.pipeline().addAfter("login","heartBeat",heartBeatServerHandler);
                    ctx.pipeline().addAfter("login","chat",chatHandler);
                    ctx.pipeline().remove("login");
                    log.info("登录成功");
                }else {
                    ServerSession.closeSession(ctx);
                    log.info("登录失败");
                }
            }

            @Override
            public void onError(Throwable t) {
                ServerSession.closeSession(ctx);
                log.error("登录发生错误，错误信息：{}",t.getMessage());
            }
        });

    }
}
