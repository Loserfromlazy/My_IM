package com.myim.server.converter;

import com.myim.common.pojo.ProtoMsgOuterClass;
import org.springframework.stereotype.Component;

/**
 * <p>
 * LoginResponseConverter
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
@Component
public class LoginResponseConverter {

    public ProtoMsgOuterClass.ProtoMsg.Message build(String msg, int code, boolean result,long seq,String sessionId) {
        ProtoMsgOuterClass.ProtoMsg.Message.Builder builder = ProtoMsgOuterClass.ProtoMsg.Message.newBuilder();
        final ProtoMsgOuterClass.ProtoMsg.LoginResponse.Builder responseBuilder = ProtoMsgOuterClass.ProtoMsg.LoginResponse.newBuilder();
        responseBuilder.setMsg(msg);
        responseBuilder.setCode(code);
        responseBuilder.setResult(result);
        builder.setLoginResponse(responseBuilder);
        builder.setSeq(seq);
        builder.setSessionId(sessionId);
        builder.setType(ProtoMsgOuterClass.ProtoMsg.MessageType.LOGIN_RESPONSE);
        return builder.build();
    }
}
