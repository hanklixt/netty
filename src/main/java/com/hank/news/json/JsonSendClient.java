package com.hank.news.json;


import com.hank.news.util.JsonUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lxt
 * @create 2020-11-05 11:29
 */
@Slf4j
public class JsonSendClient {

     private static   int port=9999;

     private static    String host="localhost";

     private static Bootstrap bootstrap= new Bootstrap();

     public static void main(String[] args) throws InterruptedException {
         EventLoopGroup boss = new NioEventLoopGroup(1);
         bootstrap.group(boss);
         bootstrap.channel(NioSocketChannel.class);
         bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
         bootstrap.remoteAddress(host,port);
         bootstrap.handler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel sch) throws Exception {
                 sch.pipeline().addLast(new LengthFieldPrepender(4));
                 sch.pipeline().addLast(new StringEncoder());
             }
         });
         ChannelFuture connectFuture = bootstrap.connect();
         connectFuture.addListener((ChannelFuture future)->{
             if (future.isSuccess()){
               log.info("客户端连接成功");
               } else {
               log.info("客户端连接失败");
             }
         });
         ChannelFuture sync = connectFuture.sync();

         Channel channel = sync.channel();

         Msg msg = new Msg();
         for (int i=0;i<99;i++){
             msg.setId(i);
             msg.setMsg("send "+i);
             channel.writeAndFlush(JsonUtil.parseToJson(msg));
             log.info("send msg"+msg.toString());
         }

         channel.flush();

         channel.closeFuture().sync();

         boss.shutdownGracefully();

     }
}
