package com.myim.client.sender;

import com.myim.client.converter.MessageRequestConverter;
import com.myim.client.session.ClientSession;
import com.myim.common.entity.ChatMessage;
import com.myim.common.pojo.ProtoMsgOuterClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MessageSender
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
@Component
@Slf4j
public class MessageSender extends BaseSender {

    public void send(ChatMessage chatMessage, ClientSession session) {
        MessageRequestConverter converter = new MessageRequestConverter(ProtoMsgOuterClass.ProtoMsg.MessageType.MESSAGE_REQUEST, session, chatMessage);
        ProtoMsgOuterClass.ProtoMsg.Message message = converter.getMessageRequest();
        super.sendMsg(session, message);
    }

    @Override
    public void sendSuccess() {
        log.info("聊天消息发送成功");
    }

    @Override
    public void sendFail() {
        log.info("聊天消息发送失败");
    }
}
