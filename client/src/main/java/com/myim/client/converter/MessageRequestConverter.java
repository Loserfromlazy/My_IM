package com.myim.client.converter;

import com.myim.client.session.ClientSession;
import com.myim.common.entity.ChatMessage;
import com.myim.common.pojo.ProtoMsgOuterClass;

/**
 * <p>
 * MessageConverter
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
public class MessageRequestConverter extends BaseConverter{

    private  ChatMessage chatMessage;

    public MessageRequestConverter(ProtoMsgOuterClass.ProtoMsg.MessageType messageType, ClientSession session, ChatMessage chatMessage) {
        super(messageType, session);
        this.chatMessage = chatMessage;
    }

    public ProtoMsgOuterClass.ProtoMsg.Message getMessageRequest(){
        ProtoMsgOuterClass.ProtoMsg.Message.Builder baseBuilder = getBuilder();
        ProtoMsgOuterClass.ProtoMsg.MessageRequest.Builder builder = ProtoMsgOuterClass.ProtoMsg.MessageRequest.newBuilder();
        builder.setFrom(chatMessage.getFrom());
        builder.setTo(chatMessage.getTo());
        builder.setContent(chatMessage.getContent());
        builder.setMessageType(chatMessage.getContentType().ordinal());
        builder.setMessageId(chatMessage.getMessageId());
        builder.setTimestamp(chatMessage.getMessageId());
        builder.setUrl(chatMessage.getUrl());
        baseBuilder.setMessageRequest(builder.build());
        return baseBuilder.build();
    }
}
