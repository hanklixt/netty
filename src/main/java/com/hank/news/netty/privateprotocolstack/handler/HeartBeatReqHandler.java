package com.hank.news.netty.privateprotocolstack.handler;

import com.hank.news.netty.privateprotocolstack.Header;
import com.hank.news.netty.privateprotocolstack.MessageType;
import com.hank.news.netty.privateprotocolstack.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatReqHandler  extends ChannelInboundHandlerAdapter {

    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        NettyMessage message= (NettyMessage) msg;
        // 握手成功--开启异步任务
        if (message.getHeader()!=null&&message.getHeader().getType()== MessageType.LOGIN_RESP.value){
            //定时发送心跳
           heartBeat= ctx.executor().scheduleAtFixedRate(new HeartBeatTask(ctx)
                    ,0,5000, TimeUnit.MILLISECONDS);
        }else if (message.getHeader()!=null&&message.getHeader().getType()==MessageType.HEART_BEAT_REQ.value){
            System.out.println("client receive server heart beat");
        }else {
         ctx.fireChannelRead(ctx);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    private class HeartBeatTask implements Runnable{

        private final ChannelHandlerContext context;

        public HeartBeatTask(final ChannelHandlerContext context) {
            this.context = context;
        }

        @Override
        public void run() {
            NettyMessage message = buildNettyMessage();
            System.out.println(message.toString());
            context.writeAndFlush(message);
        }

        private NettyMessage buildNettyMessage(){
            NettyMessage message=new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEART_BEAT_REQ.value);
            message.setHeader(header);
            return message;
        }


    }
}
