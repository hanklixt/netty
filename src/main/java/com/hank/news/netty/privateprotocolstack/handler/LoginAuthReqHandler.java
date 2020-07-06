package com.hank.news.netty.privateprotocolstack.handler;

import com.hank.news.netty.privateprotocolstack.Header;
import com.hank.news.netty.privateprotocolstack.MessageType;
import com.hank.news.netty.privateprotocolstack.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.awt.*;

//netty权威指南使用的是ChannelHandlerAdapter(5.0),反正5.0版本目前不维护了。就用4.0的了
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        ctx.writeAndFlush(buildLoginReq());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage nettyMessage= (NettyMessage) msg;
        if (nettyMessage.getHeader()!=null&& nettyMessage.getHeader().getType()== MessageType.LOGIN_REQ.value){
            byte loginResult = (byte) nettyMessage.getBody();
            if (loginResult!=(byte) 0){
                //握手失败，即登录失败
                ctx.close();
            }else {
                ctx.fireChannelRead(msg);
            }
        }else {
            ctx.fireChannelRead(msg);
        }



    }

    private NettyMessage buildLoginReq (){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value);
        message.setHeader(header);
        return message;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
