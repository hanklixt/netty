package com.hank.news.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-24 16:37
 */
public class EchoServer {

    public static void main(String[] args) {

        /**
         * 两个线程组都是无限循环
         */
        //处理请求
        NioEventLoopGroup boss = new NioEventLoopGroup();
        //工作线程
        NioEventLoopGroup worker = new NioEventLoopGroup();
        //创建服务端的启动对象
        ServerBootstrap server = new ServerBootstrap();

        server.group(boss)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024) //设置最大连接数
                .childOption(ChannelOption.SO_KEEPALIVE,true)  //保持活跃状态
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {

                        channel.pipeline().addLast(new FixedLengthFrameDecoder(20));

                        //使用$_符号作为分隔符m,指定分隔符解码器最大长度
//                        ByteBuf copiedBuffer = Unpooled.copiedBuffer("$_".getBytes());
//
//                        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,copiedBuffer));

//                        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));

                        channel.pipeline().addLast(new StringDecoder());

                        channel.pipeline().addLast(new EchoServerHandler());
                    }
                });
        try {
            ChannelFuture sync = server.bind(new InetSocketAddress(7777)).sync();
            //关闭监听端口
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
        System.out.println("server is ready");


    }
}
