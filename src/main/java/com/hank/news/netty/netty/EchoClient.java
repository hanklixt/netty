package com.hank.news.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 10:14
 */
public class EchoClient {

    public static void main(String[] args) {

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                //禁用nagle,di
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        //添加编解码器，不使用可能会出现粘包拆包问题

                        //使用$_符号作为分隔符,指定分隔符解码器
//                        ByteBuf copiedBuffer = Unpooled.copiedBuffer("$_".getBytes());
//                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,copiedBuffer));

//                        换行分割符解码器
//                        socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        socketChannel.pipeline().addLast(new StringDecoder());


                        socketChannel.pipeline().addLast(new EchoClientHandler());
                    }
                });

        try {
            //发起异步连接,也是Future模式,调用sync等待连接成功
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 7777).sync();
            //closeFuture会在channel——close的时候通知该线程--一般都会正常阻塞在这里
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }


    }
}
