package com.myim.common.codec;

import com.myim.common.constant.ProtoConstant;
import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * <p>
 * protobuf简单编码器
 * </p>
 *
 * @author Yuhaoran
 * @since 2020/6/11
 */
public class SimpleProtobufEncoder extends MessageToByteEncoder<ProtoMsgOuterClass.ProtoMsg.Message> {

    private String MAGIC_NUM;
    private String VERSION_CODE;


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ProtoMsgOuterClass.ProtoMsg.Message message, ByteBuf byteBuf) throws Exception {
        encode0(message, byteBuf);
    }

    /**
     * 将protobuf数据编码成二进制数据
     *
     * @param message 待编码消息
     * @param byteBuf 编码后二进制数据
     */
    public static void encode0(ProtoMsgOuterClass.ProtoMsg.Message message, ByteBuf byteBuf) {
        byte[] bytes = message.toByteArray();
        byteBuf.writeShort(ProtoConstant.MAGIC_NUM);
        byteBuf.writeShort(ProtoConstant.VERSION_CODE);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
