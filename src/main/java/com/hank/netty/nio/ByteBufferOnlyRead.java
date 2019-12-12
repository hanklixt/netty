package com.hank.netty.nio;

import java.nio.ByteBuffer;

/**
 * @author lxt
 * @date 2019-12-12-14:56
 * 设置ByteBuffer为只读
 */
public class ByteBufferOnlyRead {
    public static void main(String[] args) {
        final ByteBuffer byteBuffer = ByteBuffer.allocate(36);

        //一个int 4个字节，36字节刚好，再放存不了了
        for (int i = 1; i < 10; i++) {
            byteBuffer.putInt(i);
        }

        byteBuffer.flip();

        final ByteBuffer byteBuffer1 = byteBuffer.asReadOnlyBuffer();

        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.getInt());
        }

        //  bytebuffer1设成只读后，不可再进行写入
        byteBuffer1.put("1".getBytes());


    }
}
