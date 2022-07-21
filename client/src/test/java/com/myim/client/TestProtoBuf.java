package com.myim.client;

import com.myim.client.entity.User;
import com.myim.common.codec.SimpleProtobufDecoder;
import com.myim.common.codec.SimpleProtobufEncoder;
import com.myim.common.mock.MockLoginRequestHandler;
import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestProtoBuf {

    @Test
    public void testProtoBuf(){
        //构建流水线
        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline().addLast(new SimpleProtobufDecoder());
                channel.pipeline().addLast(new MockLoginRequestHandler());
            }
        };
        //构建模拟通道
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(channelInitializer);
        //构建ProtoBuf消息
        ProtoMsgOuterClass.ProtoMsg.Message message = buildMessage(new User());
        ByteBuf buffer = Unpooled.buffer();
        //手动编码
        SimpleProtobufEncoder.encode0(message,buffer);
        //写入通道，进行入站模拟
        embeddedChannel.writeInbound(buffer);
        embeddedChannel.flush();
        //等待消息处理完成
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private ProtoMsgOuterClass.ProtoMsg.Message buildMessage(User user){
        final ProtoMsgOuterClass.ProtoMsg.Message.Builder builder = ProtoMsgOuterClass.ProtoMsg.Message.newBuilder();
        builder.setType(ProtoMsgOuterClass.ProtoMsg.MessageType.LOGIN_REQUEST);
        final ProtoMsgOuterClass.ProtoMsg.LoginRequest.Builder loginRequestBuilder = ProtoMsgOuterClass.ProtoMsg.LoginRequest.newBuilder();
        loginRequestBuilder.setUid(user.getUid());
        loginRequestBuilder.setDeviceId(user.getDeviceId());
        loginRequestBuilder.setToken(user.getToken());
        loginRequestBuilder.setDeviceType(user.getDeviceType().ordinal());
        builder.setLoginRequest(loginRequestBuilder.build());
        return builder.build();
    }
}
