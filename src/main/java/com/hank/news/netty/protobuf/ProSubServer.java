package com.hank.news.netty.protobuf;

import com.hank.news.netty.javaserialize.SubServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:54
 */
public class ProSubServer {

    public static void main(String[] args) {

        NioEventLoopGroup boss = new NioEventLoopGroup();

        NioEventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)//最大连接数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//保持活跃状态
                .handler(new LoggingHandler()) //日志处理
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        // protoBuf 编解码配置
                        //用于半包处理
                        socketChannel.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        // 要解码的目标类
                        socketChannel.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
                        //对protoBuf协议的消息头添加一个长度为32的整形字段，用于标记这个消息的长度
                        socketChannel.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        //编码器
                        socketChannel.pipeline().addLast(new ProtobufEncoder());


                        //业务处理
                        socketChannel.pipeline().addLast(new ProSubServerHandler());

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
