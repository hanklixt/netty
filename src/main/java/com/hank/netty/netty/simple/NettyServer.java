package com.hank.netty.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lxt
 * @date 2019-12-31-10:20
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        /**
         * 1.bossGroup负责处理客户端的连接
         *  2.workGroup负责处理数据读写
         *  3.两个都是无限循环
         *
         */
        final NioEventLoopGroup workGroups = new NioEventLoopGroup();
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //4.创建服务端的启动对象
        final ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            //5.启动参数设置
            bootstrap.group(bossGroup, workGroups)//设置线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel为channel的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持连接活动状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sch) throws Exception {
                            sch.pipeline().addLast(new ServerHandler());
                        }
                    });
            System.out.println("server is  ready");
            //绑定端口设置为同步
            final ChannelFuture cf = bootstrap.bind(6668).sync();
            //监听关闭的端口
            ChannelFuture sync = cf.channel().closeFuture().sync();
        } finally {
            workGroups.shutdownGracefully();

            bossGroup.shutdownGracefully();
        }
    }
}
