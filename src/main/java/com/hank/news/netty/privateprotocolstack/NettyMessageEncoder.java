package com.hank.news.netty.privateprotocolstack;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;
import java.util.Map;

public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    MarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder(MarshallingEncoder marshallingEncoder) {
        this.marshallingEncoder = marshallingEncoder;
    }

    @Override
    protected  void encode(ChannelHandlerContext channelHandlerContext, NettyMessage msg, List<Object> out) throws Exception {
        if (msg==null||msg.getHeader()==null){
         throw new Exception("no message can encode");
        }
        //把各个属性一个接一个的写入
        ByteBuf send = Unpooled.buffer();
        send.writeInt(msg.getHeader().getCrcCode());
        send.writeInt(msg.getHeader().getLength());
        send.writeLong(msg.getHeader().getSessionId());
        send.writeByte(msg.getHeader().getType());
        send.writeByte(msg.getHeader().getPriority());
        send.writeInt(msg.getHeader().getAttachment().size());

        String key=null;
        byte[] byteArray=null;
        for (Map.Entry<String, Object> entry : msg.getHeader().getAttachment().entrySet()) {
            key=entry.getKey();
            byteArray=key.getBytes(CharsetUtil.UTF_8);
            send.writeInt(byteArray.length);
            send.writeBytes(byteArray);
            marshallingEncoder.encode(entry.getValue(),send);
        }
        key=null;
        byteArray=null;
        if (msg.getBody()!=null){
            marshallingEncoder.encode(msg.getBody(),send);
        }else {
            //随便写一点数据，以便区分body有无数据
            send.writeByte(0);
            send.setInt(4,send.readableBytes());

        }
    }

}
