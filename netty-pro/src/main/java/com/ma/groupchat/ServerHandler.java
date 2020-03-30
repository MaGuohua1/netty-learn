package com.ma.groupchat;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

	// 定义一个channel组，管理所有的channel
	// GlobalEventExecutor全局事件执行器
	private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	// handlerAdded 表示连接建立，一旦连接，第一个被执行
	// 将当前channel加入到channelGroup
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		// 将该客户加入聊天的信息推送给其他客户
		channelGroup.writeAndFlush("[客户端： " + channel.remoteAddress() + "] 加入聊天");
		channelGroup.add(channel);
	}

	// 表示channel处于活动状态，提示xx上线了
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + "上线了");
	}

	// 表示channel处于非活动状态
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(ctx.channel().remoteAddress() + "离线了");
	}

	// 断开连接触发，将xx客户离开的信息推送给在线的客户
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		// 将该客户离开聊天的信息推送给其他客户
		channelGroup.writeAndFlush("[客户端： " + channel.remoteAddress() + "] 离开聊天");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		channelGroup.stream().forEach(ch -> {
			if (ctx.channel() != ch) {
				ch.writeAndFlush(
						"[客户端：" + ctx.channel().remoteAddress() + " " + format.format(new Date()) + "] " + msg);
			} else {
				ch.writeAndFlush(
						"[自己：" + ctx.channel().remoteAddress() + " " + format.format(new Date()) + "] " + msg);
			}
		});
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

}
