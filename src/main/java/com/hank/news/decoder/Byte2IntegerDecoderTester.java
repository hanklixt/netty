package com.hank.news.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author lxt
 * @create 2020-11-02 10:38
 */
@Slf4j
public class Byte2IntegerDecoderTester {

    @Test
    public void  testByteToIntegerDecoder(){
        ChannelInitializer<EmbeddedChannel> initializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel embeddedChannel) throws Exception {
                embeddedChannel.pipeline()
                        .addLast(new ByteToIntegerDecoder());
                embeddedChannel.pipeline()
                        .addLast(new IntegerProcessHandler());
            }
        };

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(initializer);

        for (int i=0;i<100;i++){
            ByteBuf bf = Unpooled.buffer();
            bf.writeInt(i);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                log.error(e.getMessage());
            }
            embeddedChannel.writeInbound(bf);
        }

    }


}

