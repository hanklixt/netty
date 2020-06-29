package com.hank.news.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-23 11:46
 */
public class TimeServer {

    public static void main(String[] args)  {
        int port=6111;
        ServerSocket serverSocket=null ;
        Socket socket=null;
        try {
            serverSocket = new ServerSocket(port);
            while (true){
                //这里会一直阻塞，直到接收的client的请求
                socket=serverSocket.accept();
                //接收到client信息后，开启一个线程进行处理
//                new Thread(new TimeHandler(socket)).start();
              new  TimeHandler(socket).run();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
