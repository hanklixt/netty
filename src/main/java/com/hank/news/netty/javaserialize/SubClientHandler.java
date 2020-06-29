package com.hank.news.netty.javaserialize;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:56
 */
public class SubClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        SubscribeReq req = new SubscribeReq();
        req.setAddress("贵州省");
        req.setPhoneNumber("19900987732");
        req.setProductName("滑板车");
        req.setUserName("张三");

        for (int i=1;i<11;i++){
            req.setSubReqId(i);
            ctx.writeAndFlush(req);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeResp resp = (SubscribeResp) msg;
        System.out.println(resp.toString());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println(cause.getMessage());

        ctx.close();
    }
}
