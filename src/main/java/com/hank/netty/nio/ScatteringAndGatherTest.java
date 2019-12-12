package com.hank.netty.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author lxt
 * @date 2019-12-12-16:08
 */
public class ScatteringAndGatherTest {
    public static void main(String[] args) throws Exception {
        final ServerSocketChannel socketChannel = ServerSocketChannel.open();
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(7777);
        socketChannel.bind(inetSocketAddress);
        final ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        final SocketChannel accept = socketChannel.accept();
        int readLength = 8;
        while (true) {
            long read = 0;
            while (read < readLength) {
                read = accept.read(byteBuffers);
                Arrays.asList(byteBuffers).stream().map(x ->
                        "position:" + x.position() + ",limit" + x.limit()
                ).forEach(System.out::println);
                Arrays.asList(byteBuffers).forEach(buffer -> {
                    buffer.flip();
                });
                long writeLength = 0;
                while (writeLength < readLength) {
                    writeLength = accept.write(byteBuffers);
                    System.out.println("读取到的数据长度:" + writeLength);
                    Arrays.asList(byteBuffers).forEach(byteBuffer -> System.out.println(new String(byteBuffer.array())));
                }
            }

        }


    }
}
