package com.ma.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		MessageProtocol protocol = new MessageProtocol();
		for (int i = 0; i < 4; i++) {
			String msg = "hello server "+i;
			byte[] bytes = msg.getBytes(CharsetUtil.UTF_8);
			protocol.setContent(bytes);
			protocol.setLen(bytes.length);
			ctx.writeAndFlush(protocol);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
		System.out.println(new String(msg.getContent(),CharsetUtil.UTF_8));
	}

}
