package com.hank.news.proto.coder;

import com.hank.news.protopojo.MsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author lxt
 * @create 2020-11-24 16:30
 */
public class ProtobufEncoder extends MessageToByteEncoder<MsgProto.MsgP> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MsgProto.MsgP msgP, ByteBuf byteBuf) throws Exception {
        //将proto对象转换为字节
        byte[] bytes = msgP.toByteArray();
        //把对象转化为字节
        int length = bytes.length;
        //写入字节长度 --
        //省略魔数，混淆值--写法都是一样的
        byteBuf.writeShort(length);
        byteBuf.writeBytes(bytes);

    }
}
