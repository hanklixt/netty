package com.hank.news.netty.protobuf;

import com.hank.news.netty.javaserialize.SubscribeReq;
import com.hank.news.netty.javaserialize.SubscribeResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:56
 */
public class ProSubClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();

       List<String> addrs= new ArrayList<>();

       addrs.add("jj");
       addrs.add("ss");
       builder.addAllAddress(addrs);
       builder.setUserName("hank");
       builder.setProduceName("hhh ");
        for (int i=1;i<11;i++){
            builder.setSubReqId(i);
            ctx.writeAndFlush(builder.build());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeRespProto.SubscribeResp resp = (SubscribeRespProto.SubscribeResp) msg;
        System.out.println(resp.toString());
        System.out.println(resp.getDesc());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println(cause.getMessage());

        ctx.close();
    }
}
