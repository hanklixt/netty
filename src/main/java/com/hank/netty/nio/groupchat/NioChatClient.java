package com.hank.netty.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author lxt
 * @date 2019-12-20-9:26
 */
public class NioChatClient {
    private Selector selector;
    private SocketChannel socketChannel;
    private String userName;
    private Integer PORT = 6000;

    //客户端初始化工作
    public NioChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open();
        final InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", PORT);
        if (!socketChannel.connect(serverAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("正在连接服务器，请稍等");
            }
        }
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        userName = socketChannel.getRemoteAddress() + "::";
        System.out.println("客户端初始化成功");
        sendMsg("hahah");
    }

    /**
     * 发送消息
     *
     * @param info
     */
    public void sendMsg(String info) {
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
            System.out.println("sendMsg" + socketChannel.getRemoteAddress() + info);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    //发送消息
    public void readMessage() throws IOException {
        while (true) {
            final int readSelector = selector.select(2000);
            //有通道可以读取
            if (readSelector > 0) {
                final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        final SocketChannel socketChannel = (SocketChannel) key.channel();
                        final ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);
                        System.out.println(socketChannel.getRemoteAddress() + "说:" + new String(buffer.array()));
                    }
                    iterator.remove();
                }
            } else {
//                System.out.println("没有可以使用的通道");
            }
        }

    }

    public static void main(String[] args) throws Exception {
        final NioChatClient nioChatClient = new NioChatClient();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        nioChatClient.readMessage();
                        Thread.sleep(3000);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            final String msg = scanner.nextLine();
            nioChatClient.sendMsg(msg);
        }
    }


}
