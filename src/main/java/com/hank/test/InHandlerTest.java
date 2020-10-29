package com.hank.test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

/**
 * @author lxt
 * @create 2020-10-28 15:40
 */
public class InHandlerTest {

    @Test
   public void   testInHandlerLifeCircle(){
        InHandlerDemo inHandlerDemo = new InHandlerDemo();

        ChannelInitializer<EmbeddedChannel> initializer = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) throws Exception {
               ch.pipeline().addLast(inHandlerDemo);
            }
        };

        //netty 提供的专门用来测试的channel
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(initializer);

        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(1);
        //模拟入站
        embeddedChannel.writeInbound(buffer);

        embeddedChannel.flush();
        //模拟入站
        embeddedChannel.writeInbound(buffer);
        try{

            Thread.sleep(6000);
        }catch (Exception e){

        }


    }

}
