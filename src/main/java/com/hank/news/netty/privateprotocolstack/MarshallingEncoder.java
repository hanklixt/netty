package com.hank.news.netty.privateprotocolstack;


import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

import java.io.IOException;


public class MarshallingEncoder {

    private static final byte[] length_placeholder=new byte[4];

    private Marshaller  marshaller;

    public MarshallingEncoder() throws IOException {
    this.marshaller= MarshallingCodeCFactory.buildMarshalling();
    }


    protected  void encode(Object msg, ByteBuf out) throws IOException {

        //返回可写的第一个数组索引
        int lengthPos = out.writerIndex();

        out.writeBytes(length_placeholder);

        ChannelBufferByteOutPut outPut = new ChannelBufferByteOutPut(out);

        try {
            marshaller.start(outPut);

            marshaller.writeObject(msg);

            marshaller.finish();

            //当前可写的索引-之前可写的数组索引-4，这里应该是发送n个字节，这里应该是标记，
            // 此方法不修改writerIndex和readerIndex
            out.setInt(lengthPos,out.writerIndex()-lengthPos-4);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            marshaller.close();
        }

    }




}
