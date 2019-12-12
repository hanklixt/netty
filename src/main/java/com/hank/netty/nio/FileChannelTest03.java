package com.hank.netty.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lxt
 * @date 2019-12-12-11:03
 */
public class FileChannelTest03 {
    public static void main(String[] args) throws Exception {

        //打开输入流
        final FileInputStream fileInputStream = new FileInputStream("1.txt");

        final FileChannel channel = fileInputStream.getChannel();
        //打开输出流
        final FileOutputStream fileOutputStream = new FileOutputStream("2.txt");

        final FileChannel channel1 = fileOutputStream.getChannel();

        final ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true) {
            /**
             * public final Buffer clear() {
             *         position = 0;
             *         limit = capacity;
             *         mark = -1;
             *         return this;
             *     }
             *     此步做以上操作，避免read读不到-1
             */
            byteBuffer.clear();
            final int read = channel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            byteBuffer.flip();

            channel1.write(byteBuffer);


        }
        fileInputStream.close();
        fileOutputStream.close();

    }
}
