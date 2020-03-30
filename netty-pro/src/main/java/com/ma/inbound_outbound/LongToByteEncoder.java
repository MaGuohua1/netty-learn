package com.ma.inbound_outbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class LongToByteEncoder extends MessageToByteEncoder<Long> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
		
		System.out.println("encoder"+msg);
		out.writeLong(msg);
	}

}
