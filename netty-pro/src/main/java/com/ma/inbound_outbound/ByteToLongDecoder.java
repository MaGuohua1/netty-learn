package com.ma.inbound_outbound;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ByteToLongDecoder extends ByteToMessageDecoder {

	/**
	 * 会根据接收的数据，被调用多次，直到确定没有新的元素被添加到list，或者是byteBuf没有更多的可读字节为止
	 * 如果list out不为空，就会将list的内容传递给下一个channelInboundhandler处理，该处理器的方法也会被调用多次
	 * 
	 * @param ctx 上下文对象
	 * @param in 入站的ByteBuf
	 * @param out List集合，将解码后的数据传给下一个inboundHandler
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("decoder");
		//因为long8个字节，需要判断有8个字节，才能读取一个long
		if (in.readableBytes()>=8) {
			out.add(in.readLong());
		}
	}

}
