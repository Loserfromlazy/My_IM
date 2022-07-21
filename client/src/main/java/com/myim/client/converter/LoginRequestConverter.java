package com.myim.client.converter;

import com.myim.client.session.ClientSession;
import com.myim.common.entity.User;
import com.myim.common.pojo.ProtoMsgOuterClass;

/**
 * <p>
 * LoginRequestConvertor
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/21
 */
public class LoginRequestConverter extends BaseConverter{

    private User user;

    public LoginRequestConverter(ProtoMsgOuterClass.ProtoMsg.MessageType messageType, ClientSession session, User user) {
        super(messageType, session);
        this.user = user;
    }

    public ProtoMsgOuterClass.ProtoMsg.Message getLoginRequest(){
        ProtoMsgOuterClass.ProtoMsg.Message.Builder baseBuilder = getBuilder();
        ProtoMsgOuterClass.ProtoMsg.LoginRequest.Builder builder = ProtoMsgOuterClass.ProtoMsg.LoginRequest.newBuilder();
        builder.setUid(user.getUid());
        builder.setDeviceId(user.getDeviceId());
        builder.setToken(user.getToken());
        builder.setDeviceType(user.getDeviceType().ordinal());
        baseBuilder.setLoginRequest(builder.build());
        return baseBuilder.build();
    }
}
