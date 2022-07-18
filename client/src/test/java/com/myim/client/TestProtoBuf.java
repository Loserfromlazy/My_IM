package com.myim.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestProtoBuf {

    @Test
    public void testProtoBuf(){

        ChannelInitializer channelInitializer = new ChannelInitializer() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline().addLast();
            }
        };

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(channelInitializer);

    }
}
