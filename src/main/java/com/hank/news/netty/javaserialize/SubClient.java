package com.hank.news.netty.javaserialize;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.InetSocketAddress;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:54
 */
public class SubClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group).channel(NioSocketChannel.class)
                //关闭延迟
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        //解码器
                        socketChannel.pipeline().addLast(new ObjectDecoder(1024*1024,
                                //禁止缓存类加载器
                                ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));

                        socketChannel.pipeline().addLast(new ObjectEncoder());

                        socketChannel.pipeline().addLast(new SubClientHandler());

                    }
                });
        try {
            ChannelFuture sync = bootstrap.connect(new InetSocketAddress("127.0.0.1", 7777)).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }

    }
}
