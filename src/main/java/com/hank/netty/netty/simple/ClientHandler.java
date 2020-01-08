package com.hank.netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author lxt
 * @date 2019-12-31-11:44
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    //通道准备就绪，会调用此方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    //通道可读时，调用此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("收到服务端信息:" + msg);
    }
}
