package com.hank.news.netty.udp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;


import java.net.InetSocketAddress;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-07-01 9:44
 */
public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final String[] DICTIONARY={"只要功夫深，铁棒磨成针。",
              "旧时王谢堂前燕，飞入寻常百姓家。", "洛阳亲友如相问，一片冰心在玉壶。", "一寸光阴一寸金，寸金难买寸光阴。",
                      "老骥伏枥，志在千里。烈士暮年，壮心不已!" };


    private String nextQuote(){
        int i = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
        return DICTIONARY[i];

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        String req = packet.content().toString(CharsetUtil.UTF_8);
        if (req.equals("查询谚语")){
            System.out.println("receive req" + req);
            ctx.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语查询结果为:"+nextQuote(),
                    CharsetUtil.UTF_8),packet.sender()));

        }

    }

}
