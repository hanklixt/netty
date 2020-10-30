package com.hank.sharableHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author lxt
 * @create 2020-10-30 14:21
 */
public class NettyEchoServer {

    private final static int port=9000;
    //serverBootstrap
    private final static ServerBootstrap bootstrap=new ServerBootstrap();

    private final static NettyEchoShareHandler shareHandler=new NettyEchoShareHandler();

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();

        bootstrap.group(boss,worker);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.localAddress(port);

        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        //ByteBuf 的内存形式确认,需要结合-Dio.netty.noUnsafe
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sch) throws Exception {
                //每次新的连接过过来后，给流水线的业务处理器都是这个
                sch.pipeline().addLast(shareHandler);
            }
        });
        //绑定监听，调用sync方法直到阻塞直到成功
        try {
            ChannelFuture channelFuture = bootstrap.bind().sync();

            //阻塞等待关闭通道的异步任务结束
            channelFuture.channel().closeFuture().sync();

        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }



    }



}
