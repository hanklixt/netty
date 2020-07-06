package com.hank.news.netty.privateprotocolstack.handler;

import com.hank.news.netty.privateprotocolstack.Header;
import com.hank.news.netty.privateprotocolstack.MessageType;
import com.hank.news.netty.privateprotocolstack.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

    private Map<String,Boolean> nodeCheck=new ConcurrentHashMap<String,Boolean>();

    private String[]  ipWhiteList={"127.0.0.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       NettyMessage message= (NettyMessage) msg;
       if (message.getHeader()!=null&&message.getHeader().getType()== MessageType.LOGIN_REQ.value){
           //1.判断请求节点是否已经登录过了
           String remoteAddress = ctx.channel().remoteAddress().toString();
           NettyMessage loginResp=null;
           if (nodeCheck.containsKey(remoteAddress)){
                //非0就告诉他登录失败了，属于重复登陆
               loginResp = buildNettyMessage((byte) -1);
           }else {
            InetSocketAddress address= (InetSocketAddress) ctx.channel().remoteAddress();
               String hostAddress = address.getAddress().getHostAddress();
               //ip 白名单
               boolean isWhite=false;
               for (String wip:ipWhiteList){
                   if (wip.equalsIgnoreCase(hostAddress)){
                       isWhite=true;
                       break;
                   }
               }
               loginResp = isWhite ? buildNettyMessage((byte) 0) : buildNettyMessage((byte) -1);
               if (isWhite){
                   nodeCheck.put(remoteAddress,true);
               }
           }
           System.out.println("send response " + loginResp.toString());
           ctx.writeAndFlush(loginResp);
       }else {
           ctx.fireChannelRead(msg);
       }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    private NettyMessage buildNettyMessage(byte by){
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value);
        message.setHeader(header);
        message.setBody(by);
        return message;
    }



}
