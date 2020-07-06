package com.hank.news.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class UDPClient {




    public static void main(String[] args) {

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(eventExecutors).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new UDPClientHandler());

        try {
            Channel channel = bootstrap.bind(0).sync().channel();
            //udp协议不需要建立通道，所以这里直接发送数据包
            for (int i=0;i<10;i++){
                channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("查询谚语", CharsetUtil.UTF_8),
                        new InetSocketAddress("255.255.255.255",8080))).sync();
                //255.255.255.255 表示该网段的所有ip-----
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
