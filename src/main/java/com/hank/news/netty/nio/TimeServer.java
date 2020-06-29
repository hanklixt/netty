package com.hank.news.netty.nio;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-24 9:59
 */
public class TimeServer {

    public static void main(String[] args) {

        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(6000);

        new Thread(timeServer,"t1").start();

    }

}
