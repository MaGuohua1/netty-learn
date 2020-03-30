package com.ma.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class Server {

	private int port;

	public Server(int port) {
		this.port = port;
	}

	private void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)// 设置两个线程组
					.channel(NioServerSocketChannel.class)// 使用NioServerSocketChannel作为服务器的通道实现
					.option(ChannelOption.SO_BACKLOG, 128)// 设置线程队列等待连接个数
					.childOption(ChannelOption.SO_KEEPALIVE, true)// 设置保持活动连接状态
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
							pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
							pipeline.addLast(new ServerHandler());
						}
					});// 给我们的workerGroup的EventLoop对应的管道设置处理器
			ChannelFuture sync = serverBootstrap.bind(port).sync();
			sync.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) {
		new Server(10000).run();
	}
}
