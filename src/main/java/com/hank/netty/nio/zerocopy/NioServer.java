package com.hank.netty.nio.zerocopy;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author lxt
 * @date 2019-12-23-17:28
 * 该类主要是练习如何使用nio零拷贝，就用单线程阻塞的方式来接受客户端消息
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.socket().bind(new InetSocketAddress(8744));
        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (true) {
            final SocketChannel socketChannel = serverSocketChannel.accept();
            int read = 0;
            while (-1 != read) {
                read = socketChannel.read(buffer);
            }
            /**
             * position = 0;
             * mark = -1;
             */
            buffer.rewind();

        }
    }
}
