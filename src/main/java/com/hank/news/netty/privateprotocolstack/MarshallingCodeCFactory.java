package com.hank.news.netty.privateprotocolstack;

import org.jboss.marshalling.Marshaller;
import org.jboss.marshalling.MarshallingConfiguration;
import org.jboss.marshalling.Unmarshaller;
import org.jboss.marshalling.serial.SerialMarshallerFactory;
import java.io.IOException;
public final class MarshallingCodeCFactory {


    public static Marshaller buildMarshalling() throws IOException {

        MarshallingConfiguration configuration = new MarshallingConfiguration();

        configuration.setVersion(5);

        SerialMarshallerFactory serialMarshallerFactory = new SerialMarshallerFactory();

        return serialMarshallerFactory.createMarshaller(configuration);
    }

    public static Unmarshaller buildUnmarshaller() throws IOException {

        MarshallingConfiguration configuration = new MarshallingConfiguration();

        configuration.setVersion(5);

        SerialMarshallerFactory serialMarshallerFactory = new SerialMarshallerFactory();

        return serialMarshallerFactory.createUnmarshaller(configuration);


    }


}
