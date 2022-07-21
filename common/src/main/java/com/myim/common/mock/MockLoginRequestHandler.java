package com.myim.common.mock;

import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>
 * MockLoginRequestHandler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/21
 */
public class MockLoginRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtoMsgOuterClass.ProtoMsg.Message message = (ProtoMsgOuterClass.ProtoMsg.Message) msg;
        final ProtoMsgOuterClass.ProtoMsg.LoginRequest loginRequest = message.getLoginRequest();
        final String uid = loginRequest.getUid();
        final String deviceId = loginRequest.getDeviceId();
        final String token = loginRequest.getToken();
        final int deviceType = loginRequest.getDeviceType();
        final ProtoMsgOuterClass.ProtoMsg.MessageType type = message.getType();
        System.out.println("type="+type);
        System.out.println("uid=" + uid);
        System.out.println("deviceId=" + deviceId);
        System.out.println("token=" + token);
        System.out.println("deviceType=" + deviceType);
        super.channelRead(ctx, msg);
    }
}
