package com.hank.netty.nio.zerocopy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * @author lxt
 * @date 2019-12-23-17:28
 * 该类主要是练习如何使用nio零拷贝，就用单线程阻塞的方式来接受客户端消息
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //获取一个selector
        final Selector selector = Selector.open();
        //设置非阻塞
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.socket().bind(new InetSocketAddress(8744));

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        final ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (true) {
            final int select = selector.select(2000);
            if (select == 0) {
                continue;
            }

            final Set<SelectionKey> selectionKeys = selector.selectedKeys();

            selectionKeys.forEach(x -> {
                if (x.isAcceptable()) {
                    try {
                        final SocketChannel socketChannel = serverSocketChannel.accept();

                        socketChannel.configureBlocking(false);

                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (x.isReadable()) {
                    System.out.println("..............");
                    /**
                     * position = 0;
                     * mark = -1;
                     */
                    buffer.rewind();

                }

            });

        }
    }
}
