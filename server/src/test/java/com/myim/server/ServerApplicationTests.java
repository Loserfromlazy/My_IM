package com.myim.server;

import com.myim.common.entity.User;
import com.myim.server.session.ServerSession;
import com.myim.server.session.ServerSessionMap;
import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;
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

}
