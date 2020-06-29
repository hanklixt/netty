package com.hank.news.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-24 11:37
 */
public class TimeClientHandler implements Runnable {

    private int port;

    private String host;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean stop;

    public TimeClientHandler(int port, String host,SocketChannel socketChannel ) {
        this.port = port;
        this.host = host;
        try {
            selector = Selector.open();
            this.socketChannel=socketChannel;
            socketChannel.configureBlocking(false);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
            if (!socketChannel.connect(inetSocketAddress)){
                while (!socketChannel.finishConnect()){
                    System.out.println("正在连接服务器");
                }
            }
            socketChannel.configureBlocking(false);
            socketChannel.register(selector,SelectionKey.OP_READ);
            System.out.println("客户端初始化成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!stop){
            try {
              selector.select(1000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectionKeys.iterator();
            SelectionKey key=null;
            while (it.hasNext()){
                key=it.next();
                it.remove();
                if (key.isValid()){
                    if (key.isConnectable()){
                        try {
                            if (socketChannel.finishConnect()){
                                socketChannel.register(selector,SelectionKey.OP_READ);
                                doWrite(socketChannel);
                                if (key.isReadable()){
                                    String resp="";
                                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                                    SocketChannel channel = (SocketChannel) key.channel();
                                    try {
                                        int read = channel.read(readBuffer);
                                        if (read>0){
                                            readBuffer.flip();
                                            byte[] bytes = new byte[readBuffer.remaining()];
                                            resp =new String(bytes,"utf-8");
                                        }else if (read<0){
                                            key.cancel();
                                            channel.close();
                                        }
                                        System.out.println(resp);
                                        this.stop=true;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            //进程退出
                            System.exit(1);
                        }

                    }



                }
            }
        }

    }



    public synchronized void  doWrite(SocketChannel socketChannel) throws IOException {
        String req="QueryTime";
        byte[] bytes = req.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()){
            System.out.println("send server" + req);
        }
    }

    public void doWrite(String msg) throws IOException {
        String req=msg;
        byte[] bytes = req.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()){
            System.out.println("send server" + req);
        }
    }



}
