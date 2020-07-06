package com.hank.news.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-30 15:42
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {


    private static final Logger logger=Logger.getLogger(WebSocketServerHandler.class.getName());

    private  WebSocketServerHandshaker handShaker;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object object) throws Exception {

        System.out.println("begin");
        if (object instanceof FullHttpRequest){
            handleHttpRequest(channelHandlerContext, (FullHttpRequest) object);
        }else if (object instanceof  WebSocketFrame){
            handleWebSocketFrame(channelHandlerContext, (WebSocketFrame) object);
        }

    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //处理http请求
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req){
        if (!req.decoderResult().isSuccess()||!"websocket".equals(req.headers().get("Upgrade"))){
            sendResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket", null, false);
        handShaker = wsFactory.newHandshaker(req);
        if (handShaker==null){
          WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else {
            //建立连接,这里会动态设置编解码方式,同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中
            handShaker.handshake(ctx.channel(),req);
        }

    }

    //处理websocket
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){
        //判断是否是关闭链路的指令
        if (frame instanceof CloseWebSocketFrame){
            handShaker.close(ctx.channel(),((CloseWebSocketFrame) frame).retain());
            return;
        }
        //判断是否是ping消息
        if (frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //本例程只支持文本类型
        if (!(frame instanceof TextWebSocketFrame)){
            String format = String.format("%s不允许的frame类型", frame.getClass().getName());
            System.out.println(format);
            throw new UnsupportedOperationException(String.format("%s不允许的frame类型",format));
        }
        String request = ((TextWebSocketFrame) frame).text();

        if (logger.isLoggable(Level.FINE)){
            logger.fine(String.format("%s receive %s",ctx.channel(),request));
        }
        System.out.println(ctx.channel() + "receive" + request);
        ctx.channel().write(new TextWebSocketFrame(request+"欢迎使用netty时刻查询:"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    private void sendResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp){
        if (resp.status().code()!=200){
            //设置response-content 和 设置 response content-length
            ByteBuf buf = Unpooled.copiedBuffer(resp.status().toString(), CharsetUtil.UTF_8);
            resp.content().writeBytes(buf);
            buf.release();
            setContentLength(resp,resp.content().readableBytes());
        }
        ChannelFuture f = ctx.channel().writeAndFlush(resp);
        //不是长链接关闭链接
        if (!keepAlive(req)||resp.status().code()!=200){
            f.addListener(ChannelFutureListener.CLOSE);
       }


    }

    private void  setContentLength(FullHttpResponse resp,long length){
        resp.headers().set("content-length",length);
    }

    //是否是长链接
    private boolean keepAlive(FullHttpRequest request){
        String connection = request.headers().get("Connection");
        if (null==connection||"".equals(connection)){
            return false;
        }else if("keep-alive".equalsIgnoreCase(connection)){
            return true;
        }
        return false;
    }


}
