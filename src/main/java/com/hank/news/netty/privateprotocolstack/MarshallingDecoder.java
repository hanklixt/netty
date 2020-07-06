package com.hank.news.netty.privateprotocolstack;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

public class MarshallingDecoder {

    private Unmarshaller unmarshaller;

    public MarshallingDecoder() {
        try {
           this.unmarshaller = MarshallingCodeCFactory.buildUnmarshaller();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Object decode(ByteBuf in) throws Exception {

        //第一个int是object对象长度，编码时设置的，同时这个方法会在缓冲区将readerIndex加4
        int objectSize = in.readInt();

        ByteBuf slice = in.slice(in.readerIndex(), objectSize);

        ByteInput input = new ChannelBufferByteInput(slice);

        try {
            unmarshaller.start(input);

            Object o = unmarshaller.readObject();

            unmarshaller.finish();

            //不读Object byte长度
            in.readerIndex(in.readerIndex()+objectSize);

            return o;

        } finally {
            try {
                unmarshaller.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }


}
