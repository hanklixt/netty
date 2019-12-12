package com.hank.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author lxt
 * @date 2019-12-12-11:26
 */
public class FileChannelTest04 {
    public static void main(String[] args) throws Exception {

        final FileInputStream fileInputStream = new FileInputStream("1.jpg");

        final FileChannel srcChannel = fileInputStream.getChannel();

        final FileOutputStream fileOutputStream = new FileOutputStream("2.jpg");

        final FileChannel dstChannel = fileOutputStream.getChannel();

        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

        fileInputStream.close();
        fileOutputStream.close();

    }

}
