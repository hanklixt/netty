package com.hank.news.proto.coder;

import com.hank.news.protopojo.MsgProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author lxt
 * @create 2020-11-25 9:56
 */
public class ProtoBufDecoder  extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        //标记下当前读指针reader index的位置
        in.markReaderIndex();

        if (in.readableBytes()<2){
            //不够包头读取
            return;
        }
        //  先读取头
        int length = in.readShort();
        if (length<0){
            // 非法数据--关闭连接
            ctx.close();
        }
        if (length>in.readableBytes()){
            //可读数据长度 不满足头里面的消息长度
            in.resetReaderIndex();
            return;
        }

        //省略 --读取魔数，版本号相关的校验
        //省略 -- 读取内容
        byte[] array;

        if (in.hasArray()){
            //堆缓冲
            ByteBuf slice = in.slice();
         //不带参数的 slice 方法等同于 buf.slice(buf.readerIndex(), buf.readableBytes()) 调用, 即返回 buf 中可读部分的切片
            array=slice.array();
        }else {
            //堆外内存
            array = new byte[length];
            in.readBytes(array,0,length);
        }

        MsgProto.MsgP outMsgP = MsgProto.MsgP.parseFrom(array);

        if (outMsgP!=null){

            list.add(outMsgP);

        }

    }
}
