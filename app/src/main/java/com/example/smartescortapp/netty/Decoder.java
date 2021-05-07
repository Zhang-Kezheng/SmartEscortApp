package com.example.smartescortapp.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author 章可政
 * @date 2021/4/25
 */
public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] data=new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        Message message = Message.getInstance(data);
        if (message!=null)
        list.add(message);
    }

}
