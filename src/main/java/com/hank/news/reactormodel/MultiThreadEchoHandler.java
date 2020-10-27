package com.hank.news.reactormodel;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.lang.String;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lxt
 * @create 2020-10-27 16:34
 */
public class MultiThreadEchoHandler implements Runnable {

    final SocketChannel socketChannel;

    final SelectionKey selectionKey;

    final ByteBuffer bf=ByteBuffer.allocate(1024);

    static final int RECEIVING = 0, SENDING = 1;
    int state = RECEIVING;
    //引入线程池
    static ExecutorService executors=Executors.newFixedThreadPool(4);

    public MultiThreadEchoHandler(SocketChannel socketChannel, Selector selector) throws Exception {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        //先获取selectionKey,后面再注册事件
        selectionKey = socketChannel.register(selector, 0);
        selectionKey.attach(this);
        selectionKey.interestOps(SelectionKey.OP_READ);
        //如果selector 线程阻塞 ，将其唤醒
        selector.wakeup();
    }

    //-------------- run 方法
    @Override
    public void run() {
        CompletableFuture.runAsync(this::asyncRun,executors);
    }

    public synchronized void asyncRun(){
        try{
            if (state==SENDING){

                socketChannel.write(bf);

                //写完后，清空一下
                bf.clear();
                //读完切换下关心的事件
                selectionKey.interestOps(SelectionKey.OP_READ);

                state=RECEIVING;

            }else if (state==RECEIVING){
                int length=0;
                while ((length=socketChannel.read(bf))>0){
                    System.out.println(new String(bf.array(), 0, length));
                }
                //读完后，反转一下
                bf.flip();
                //注册write就绪事件
                selectionKey.interestOps(SelectionKey.OP_WRITE);
                //读完后，切换一下状态
                state=SENDING;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
