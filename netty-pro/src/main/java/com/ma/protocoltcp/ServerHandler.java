package com.ma.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

	private int count;

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
		System.out.println(new String(msg.getContent(), CharsetUtil.UTF_8));
		System.out.println("服务器接收到的消息量=" + (++this.count));
		MessageProtocol protocol = new MessageProtocol();
		byte[] content = "你好，客户端……".getBytes(CharsetUtil.UTF_8);
		int len=content.length;
		protocol.setLen(len);
		protocol.setContent(content);
		ctx.writeAndFlush(protocol);
	}

}
