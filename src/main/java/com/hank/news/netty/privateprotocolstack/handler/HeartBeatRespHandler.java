package com.hank.news.netty.privateprotocolstack.handler;

import com.hank.news.netty.privateprotocolstack.Header;
import com.hank.news.netty.privateprotocolstack.MessageType;
import com.hank.news.netty.privateprotocolstack.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      NettyMessage  heartMessage= (NettyMessage) msg;
      if (heartMessage.getHeader()!=null&&MessageType.HEART_BEAT_REQ.value==heartMessage.getHeader().getType()){
          NettyMessage resp = buildHeartResp();
          System.out.println("server send resp " + resp.toString());
          ctx.writeAndFlush(resp);
      }else {
          ctx.fireChannelRead(msg);
      }
    }

    private NettyMessage buildHeartResp(){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEART_BEAT_RESP.value);
        message.setHeader(header);
        return message;

    }


}
