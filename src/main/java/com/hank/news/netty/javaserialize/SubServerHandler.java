package com.hank.news.netty.javaserialize;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:55
 */
public class SubServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接就绪");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq sub = (SubscribeReq) msg;
        System.out.println("read"+sub.toString());
        SubscribeResp produce = produce(sub.getSubReqId());
        ctx.writeAndFlush(produce);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }


    public SubscribeResp produce(int reqId){
        SubscribeResp resp = new SubscribeResp();
        resp.setDesc("我的商品，质量是不错的了");
        resp.setRespCode(1);
        resp.setSubReqId(reqId);
        return  resp;
    }


}
