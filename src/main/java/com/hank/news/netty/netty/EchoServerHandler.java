package com.hank.news.netty.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-24 17:58
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //最后调用flush方法，把缓冲数组的消息全部发送写到socketChannel中
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("建立了连接");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //不使用编解码器的时候有可能会出现粘包拆包问题
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        String req = new String(bytes, CharsetUtil.UTF_8);
        System.out.println("receive req" +  msg);
//        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                +System.getProperty("line.separator");
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                +"$_";
        //调用write方法只是把消息加入到到待发送的缓冲数组中
        ctx.write(Unpooled.copiedBuffer(currentTime.getBytes()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       //关闭和释放关于ChannelHandlerContext的资源和句柄
       ctx.close();
    }
}
