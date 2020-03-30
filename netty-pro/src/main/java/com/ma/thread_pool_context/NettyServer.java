package com.ma.thread_pool_context;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class NettyServer {

	//创建业务线程池
	static final EventExecutorGroup group = new DefaultEventExecutorGroup(2);
	
	public static void main(String[] args) throws InterruptedException {
		//创建两个线程组bossGroup和workerGroup
		//bossGroup只是处理连接请求，真正和客户端业务处理，会交给workerGroup完成
		//两个都是无限循环
		//bossGroup和workerGroup含有的子线程（NioEventLoop）默认为 cpu核数 * 2
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {
			//创建服务器端的启动对象，配置参数
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)//设置两个线程组
					.channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器的通道实现
					.option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待连接个数
					.childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
					.childHandler(new ChannelInitializer<SocketChannel>() {

						//给pipeline设置处理器
						//在addLast()指定EventExecutorGroup，该handler会加入到该线程池中
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(group, new NettyServerHandler());
						}

					});//给我们的workerGroup的EventLoop对应的管道设置处理器
			//绑定一个端口并且同步，生成一个ChannelFuture对象
			//启动服务器
			ChannelFuture sync = serverBootstrap.bind(6666).sync();
			//对关闭通道进行监听
			sync.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
}
