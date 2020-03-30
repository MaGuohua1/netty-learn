package com.ma.protocoltcp;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class MessageDecoder extends ReplayingDecoder<MessageProtocol> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println("decoder 方法被调用");
		int length = in.readInt();
		byte[] content = new byte[length];
		in.readBytes(content);
		MessageProtocol protocol = new MessageProtocol();
		protocol.setLen(length);
		protocol.setContent(content);
		out.add(protocol);
	}

}
