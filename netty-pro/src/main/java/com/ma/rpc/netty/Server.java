package com.ma.rpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class Server {

	public static void startServer(String hostname, int port) {
		startServer0(hostname, port);
	}
	private static void startServer0(String hostname, int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)//设置两个线程组
					.channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
							pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
							pipeline.addLast(new ServerHandler());
						}

					});//给我们的workerGroup的EventLoop对应的管道设置处理器
			ChannelFuture sync = serverBootstrap.bind(hostname,port).sync();
			sync.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
