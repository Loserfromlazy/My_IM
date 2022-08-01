package com.myim.server.process;

import com.myim.common.pojo.ProtoMsgOuterClass;
import com.myim.server.session.ServerSession;
import com.myim.server.session.ServerSessionMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * ChatProcesser
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/1
 */
@Component
@Slf4j
public class ChatProcessor implements BaseProcessor {

    @Override
    public boolean action(ServerSession serverSession, ProtoMsgOuterClass.ProtoMsg.Message message) {
        final ProtoMsgOuterClass.ProtoMsg.MessageRequest messageRequest = message.getMessageRequest();
        String to = messageRequest.getTo();
        List<ServerSession> sessionsByUserId = ServerSessionMap.getServerSessionMap().getSessionByUserId(to);
        if (sessionsByUserId == null || sessionsByUserId.size() == 0) {
            log.info("接收方{}不在线，发送失败", to);
            return false;
        }
        sessionsByUserId.forEach(session -> {
            session.getChannel().writeAndFlush(message);
        });
        return true;
    }
}
