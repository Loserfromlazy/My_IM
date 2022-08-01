package com.myim.server.process;

import com.myim.common.constant.ProtoConstant;
import com.myim.common.entity.User;
import com.myim.common.pojo.ProtoMsgOuterClass;
import com.myim.server.converter.LoginResponseConverter;
import com.myim.server.session.ServerSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * LoginCheckProcess
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/7/27
 */
@Service
public class LoginCheckProcessor implements BaseProcessor{
    @Autowired
    LoginResponseConverter converter;

    @Override
    public boolean action(ServerSession session,ProtoMsgOuterClass.ProtoMsg.Message message){
        final ProtoMsgOuterClass.ProtoMsg.LoginRequest loginRequest = message.getLoginRequest();
        User user = User.fromLoginRequets(loginRequest);
        if (!checkUser(user)){
            ProtoMsgOuterClass.ProtoMsg.Message result = converter.build("用户校验失败", ProtoConstant.FAIL, false, message.getSeq(), user.getSessionId());
            session.writeAndFlush(result);
            return false;
        }
        session.setUser(user);
        session.bindChannel();
        ProtoMsgOuterClass.ProtoMsg.Message result = converter.build("登录成功", ProtoConstant.SUCCESS, true, message.getSeq(), user.getSessionId());
        session.writeAndFlush(result);
        return true;
    }

    private boolean checkUser(User user){
        if (user.getToken() == null){
            return false;
        }
        //需要连接DB或Redis等对用户进行校验，这里省略校验，默认登录成功
        return true;
    }
}
