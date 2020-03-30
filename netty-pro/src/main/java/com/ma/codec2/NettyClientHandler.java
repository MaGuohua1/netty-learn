package com.ma.codec2;

import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

	//当通道就绪就会触发该方法
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		int random = new Random().nextInt(3);
		DataInfo.Mymessage mymessage = null;
		if (random == 0) {
			mymessage = DataInfo.Mymessage.newBuilder()
				.setDataType(DataInfo.Mymessage.DateType.UserType)
				.setUser(DataInfo.User.newBuilder().setId(22).setName("哈哈").build()).build();
		}
		if (random == 1) {
			mymessage = DataInfo.Mymessage.newBuilder()
					.setDataType(DataInfo.Mymessage.DateType.WorkerType)
					.setWorker(DataInfo.Worker.newBuilder().setName("fff").setAge(23).build()).build();
		}
		ctx.writeAndFlush(mymessage);
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
