package com.hank.news.netty.privateprotocolstack;


import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

//这个类直接复制netty包里面的
public class ChannelBufferByteOutPut implements ByteOutput {


       private  ByteBuf buffer;

       public  ChannelBufferByteOutPut(ByteBuf buffer) {
            this.buffer = buffer;
        }

        public void close() throws IOException {
        }

        public void flush() throws IOException {
        }

        public void write(int b) throws IOException {
            this.buffer.writeByte(b);
        }

        public void write(byte[] bytes) throws IOException {
            this.buffer.writeBytes(bytes);
        }

        public void write(byte[] bytes, int srcIndex, int length) throws IOException {
            this.buffer.writeBytes(bytes, srcIndex, length);
        }

        ByteBuf getBuffer() {
            return this.buffer;
        }



}
