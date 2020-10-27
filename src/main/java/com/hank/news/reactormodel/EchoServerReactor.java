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

/**
 * @author lxt
 * @create 2020-10-10 11:17
 */
public class EchoServerReactor implements Runnable {

    Selector selector;
    ServerSocketChannel serverSocketChannel;

    EchoServerReactor() throws IOException {
        //Reactor 初始化
        selector=Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(5223);
        serverSocketChannel.socket().bind(inetSocketAddress);
        //绑定端口
        serverSocketChannel.configureBlocking(false);
        //注册可接受事件
        AcceptHandler handler = new AcceptHandler();
        System.out.println(handler);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,handler);
        //attch callback object,AcceptHandler
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.interrupted()){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey sk = iterator.next();
                dispatch(sk);
            }
            selectionKeys.clear();
        }

    }

    private void  dispatch(SelectionKey selectionKey){
        //
        AcceptHandler handler =(AcceptHandler) selectionKey.attachment();
        if (handler!=null){
            //这里重复调用run方法，注意是一个线程
            handler.run();
        }
    }

    class  AcceptHandler implements Runnable{

         @SneakyThrows
         @Override
         public void run() {
             //
             System.out.println("AcceptHandler");
             SocketChannel channel = serverSocketChannel.accept();
             System.out.println(channel);
             if (channel!=null){
               new EchoHandler(selector,channel).run();
             }

         }
     }

    public static void main(String[] args) {
        try{
           new Thread(new EchoServerReactor()).start(); ;
        }catch (Exception e){
            e.printStackTrace();
        }

    }







}
