package com.hank.netty.nio.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.time.Duration;
import java.time.Instant;

/**
 * @author lxt
 * @date 2019-12-23-17:29
 */
public class NioClient {

    static String fileName = "F:\\User\\goolechrome\\download\\Navicat+for+MySQL破解版.zip";

    public static void main(String[] args) throws Exception {
        final Instant begin = Instant.now();
        final SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress("localhost", 8744));
        while (!socketChannel.finishConnect()) {
            System.out.println("正在重试连接服务器，请稍等");
        }
        final FileInputStream fileInputStream = new FileInputStream(fileName);
        final FileChannel filechannel = fileInputStream.getChannel();
        /**
         * linux上可以一次读取完成，windows上最多可以一次读取8m，这里计算要分次读取的次数。分段读取
         */
        final int num = 8 * 1024 * 1024;    //8M数据的字节数
        //通道里文件的字节数
        final long size = filechannel.size();
        System.out.println("通道数据大小" + size);
        //要分次读取的次数
        final long count = size / num;
        //初始读取的数据大小
        long transferToCount = 0; //这样可以把数据读完
        for (int i = 0; i < count; i++) {
            System.out.println("这是第" + (i + 1) + "次");
            transferToCount = filechannel.transferTo(transferToCount, num, socketChannel);
        }

        final Instant end = Instant.now();
        System.out.println("测试耗时零拷贝" + Duration.between(begin, end).toMillis());

        System.in.read();

    }
}
