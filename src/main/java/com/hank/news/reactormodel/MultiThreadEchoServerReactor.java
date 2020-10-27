package com.hank.news.reactormodel;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lxt
 * @create 2020-10-27 15:08
 */
public class MultiThreadEchoServerReactor {

    ServerSocketChannel serverSocket;

    AtomicInteger next=new AtomicInteger(0);

    Selector[] selectors=new Selector[2];

    SubReactor[] subReactors=null;


    public MultiThreadEchoServerReactor() throws IOException {
        //初始化多个selector选择器
        selectors[0]=Selector.open();
        selectors[1]=Selector.open();

        serverSocket=ServerSocketChannel.open();

        InetSocketAddress address = new InetSocketAddress(8774);

        serverSocket.bind(address);
        //设置为非阻塞
        serverSocket.configureBlocking(false);

        //第一个selector注册为关心连接事件
        SelectionKey sk = serverSocket.register(selectors[0], SelectionKey.OP_ACCEPT);

        sk.attach(new AcceptorHandler());

        //第一个子反应器--

        SubReactor subReactor = new SubReactor(selectors[0]);

        //第二个子反应器
        SubReactor subReactor1 = new SubReactor(selectors[1]);

        subReactors=new SubReactor[]{subReactor,subReactor1};

    }

    private void startService(){
        new Thread(subReactors[0]).start();
        new Thread(subReactors[1]).start();
    }


    //反应器
    class SubReactor implements Runnable{

       final Selector selector;

       public SubReactor(Selector selector){
           this.selector=selector;
       }



        @SneakyThrows
        @Override
        public void run() {
           while (!Thread.interrupted()){
               selector.select();
               Set<SelectionKey> sks = selector.selectedKeys();
               Iterator<SelectionKey> iterator = sks.iterator();
               while (iterator.hasNext()){
                   SelectionKey sk = iterator.next();
                   dispatch(sk);
               }
               sks.clear();
           }

        }


        private void dispatch(SelectionKey sk){
            Runnable handler = (Runnable)sk.attachment();
            if (handler!=null){
               handler.run();
            }

        }

    }

    class  AcceptorHandler implements Runnable{

        @SneakyThrows
        @Override
        public void run() {
            SocketChannel channel = serverSocket.accept();
            if (channel!=null){

                MultiThreadEchoHandler multiThreadEchoHandler = new MultiThreadEchoHandler(channel,selectors[next.get()]);

                if (next.incrementAndGet()==selectors.length){
                    next.set(0);
                }

            }

        }
    }

    public static void main(String[] args) throws IOException {
        new MultiThreadEchoServerReactor().startService();
    }
}
