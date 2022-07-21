package com.myim.common.codec;

import com.myim.common.constant.ProtoConstant;
import com.myim.common.exception.InvalidException;
import com.myim.common.pojo.ProtoMsgOuterClass;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * <p>
 * protobuf简单解码器
 * </p>
 *
 * @author Yuhaoran
 * @since 2020/6/11
 */
public class SimpleProtobufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        Object o = decode0(byteBuf,channelHandlerContext);
        if (o != null) {
            list.add(o);
        }
    }

    public static Object decode0(ByteBuf byteBuf,ChannelHandlerContext channelHandlerContext) throws Exception {
        byteBuf.markReaderIndex();
        //这里判断是否够协议的头部长度：2字节魔数+2字节版本号+4字节内容长度
        if (byteBuf.readableBytes() < 8) {
            return null;
        }
        short magicNum = byteBuf.readShort();
        if (magicNum != ProtoConstant.MAGIC_NUM) {
            throw new InvalidException("客户端口令不对");
        }
        short versionCode = byteBuf.readShort();
        if (versionCode != ProtoConstant.VERSION_CODE) {
            throw new InvalidException("协议版本不对");
        }
        int length = byteBuf.readInt();
        if (length < 0) {
            //非法数据，关闭连接
            channelHandlerContext.close();
        }
        //内容长度不够就不读取
        if (length > byteBuf.readableBytes()) {
            byteBuf.resetReaderIndex();
            return null;
        } else {
            //读取数据到堆内存，并反序列化ProtoBuf信息
            byte[] array = new byte[length];
            byteBuf.readBytes(array);
            return ProtoMsgOuterClass.ProtoMsg.Message.parseFrom(array);
        }
    }


}
