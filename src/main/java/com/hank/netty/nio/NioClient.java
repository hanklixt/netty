package com.hank.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author lxt
 * @date 2019-12-19-14:17
 * nioClient
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        final SocketChannel socketChannel = SocketChannel.open();
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        final InetSocketAddress localhost = new InetSocketAddress("localhost", 6666);
        if (!socketChannel.connect(localhost)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("连接服务器需要时间,做些其他事");
            }
        }
        String message = "hello world";
        final ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        socketChannel.write(buffer);
        System.in.read();

    }
}
