package com.myim.client.converter;

import com.myim.client.session.ClientSession;
import com.myim.common.entity.User;
import com.myim.common.pojo.ProtoMsgOuterClass;

/**
 * <p>
 * HeartBeatConverter
 * </p>
 *
 * @author Yuhaoran
 * @since 2022/8/2
 */
public class HeartBeatConverter extends BaseConverter{


    private User user;

    public HeartBeatConverter(ProtoMsgOuterClass.ProtoMsg.MessageType messageType, ClientSession session,User user) {
        super(messageType, session);
        this.user = user;
    }


    public ProtoMsgOuterClass.ProtoMsg.Message getHeartBeatMsg(){
        ProtoMsgOuterClass.ProtoMsg.Message.Builder baseBuilder = getBuilder();
        final ProtoMsgOuterClass.ProtoMsg.MessageHeartBeat.Builder builder = ProtoMsgOuterClass.ProtoMsg.MessageHeartBeat.newBuilder();
        builder.setSeq(1);
        builder.setUid(user.getUid());
        baseBuilder.setMessageHeartBeat(builder.build());
        return baseBuilder.build();
    }
}
