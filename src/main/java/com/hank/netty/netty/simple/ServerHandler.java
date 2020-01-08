package com.hank.netty.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author lxt
 * @date 2019-12-31-11:01
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    //读取数据 这里我们可以读取客户端发送的消息
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("ctx is" + ctx);
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("receive message: " + buf);
        System.out.println("客户端地址" + ctx.channel().remoteAddress());
    }

    //读取完毕后在这里做操作
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("server received msg", CharsetUtil.UTF_8));
    }
}
