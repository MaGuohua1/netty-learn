package com.ma.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author mgh_2
 *
 * @desription 我们自定义一个Handler，需要继承netty规定好的HandlerAdapter
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	// 读取数据消息（这里我们可以读取客户端发送的消息）
	/*
	1、 ChannelHandlerContext：上下文对象，含有管道pipeline，通道channel，地址
	2、Object msg:就是客户端发送的数据，默认Object
	*/
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		UserPOJO.User user = (UserPOJO.User) msg;
		System.out.println("客户端数据："+user.getId()+":"+user.getName());
	}

	// 数据读取完毕
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// writeAndFlush 是write + flush
		// 将数据写入到缓存，并刷新
		// 一般将，我们对这个发送的数据进行编码
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));
	}

	//处理异常，一般是需要关闭通道
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
