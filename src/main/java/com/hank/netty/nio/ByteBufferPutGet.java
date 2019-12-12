package com.hank.netty.nio;

import java.nio.ByteBuffer;

/**
 * @author lxt
 * @date 2019-12-12-14:51
 * buffer的存取顺序
 */
public class ByteBufferPutGet {
    public static void main(String[] args) {

        final ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        byteBuffer.putInt(10);
        byteBuffer.putLong(20L);
        byteBuffer.putShort((short) 2);
        byteBuffer.putChar('1');

        byteBuffer.flip();

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getShort());
        System.out.println(byteBuffer.getChar());
    }
}
