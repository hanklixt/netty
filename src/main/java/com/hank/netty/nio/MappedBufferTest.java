package com.hank.netty.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author lxt
 * @date 2019-12-12-15:28
 * 使用MappedBuffer直接在堆外内存修改文件
 */
public class MappedBufferTest {
    public static void main(String[] args) throws Exception {
        //以读写方式打开文件
        final RandomAccessFile rw = new RandomAccessFile("1.txt", "rw");

        final FileChannel channel = rw.getChannel();
        /**
         * 参数说明
         * 1.指定文件模式
         * 2.指定可以修改文件开始位置
         * 3.指定可以修改文件结束标记位(不包含5)
         */
        final MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(3, (byte) 'w');

        rw.close();

    }
}
