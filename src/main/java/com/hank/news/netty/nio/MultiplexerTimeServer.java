package com.hank.news.netty.nio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-24 10:00
 */
public class MultiplexerTimeServer implements Runnable {


    private int port;

    private  Selector selector;

    private ServerSocketChannel socketChannel;

    private volatile boolean stop;



    public MultiplexerTimeServer(int port) {

        try {
            selector = Selector.open();
            socketChannel=ServerSocketChannel.open();
            //设置非阻塞
            socketChannel.configureBlocking(false);
            //绑定监听端口
            // backlog}参数是请求的最大数量
            //
            //套接字上的挂起连接。它的确切语义是实现
            //
            //具体而言，实现可以施加最大长度
            //
            //或者可以选择忽略参数altogther。提供的价值
            //
            //应该大于{@code 0}。如果小于或等于
            //
            //{@code 0}，则将使用特定于实现的默认值
            socketChannel.socket().bind(new InetSocketAddress(port),1024);
            //关心可连接事件
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("ready");

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.port = port;
    }


    private void stop(){
        this.stop=true;
    }


    @Override
    public void run() {

        while (!stop){
            try {
                //等待客户端连接，等待时间为1s
                /**
                 * 无论读写就绪，每隔1秒种就会被唤醒一次
                 */
                selector.select(1000);
                System.out.println("等待客户滴连接");
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handleInput(key);
                }
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        //多路复用器关闭之后，其他的也都会关闭
        if (selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void  handleInput(SelectionKey key)  {

        if (key.isValid()){
            /**
             * 通过selectKey获取网络事件的类型
             */
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                /**
                 * 通过accept接收客户端请求并且生成一个SocketChannel对象，完成此操作相当于完成了tcp的三次握手，同时
                 * 我们需要将他设置成异步非阻塞
                 */
                SocketChannel sc = null;
                try {
                    sc = ssc.accept();
                    sc.configureBlocking(false);
                    //关心可读时间
                    sc.register(selector,SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //读已就位
            if (key.isReadable()){
                SocketChannel channel = (SocketChannel) key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int read = 0;
                try {
                    read = channel.read(readBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                    key.cancel();
                    try {
                        channel.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                //反转
                readBuffer.flip();
                //读到了数据
                if (read>0){
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String s = null;
                    try {
                        s = new String(bytes, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    System.out.println("the Time hasReceive order "+s);
                    String currentTime=s.equalsIgnoreCase("QueryTime")? LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")):"bad request";
                    doWrite(channel,currentTime);
                }else if (read<0){
                    //关闭链路
                    key.cancel();
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }


    }

    /**
     * 写数据
     * @param channel
     * @param response
     */
    private void doWrite(SocketChannel channel,String response){
        try {
            channel.write(ByteBuffer.wrap(response.getBytes()));
            System.out.println("应答数据" + response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
