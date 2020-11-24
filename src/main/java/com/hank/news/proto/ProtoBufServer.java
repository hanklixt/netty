package com.hank.news.proto;

import com.hank.news.protopojo.MsgProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lxt
 * @create 2020-11-23 16:20
 */
@Slf4j
public class ProtoBufServer {

    private final static EventLoopGroup boss=new NioEventLoopGroup();
    private final static EventLoopGroup worker=new NioEventLoopGroup();
    private final static int port=9999;

    public static void main(String[] args) {

        ServerBootstrap b = new ServerBootstrap();
        b.group(boss,worker);
        b.channel(NioServerSocketChannel.class);
        b.localAddress(port);
        b.option(ChannelOption.SO_KEEPALIVE,true);
        b.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel sch) throws Exception {
                sch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                sch.pipeline().addLast(new ProtobufDecoder(MsgProto.MsgP.getDefaultInstance()));
                sch.pipeline().addLast(new ProtoEndDecoder());
            }
        });

        try{
            ChannelFuture channelFuture = b.bind().sync();

            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            log.error(e.getMessage());
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


   static class ProtoEndDecoder extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            MsgProto.MsgP receive = (MsgProto.MsgP) msg;

            log.info("----------收到数据包");
            log.info("msg id:{}",receive.getId());
            log.info("msg content:{}",receive.getContent());

        }
    }


}
