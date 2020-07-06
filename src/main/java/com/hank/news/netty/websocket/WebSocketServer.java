package com.hank.news.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-30 15:41
 */
public class WebSocketServer {


    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sch) throws Exception {
                        ChannelPipeline pipeline = sch.pipeline();
                        //将请求和应答消息编码或者解码成http消息
                        pipeline.addLast("http-codec",new HttpServerCodec());
                        //将http消息的多个部分组成成一条完整的http消息
                        pipeline.addLast("http-aggregator",new HttpObjectAggregator(65536));
                        //用于异步向浏览器发送码流
                        pipeline.addLast("http-chunked",new ChunkedWriteHandler());
                        pipeline.addLast("handler",new WebSocketServerHandler());
                    }
                });
        try {
            ChannelFuture sync = bootstrap.bind(8080).sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }


    }


}
