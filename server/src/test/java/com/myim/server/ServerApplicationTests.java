package com.myim.server;

import com.myim.common.codec.SimpleProtobufDecoder;
import com.myim.common.codec.SimpleProtobufEncoder;
import com.myim.common.entity.User;
import com.myim.common.pojo.ProtoMsgOuterClass;
import com.myim.server.handler.LoginRequestHandler;
import com.myim.server.session.ServerSession;
import com.myim.server.session.ServerSessionMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ServerApplicationTests {

    @Test
    void contextLoads() {
    }

    static final AttributeKey<ServerSession> SESSION =    AttributeKey.valueOf("SESSION");
    @Test
    public void testServerSession(){
        //正向绑定
        ServerSession session = new ServerSession(new EmbeddedChannel());
        //反向绑定
        session.bindChannel();
        session.setUser(new User());
        //正向导航
        Channel channel = session.getChannel();
        //反向导航
        ServerSession session1 = channel.attr(SESSION).get();
        System.out.println(session1.getUser());
    }

    @Test
    public void testSessionMap(){
        final User user = new User();
        for (int i = 0; i < 10; i++) {
            ServerSession serverSession = new ServerSession(new EmbeddedChannel());
            serverSession.setUser(user);
            serverSession.bindChannel();
        }
        final ServerSessionMap map = ServerSessionMap.getServerSessionMap();
        final List<ServerSession> userList = map.getSessionByUserId(user.getUid());
        System.out.println("size="+userList.size());
        userList.forEach(System.out::println);

    }

    @Autowired
    LoginRequestHandler loginRequestHandler;

    @Test
    public void testLoginHandler(){
        ChannelInitializer initializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new SimpleProtobufDecoder());
                ch.pipeline().addLast("login",loginRequestHandler);
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(initializer);
        final ByteBuf buf = buildLoginBuffer();
        channel.writeInbound(buf);
        channel.flush();

        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ByteBuf buildLoginBuffer(){
        final ByteBuf buffer = Unpooled.buffer();
        final ProtoMsgOuterClass.ProtoMsg.Message.Builder builder = ProtoMsgOuterClass.ProtoMsg.Message.newBuilder();
        final ProtoMsgOuterClass.ProtoMsg.LoginRequest.Builder requestBuilder = ProtoMsgOuterClass.ProtoMsg.LoginRequest.newBuilder();
        User user = new User();
        requestBuilder.setUid(user.getUid());
        requestBuilder.setToken(user.getToken());
        requestBuilder.setDeviceId(user.getDeviceId());
        requestBuilder.setDeviceType(user.getDeviceType().ordinal());
        builder.setSeq(1);
        builder.setType(ProtoMsgOuterClass.ProtoMsg.MessageType.LOGIN_REQUEST);
        builder.setLoginRequest(requestBuilder.build());
        SimpleProtobufEncoder.encode0(builder.build(),buffer);
        return buffer;
    }

}
