package com.myim.client.handler;

import com.myim.client.session.ClientSession;
import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * LoginResponseHandler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/28
 */
@Slf4j
@Service
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {


    @Autowired
    MessageHandler messageHandler;

    @Autowired
    HeartBeatHandler heartBeatHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ProtoMsgOuterClass.ProtoMsg.Message message = (ProtoMsgOuterClass.ProtoMsg.Message) msg;
        ProtoMsgOuterClass.ProtoMsg.MessageType type = message.getType();

        if (ProtoMsgOuterClass.ProtoMsg.MessageType.LOGIN_RESPONSE != type) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsgOuterClass.ProtoMsg.LoginResponse loginResponse = message.getLoginResponse();
        if (loginResponse.getResult()) {
            log.info("登录成功");
            ClientSession.loginSuccess(ctx.channel(), message.getSessionId());
            //增加心跳处理等处理器，去除登录响应处理器
            ctx.channel().pipeline().addAfter("loginResponse", "heartBeat", heartBeatHandler);
            ctx.channel().pipeline().addAfter("loginResponse", "messageHandler", messageHandler);
            ctx.channel().pipeline().remove("loginResponse");
        } else {
            log.error("登录失败,错误码{}，错误信息{}", loginResponse.getCode(), loginResponse.getMsg());
        }
    }
}
