package com.ma.rpc.netty;

import com.ma.rpc.provider.HelloServiceImpl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
		String str = msg.toString();
		if (str.startsWith("HelloService#hello#")) {
			String result = new HelloServiceImpl().hello(str.substring(str.lastIndexOf("#")+1));
			ctx.writeAndFlush(result);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
