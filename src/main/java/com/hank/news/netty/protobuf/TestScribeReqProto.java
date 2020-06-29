package com.hank.news.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @auther 李先涛
 * @create 2020-06-29 9:42
 */
public class TestScribeReqProto {


    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq subscribeReq(){

        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqId(1);
        builder.setUserName("Lilinfeng");
        builder.setProduceName("Netty Book");
        List<String> objects = new ArrayList<>();
        objects.add("hu kuang");
        objects.add("bei jing");
        objects.add("cheng shi");
        builder.addAllAddress(objects);
        return builder.build();

    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq subscribeReq = TestScribeReqProto.subscribeReq();
        System.out.println(subscribeReq.toString());
        SubscribeReqProto.SubscribeReq decode = decode(encode(subscribeReq));
        System.out.println(decode.toString());

        System.out.println(decode.equals(subscribeReq));
    }



}
