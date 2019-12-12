package com.hank.netty.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lxt
 * @date 2019-12-12-10:00
 * 使用fileChannel,ByteBuffer写入文件
 */
public class FileChannelTest01 {
    public static void main(String[] args) throws Exception {

        String str = "hello world";
        final FileOutputStream fos = new FileOutputStream("E:\\file01.txt");

        final FileChannel fileChannel = fos.getChannel();

        //申请1024个byte
        final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        //放入字节
        byteBuffer.put(str.getBytes("utf-8"));
        //byteBuffer反转为可读
        byteBuffer.flip();

        //写入到fileChannel
        fileChannel.write(byteBuffer);


        fos.close();


    }
}
