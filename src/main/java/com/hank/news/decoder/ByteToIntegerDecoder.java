package com.hank.news.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author lxt
 * @create 2020-11-02 10:26
 */
@Slf4j
public class ByteToIntegerDecoder extends ByteToMessageDecoder {

    /**
     * ByteToMessageDecoder 流程，读取到的ByteBuf,解码成object,加入List<Object>___end
     * @param channelHandlerContext
     * @param in
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        while (in.readableBytes()>=4){
            int i = in.readInt();
            log.info("解码出一个整数:"+i);
            list.add(i);
        }
        System.out.println("解码完成");

    }
}

