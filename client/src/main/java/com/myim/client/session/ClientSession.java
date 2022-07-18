package com.myim.client.session;

import com.myim.client.entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 客户端会话
 * </p>
 *
 * @author Yuhaoran
 * @since 2020/6/11
 */
@Data
@Slf4j
public class ClientSession {

    static final AttributeKey<ClientSession> attributeKey = AttributeKey.valueOf("SESSION");

    private Channel channel;
    private User user;

    /**
     * 服务端给的的sessionId
     */
    private String sessionId;

    private Boolean isLogin = false;
    private Boolean isConnected = false;

    /**
     * 连接建立成功后 进行channel绑定
     *
     * @param channel
     */
    public ClientSession(Channel channel) {
        //正向绑定
        this.channel = channel;
        this.isConnected = true;
        //反向绑定
        this.channel.attr(attributeKey).set(this);
    }

    public void loginSuccess(String sessionId){
        this.isLogin = true;
        this.sessionId = sessionId;
    }


    /**
     * 获取会话
     *
     * @return ClientSession 会话
     */
    public ClientSession getClientSession() {
        return this.channel.attr(attributeKey).get();
    }


    /**
     * 关闭会话
     *
     * @return ChannelFuture 关闭回调
     */
    public ChannelFuture close() {
        ChannelFuture channelFuture = channel.closeFuture();
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()){
                log.info("关闭成功");
            }
        });
        return channelFuture;
    }


}
