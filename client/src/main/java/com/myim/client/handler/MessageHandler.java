package com.myim.client.handler;

import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * MessageHandler
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
@Slf4j
@Service
public class MessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ProtoMsgOuterClass.ProtoMsg.Message message= (ProtoMsgOuterClass.ProtoMsg.Message) msg;
        ProtoMsgOuterClass.ProtoMsg.MessageType type = message.getType();

        if (ProtoMsgOuterClass.ProtoMsg.MessageType.MESSAGE_REQUEST!=type){
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsgOuterClass.ProtoMsg.MessageRequest messageRequest = message.getMessageRequest();
        final String from = messageRequest.getFrom();
        final String content = messageRequest.getContent();
        System.out.println("用户"+from +"发来信息："+content);

    }
}
