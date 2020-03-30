package com.ma.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class Server {

	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).handler(new LoggingHandler())
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// 加入一个netty提供IdleStateHandler
							/*
							 Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
								read, write, or both operation for a while.
								客户id状态事件触发器：当一个信道没有处理读、写或读写操作时之执行。
							 1、IdleStateHandler是netty提供的处理空闲状态的处理器
							 2、readerIdleTime：表示多长事件没有读，就会发送一个心跳检测包，检测是否是否连接
							 3、writerIdleTime：表示多长事件没有写，就会发送一个心跳检测包，检测是否是否连接
							 4、allIdleTime：表示多长事件没有写，就会发送一个心跳检测包，检测是否是否连接
							 5、当idleStateEvent：触发后，就会传递给管道的下一个handler去处理，通过调用（触发）下一个handler
							 的userEventTiggered，在该方法中去处理
							 */
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new IdleStateHandler(3, 5, 7));
							//加入一个对空闲检测进一步处理的handler（自定义）
							pipeline.addLast(new HeartBeatHandler());
						}
					});
			ChannelFuture future = bootstrap.bind(9999).sync();
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
