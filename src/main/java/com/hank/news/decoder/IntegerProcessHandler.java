package com.hank.news.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lxt
 * @create 2020-11-02 10:34
 */
@Slf4j
public class IntegerProcessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Integer msgInteger = (Integer) msg;
        log.info("读取到消息:{}",msgInteger);
    }
}
