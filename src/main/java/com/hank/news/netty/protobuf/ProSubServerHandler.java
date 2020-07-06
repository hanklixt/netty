package com.hank.news.netty.protobuf;

import com.hank.news.netty.javaserialize.SubscribeReq;
import com.hank.news.netty.javaserialize.SubscribeResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-28 15:55
 */
public class ProSubServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接就绪");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReqProto.SubscribeReq sub = (SubscribeReqProto.SubscribeReq) msg;
        System.out.println("read"+sub.toString());
        SubscribeRespProto.SubscribeResp produce = produce(sub.getSubReqId());
        ctx.writeAndFlush(produce);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }


    public SubscribeRespProto.SubscribeResp produce(int reqId){
        SubscribeRespProto.SubscribeResp.Builder resp = SubscribeRespProto.SubscribeResp.newBuilder();
        resp.setDesc("我的商品，质量是不错的了");
        resp.setSubRespCode(String.valueOf(reqId));
        resp.setSubReqId(reqId);
        return  resp.build();
    }

}
