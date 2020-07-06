package com.hank.news.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetSocketAddress;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-29 13:25
 */
public class HttpFileServer {

    private static final  String DEFAULT_URl="/src/main";


    public static void main(String[] args) {
        run(DEFAULT_URl,5200);
    }

    private static void run(final String url,final  int port){
        NioEventLoopGroup worker = new NioEventLoopGroup();
        NioEventLoopGroup boss = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(worker,boss)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sch) throws Exception {

                        //http消息解码器
                        sch.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                        //将多个消息合并成单个的FullHttpRequest或者是FullHttpResponse
                        sch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                        sch.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                        //支持异步发送大的码流，但是不占用过多的内存，方式java内存溢出
                        sch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                        sch.pipeline().addLast("httpFileHandler",new HttpFileServerHandler(url));

                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(new InetSocketAddress(5200)).sync();
            System.out.println("http文件目录服务器启动了，地址是:" + "127.0.0.1:" + port + "/" + url);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }


}
