package com.ma.thread_pool_handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
	
	//当通道就绪就会触发该方法
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx);
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello world",CharsetUtil.UTF_8));
	}

	//当通道有读取事件时，会触发
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		System.out.println(buf.toString(CharsetUtil.UTF_8));
		System.out.println(ctx.channel().remoteAddress());
	}

	//异常处理
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
