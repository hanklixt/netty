package com.hank.news.netty.nio;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-24 11:35
 */
public class TimeClient {
    public static void main(String[] args) throws IOException {
        SocketChannel open = SocketChannel.open();
        TimeClientHandler timeClientHandler = new TimeClientHandler(6000, "127.0.0.1", open);
        new Thread(timeClientHandler).start();
    }
}
