package com.ma.rpc.netty;

import java.util.concurrent.Callable;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter implements Callable<String> {

	private ChannelHandlerContext context;
	private String result;
	private String param;

	/*step1*/
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		context = ctx;
	}
	
	/*step4*/
	@Override
	public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		result = msg.toString();
		notifyAll();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	/*step3*/
	@Override
	public synchronized String call() throws Exception {
		context.writeAndFlush(param);
		if (result == null) {
			wait();
		}
		return result;
	}

	/*step2*/
	public void setParam(String param) {
		this.param = param;
	}

}
