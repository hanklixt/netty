package com.hank.netty.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lxt
 * @date 2019-12-12-10:25
 */
public class FileChannelTest02 {
    public static void main(String[] args) throws Exception {
        final File file = new File("C:\\Users\\HASEE\\Desktop\\新建文本文档.txt");



        final FileInputStream fileInputStream = new FileInputStream(file);

        final FileChannel channel = fileInputStream.getChannel();

        final ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

        channel.read(byteBuffer);

        fileInputStream.close();

        System.out.println(new String(byteBuffer.array(), "utf-8"));
    }
}
