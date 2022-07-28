package com.myim.client.sender;

import com.myim.client.converter.LoginRequestConverter;
import com.myim.client.session.ClientSession;
import com.myim.common.entity.User;
import com.myim.common.pojo.ProtoMsgOuterClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * LoginSender
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/28
 */
@Component
@Slf4j
public class LoginSender extends BaseSender{

    public void send(User user, ClientSession session){
        LoginRequestConverter converter = new LoginRequestConverter(ProtoMsgOuterClass.ProtoMsg.MessageType.LOGIN_REQUEST,session,user);
        final ProtoMsgOuterClass.ProtoMsg.Message message = converter.getLoginRequest();
        super.sendMsg(session,message);
    }

    @Override
    public void sendSuccess() {
        log.info("登录消息发送成功");
    }

    @Override
    public void sendFail() {
        log.info("登录消息发送失败");
    }
}
