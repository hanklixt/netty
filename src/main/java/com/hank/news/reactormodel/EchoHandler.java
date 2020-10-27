package com.hank.news.reactormodel;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author lxt
 * @create 2020-10-10 14:36
 */
public class EchoHandler implements Runnable {

    final SocketChannel socketChannel;

    final SelectionKey sk;
    final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    static final int RECIEVING = 0, SENDING = 1;

    int state = RECIEVING;

    EchoHandler(Selector selector, SocketChannel channel) throws IOException {
        socketChannel=channel;
        //取得selectKey,后面再注册事件

        channel.configureBlocking(false);

        sk = channel.register(selector, 0);

        //注册读事件
        sk.interestOps(SelectionKey.OP_READ);

        //唤醒slector...
        selector.wakeup();
    }

    @SneakyThrows
    @Override
    public void run() {
        if (state==SENDING){
            socketChannel.write(byteBuffer);
            //写完后，开始准备从通道读
            byteBuffer.clear();
            sk.interestOps(SelectionKey.OP_READ);
            state=RECIEVING;
        }else if (state==RECIEVING){
            int length=0;
           while ((length=socketChannel.read(byteBuffer))>0){
               System.out.println(new String(byteBuffer.array(),0,length));
           }
           //读完后反转
           byteBuffer.flip();
           sk.interestOps(SelectionKey.OP_WRITE);
           state=SENDING;

        }
//        需要重复使用
//        sk.cancel();

    }

}
