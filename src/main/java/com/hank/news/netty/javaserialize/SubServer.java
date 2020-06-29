package com.hank.news.netty.javaserialize;

import com.hank.netty.netty.simple.ClientHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:54
 */
public class SubServer {

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup();

        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)//最大连接数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//保持活跃状态
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //设置解码器
                        socketChannel.pipeline().addLast(new ObjectDecoder(1024*1024,
                                //缓存类加载器
                                ClassResolvers.weakCachingResolver(this.getClass().getClassLoader())));
                        //编码器
                        socketChannel.pipeline().addLast(new ObjectEncoder());
                        //业务处理
                        socketChannel.pipeline().addLast(new SubServerHandler());

                    }
                });
        try {
            ChannelFuture future = bootstrap.bind(7777).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }


    }

}
