package com.hank.news.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-29 13:36
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private  String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }


    @Override
    protected void channelRead0 (ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        System.out.println("begin");

        if (!request.decoderResult().isSuccess()){
              sendError(HttpResponseStatus.BAD_REQUEST,channelHandlerContext);
            System.out.println("req not normal ,code 1");
            return;
        }
        HttpMethod method = request.method();
        if (!method.name().equalsIgnoreCase(method.name())){
              sendError(HttpResponseStatus.FORBIDDEN,channelHandlerContext);
            System.out.println("req not normal ,code 2");
               return;
        }
        String uri = request.uri();
        String path=sanitizeUri(uri);
        if (path==null){
           sendError(HttpResponseStatus.FORBIDDEN,channelHandlerContext);
            System.out.println("req not normal ,code 3");
           return;
        }
        System.out.println("path"+path);
        File file = new File(path);
        if (file.isDirectory()){
            if (path.endsWith("\\")){
                sendListing(channelHandlerContext,file);
            }else {
                sendRedirect(channelHandlerContext,uri+'/');
            }
            return;
        }
        if (file.isHidden()||!file.exists()){
            sendError(HttpResponseStatus.NOT_FOUND,channelHandlerContext);
            System.out.println("req not normal ,code 5");
            return;
        }
        if (!file.isFile()){
            sendError(HttpResponseStatus.FORBIDDEN,channelHandlerContext);
            System.out.println("req not normal ,code 4");
            return;
        }



        //只读方式打开文件
        RandomAccessFile randomAccessFile = new RandomAccessFile(file,"r");

        long length = randomAccessFile.length();

        DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);

        response.headers().set("content-length",length);

        setContentTypeHeader(response,file);

        String s = request.headers().get("Connection");
        if (null!=s&&"keep-alive".equalsIgnoreCase(s)){

            response.headers().set("Connection",HttpHeaders.Values.KEEP_ALIVE);
        }
        channelHandlerContext.write(response);

        ChannelFuture sendFuture;

        sendFuture=channelHandlerContext.write(new ChunkedFile(randomAccessFile,0,length,8192),
                channelHandlerContext.newProgressivePromise());

        sendFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long progress, long total) throws Exception {
                if (total<0){
                    System.out.println("transfer progress" + progress);
                }else {
                    System.out.println("transfer progress" + progress + "/" + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                System.out.println("transfer completed");
            }
        });

        // 如果是使用chunked编码，最后需要发送一个编码结束的空消息体，标识所有的消息体已经发送完成
        ChannelFuture lastContentFuture = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        //如果不是长链接，关闭连接
        if (s==null||s.equals("")||!s.equals(HttpHeaders.Values.KEEP_ALIVE)){
              lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }

    }


    private static void  sendError(HttpResponseStatus status,ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,status,
                Unpooled.copiedBuffer("Failure:"+status.toString()+"/r/n",CharsetUtil.UTF_8));
        response.headers().set("content-type","text/plain; charset= UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void setContentTypeHeader(HttpResponse response, File file){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set("content-type",mimetypesFileTypeMap.getContentType(file.getPath()));

    }

    private static void  sendListing(ChannelHandlerContext ctx,File dir){

        DefaultFullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK);

        resp.headers().set("Content-Type","text/html;charset=UTF-8");

        StringBuilder sb = new StringBuilder();

        String path = dir.getPath();

        sb.append("<!DOCTYPE html>\r\n");
        sb.append("<html><head><title>");
        sb.append(path);
        sb.append(" 目录：");
        sb.append("</title></head><body>\r\n");
        sb.append("<h3>");
        sb.append(path).append(" 目录：");
        sb.append("<ul>");
        sb.append("<li>链接：<a href=\"../\">..</a></li>\r\n;</li>");

        for (File file : dir.listFiles()) {
            if (file.isHidden()||!file.canRead()){
                continue;
            }
            String name=file.getName();
            sb.append("<li>链接：<a href=\"");
            sb.append(name);
            sb.append("\">");
            sb.append(name);
            sb.append("</a></li>\r\n");
        }
        sb.append("</ul></body></html>\r\n");
        ByteBuf buf = Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8);

        resp.content().writeBytes(buf);

        buf.release();

        ctx.writeAndFlush(resp).addListener (ChannelFutureListener.CLOSE);

    }

    private  String sanitizeUri(String uri){
        try {
          uri=  URLDecoder.decode(uri,"utf-8");
        } catch (UnsupportedEncodingException e) {
            try {
                URLDecoder.decode(uri,"Iso-8859-1");
            } catch (UnsupportedEncodingException e1) {
                throw new Error();
            }
        }
         if (!uri.startsWith(url)){
             return null;
         }

         if (!uri.startsWith("/")){
             return null;
         }

        System.out.println(File.separator);
        uri= uri.replace('/',File.separatorChar);

         if (uri.contains("."+File.separator)||uri.contains(File.separator+".")||uri.startsWith(".")||uri.endsWith(".")){
             return null;
         }

        return System.getProperty("user.dir")+File.separator+uri;

    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
          FullHttpResponse response = new DefaultFullHttpResponse (HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
          response.headers().set("location", newUri);
              ctx.writeAndFlush(response).addListener (ChannelFutureListener.CLOSE);
             }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println("produce ex :" + cause.getMessage());

        if (ctx.channel().isActive()){
            sendError(HttpResponseStatus.INTERNAL_SERVER_ERROR,ctx);
        }

    }
}
