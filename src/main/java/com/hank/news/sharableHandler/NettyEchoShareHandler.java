package com.hank.news.sharableHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lxt
 * @create 2020-10-30 14:45
 * hander 标记为单例的
 */
@Slf4j
@ChannelHandler.Sharable
public class NettyEchoShareHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bf = (ByteBuf) msg;
        String s = bf.hasArray() ? "堆内存" : "堆外内存";
        log.info(s);

        int len = bf.readableBytes();
        byte[] arr = new byte[len];
        bf.getBytes(0,arr);

        log.info("receive:"+new String(arr, CharsetUtil.UTF_8));


        log.info("rnt:{}",bf.refCnt());

        ChannelFuture cf = ctx.writeAndFlush(msg);

        cf.addListener((ChannelFuture futureListener) -> {
            log.info("写回后，msg.refCnt:" + bf.refCnt());
        });



    }


}
