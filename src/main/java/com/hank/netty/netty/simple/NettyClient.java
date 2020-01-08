package com.hank.netty.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author lxt
 * @date 2019-12-31-11:36
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {
        final NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        final Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventExecutors) //设置监听线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sch) throws Exception {
                            sch.pipeline().addLast(new ClientHandler());
                        }
                    });
            final ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
            //监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
