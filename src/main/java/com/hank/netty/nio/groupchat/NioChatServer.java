package com.hank.netty.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @author lxt
 * @date 2019-12-19-17:00
 */
public class NioChatServer {
    //定义私有属性
    private Selector selector;
    private ServerSocketChannel listen;
    private static final Integer port = 1112;

    //注册serverSocketChannel 到Selector
    public NioChatServer() {
        try {
            listen = ServerSocketChannel.open();
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(port);
            listen.socket().bind(inetSocketAddress);
            selector = Selector.open();
            listen.configureBlocking(false);
            listen.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 监听客户端消息
     */
    private void listen() {

        SocketChannel socketChannel = null;
        try {
            while (true) {
                int select = 0;
                select = selector.select(2000);
                if (!(select > 0)) {
                    continue;
                }
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                //遍历selectionKey
                final Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    final SelectionKey selectionKey = iterator.next();
                    System.out.println("selectionKeys:" + selectionKeys.size());
                    //接收事件
                    if (selectionKey.isAcceptable()) {
                        socketChannel = listen.accept();
                        //设置非阻塞
                        socketChannel.configureBlocking(false);
                        System.out.println(socketChannel.getRemoteAddress() + "上线了");
                        //设置关心事件为可读
                        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(512));
                    }
                    //读事件
                    if (selectionKey.isReadable()) {
                        readData(selectionKey);
                    }
                    iterator.remove();
                }

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            try {
                System.out.println(socketChannel.getRemoteAddress() + "下线了");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void readData(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            socketChannel.configureBlocking(false);
            final ByteBuffer buffer = ByteBuffer.allocate(512);
            final int read = socketChannel.read(buffer);
            if (read > 0) {
                final String msg = new String(buffer.array());
                //转发数据
                System.out.println("转发数据" + msg);
                sendMsgToOtherClient(socketChannel, msg);
            }
        } catch (Exception e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                selectionKey.cancel();
                socketChannel.close();

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 转发消息
     *
     * @param self
     * @param msg
     */
    private void sendMsgToOtherClient(SocketChannel self, String msg) {
        SocketChannel socketChannel = null;
        try {
            final Set<SelectionKey> keys = selector.keys();
            System.out.println(keys.size());
            for (SelectionKey key : keys) {
                final Channel channel = key.channel();
                if (channel instanceof SocketChannel && channel != self) {
                    final ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                    socketChannel = (SocketChannel) channel;
                    socketChannel.write(wrap);
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public static void main(String[] args) {

        final NioChatServer nioChatServer = new NioChatServer();
        nioChatServer.listen();
    }
}
