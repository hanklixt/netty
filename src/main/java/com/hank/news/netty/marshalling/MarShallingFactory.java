package com.hank.news.netty.marshalling;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public final class MarShallingFactory {






    //反正就是提供的配置方式了
    public static MarshallingDecoder buildMarshallingDecoder() {
              final MarshallerFactory marshallerFactory = Marshalling

                .getProvidedMarshallerFactory("serial");

             final MarshallingConfiguration configuration = new MarshallingConfiguration();

            configuration.setVersion(5);

           UnmarshallerProvider provider = new DefaultUnmarshallerProvider(

                        marshallerFactory, configuration);

             MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024*1024);

            return decoder;

           }




       /**
     37.        * 创建Jboss Marshalling编码器MarshallingEncoder
     38.        *
     39.        * @return
     40.        */
             public static MarshallingEncoder buildMarshallingEncoder() {
            final MarshallerFactory marshallerFactory = Marshalling

                .getProvidedMarshallerFactory("serial");

            final MarshallingConfiguration configuration = new MarshallingConfiguration();

           configuration.setVersion(5);

           MarshallerProvider provider = new DefaultMarshallerProvider(

                     marshallerFactory, configuration);

           MarshallingEncoder encoder = new MarshallingEncoder(provider);

           return encoder;

            }


}
