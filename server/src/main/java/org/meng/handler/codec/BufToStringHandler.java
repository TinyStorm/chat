package org.meng.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * ctx.write() ctx.channel().write()  前者从当前Handler向前遍历,如果有合适的Handler则进,如果没有则不进,后者  从所有Handler里边遍历
 */
public class BufToStringHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        String msg = new String(data);
        out.add(msg);
//        System.out.println("server receive:" + msg);
//        ctx.writeAndFlush(msg);

    }
}
