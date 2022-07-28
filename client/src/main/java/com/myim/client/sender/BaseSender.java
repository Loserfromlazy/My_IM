package com.myim.client.sender;

import com.myim.client.session.ClientSession;
import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * BaseSender
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/28
 */
@Slf4j
public abstract class BaseSender {

    protected final void sendMsg(ClientSession session, ProtoMsgOuterClass.ProtoMsg.Message message){
        Channel channel = session.getChannel();
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()){
                sendSuccess();
            }else {
                sendFail();
            }
        });
    }

    public abstract void sendSuccess();
    public abstract void sendFail();
}
