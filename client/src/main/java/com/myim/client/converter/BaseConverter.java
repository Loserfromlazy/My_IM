package com.myim.client.converter;

import com.myim.client.session.ClientSession;
import com.myim.common.pojo.ProtoMsgOuterClass;

/**
 * <p>
 * BaseConverter
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/21
 */
public class BaseConverter {
    private ProtoMsgOuterClass.ProtoMsg.MessageType messageType;
    private ClientSession session;

    public BaseConverter(ProtoMsgOuterClass.ProtoMsg.MessageType messageType, ClientSession session) {
        this.messageType = messageType;
        this.session = session;
    }

    public ProtoMsgOuterClass.ProtoMsg.Message.Builder getBuilder(){
        final ProtoMsgOuterClass.ProtoMsg.Message.Builder builder = ProtoMsgOuterClass.ProtoMsg.Message.newBuilder();
        builder.setType(messageType);
        builder.setSessionId(session.getSessionId());
        return builder;
    }
}
