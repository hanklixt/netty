package com.hank.news.netty.privateprotocolstack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.HashMap;

public class NettyMessageDecoder  extends LengthFieldBasedFrameDecoder {
    private final MarshallingDecoder marshallingDecoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
       this.marshallingDecoder=new MarshallingDecoder();
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        //Netty的LengthFieldBasedFrameDecoder解码器，
        // 它支持自动的TCP粘包和半包处理，只需要给出标识消息长度的字段偏移量和消息长度自身所占的字节数，
        // Netty就能自动实现对半包的处理。对于业务解码器来说，
        // 调用父类LengthFieldBased FrameDecoder的解码方法后，返回的就是整包消息或者为空，
        // 如果为空说明是个半包消息，直接返回继续由I/O线程读取后续的码流
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame==null){
            return null;
        }
        //根据编码顺序，读出Header数据然后设置
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(in.readInt());
        header.setLength(in.readInt());
        header.setSessionId(in.readLong());
        header.setType(in.readByte());
        header.setPriority(in.readByte());

        int size = in.readInt();

        if (size>0){
            HashMap<String, Object> map = new HashMap<>(size);
            int keySize=0;
            byte[] keyArray=null;
            String key=null;
            for (int i=0;i<size;i++){
             keySize= in.readInt();
             keyArray=new byte[keySize];
             in.readBytes(keyArray);
             key = new String(keyArray, CharsetUtil.UTF_8);
                Object obj = marshallingDecoder.decode(in);
                map.put(key,obj);
            }
            keyArray=null;
            key=null;
            header.setAttachment(map);
        }
        int i = in.readInt();
        if (i>4){
            Object body = marshallingDecoder.decode(in);
            message.setBody(body);
        }
        message.setHeader(header);
        return message;
    }
}
