package com.hank.news.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 10:20
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ctx.writeAndFlush(Unpooled.copiedBuffer("hello".getBytes()));

        ByteBuf buf=null;
        for (int i=0;i<100;i++){
 // 末尾添加不同解码器得分割符
//            String s = "hello" + i + System.getProperty("line.separator");
            //
            String s = "hello" + i + "$_";
            buf=Unpooled.buffer(s.length());
            buf.writeBytes(s.getBytes());
            ctx.writeAndFlush(buf);
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf= (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req, CharsetUtil.UTF_8);
        System.out.println("receive body" + msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("procuduce  excption" + cause.getMessage());
        ctx.close();
    }

}
