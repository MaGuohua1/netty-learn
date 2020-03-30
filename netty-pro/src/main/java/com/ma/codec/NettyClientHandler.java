package com.ma.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	//当通道就绪就会触发该方法
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		UserPOJO.User user = UserPOJO.User.newBuilder().setId(11).setName("haha").build();
		ctx.writeAndFlush(user);
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
