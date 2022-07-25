package com.myim.server.session;

import com.myim.common.entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

import java.util.UUID;

/**
 * <p>
 * ServerSession
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/25
 */
public class ServerSession {
    static final AttributeKey<ServerSession> SESSION = AttributeKey.valueOf("SESSION");

    /**
     * session唯一标识
     */
    private String sessionId;
    /**
     * 通道
     */
    private Channel channel;
    /**
     * 用户
     */
    private User user;
    private Boolean isLogin = false;

    public ServerSession(Channel channel) {
        //正向绑定
        this.channel = channel;
        this.sessionId = buildSessionId();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.user.setSessionId(this.sessionId);
    }

    public String getSessionId() {
        return sessionId;
    }


    public Channel getChannel() {
        return channel;
    }

    /**
     * 反向导航，从channel中获取ServerSession
     *
     * @author Yuhaoran
     * @date 2022/7/25 11:33
     */
    public ServerSession getServerSession(ChannelHandlerContext ctx) {
        return ctx.channel().attr(SESSION).get();
    }

    /**
     * 登录成功进行反向绑定，将Channel绑定上此ServerSession
     *
     * @author Yuhaoran
     * @date 2022/7/25 12:45
     */
    public void bindChannel() {
        this.channel.attr(SESSION).set(this);
        this.isLogin = true;
        ServerSessionMap serverSessionMap = ServerSessionMap.getServerSessionMap();
        serverSessionMap.addSession(this);
    }


    private String buildSessionId() {
        return UUID.randomUUID().toString();
    }


}
