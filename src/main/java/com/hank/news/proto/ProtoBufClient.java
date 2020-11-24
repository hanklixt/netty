package com.hank.news.proto;

import com.hank.news.protopojo.MsgProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lxt
 * @create 2020-11-23 16:23
 */
@Slf4j
public class ProtoBufClient {

    private final static int port=9999;

    private final static String host="localhost";

    public static void main(String[] args) {

        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();

        b.group(workerGroup);

        b.channel(NioSocketChannel.class);

        b.option(ChannelOption.SO_KEEPALIVE,true);

        b.remoteAddress(host,port);

        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sch) throws Exception {
                sch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                sch.pipeline().addLast(new ProtobufEncoder());
            }
        });

        ChannelFuture connectFuture = b.connect();

        connectFuture.addListener((ChannelFuture channel)->{
            if (connectFuture.isSuccess()){
                log.info("连接成功");
            }else {
                log.info("连接失败");
            }
        });

        try{
            Channel channel = connectFuture.sync().channel();

            for (int i=0;i<10;i++){
                MsgProto.MsgP.Builder builder = MsgProto.MsgP.newBuilder();
                builder.setId(i);
                builder.setContent("第"+i+"次报文");
                channel.writeAndFlush(builder);
                log.info("第"+i+"次报文");
            }
            channel.flush();

            channel.closeFuture().sync();
        }catch (Exception e){
            log.info("出现异常:{}",e.getMessage());
        }finally {
            workerGroup.shutdownGracefully();
        }

    }

}


