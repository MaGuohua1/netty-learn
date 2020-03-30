package com.ma.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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
							//因为基于http协议，使用http的编码和解码
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new HttpServerCodec());
							//是以块为基础，添加ChunkedWriteHandler处理器
							pipeline.addLast(new ChunkedWriteHandler());
							/*
							 1、http数据在传输过程中是分段，HttpObjectAggregator，就是可以将多个段聚合
							 2、这就是为什么，当浏览器发送大量数据时，就会发出多次http请求
							 */
							pipeline.addLast(new HttpObjectAggregator(8192));
							/*
							 1、对于websocket，它的数据是以帧（frame）形式传递
							 2、可以看到webSocketFrame下面六个子类
							 3、浏览器请求时，ws://localhost:9999/xxx表示请求的uri
							 4、WebSocketServerProtocolHandler将http协议升级为ws协议，保持长连接
							 5、是通过一个状态码101
							 */
							pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
							//处理websocket信息
							pipeline.addLast(new TextWebSocketFrameHandler());
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
