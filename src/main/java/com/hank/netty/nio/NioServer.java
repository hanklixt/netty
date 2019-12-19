package com.hank.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lxt
 * @date 2019-12-19-11:41
 * 事件驱动nioServer
 */
public class NioServer {
    public static void main(String[] args) throws Exception {
        //获取一个ServerSocketChannel对象
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //获取一个Selector对象
        final Selector selector = Selector.open();
        //绑定端口6666
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        serverSocketChannel.configureBlocking(false);
        //关心的事件为可读
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //轮询监听客户端请求
        while (true) {
            //如果selector没有事件
            if (selector.select(1000) == 0) {
                System.out.println("等待1s,没有事件");
                continue;
            }
            //获取selectionKey
            final Set<SelectionKey> selectionKeys = selector.selectedKeys();
            final Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                final SelectionKey key = keyIterator.next();
                //如果有isAccpet事件
                if (key.isAcceptable()) {
                    //accept方法虽然是阻塞的。但是上面已经做了判断，所以不会阻塞
                    //获取一个socketChannel,new SocketChannelImpl
                    final SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);
                    System.out.println("收到客户端连接" + socketChannel.hashCode());
                    //设置当前key对应的通道事件为可读
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //如果有可读事件
                if (key.isReadable()) {
                    //获取socketChannel
                    final SocketChannel socketChannel = (SocketChannel) key.channel();
                    //获取当前通道绑定的buffer
                    final ByteBuffer buffer = (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    System.out.println(new String(buffer.array(), "utf-8"));
                }

                //读完之后删除该selectionKey
                keyIterator.remove();

            }

        }

    }
}
