package com.ma.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyClient {

	public static void main(String[] args) throws InterruptedException {
		//客户端事件循环组
		EventLoopGroup group = new NioEventLoopGroup();
		
		try {
			//创建客户端启动对象
			//注意客户端使用的不是ServerBootstrap而是Bootstrap
			Bootstrap bootstrap = new Bootstrap();
			//设置相关参数
			bootstrap.group(group)//设置线程组
					.channel(NioSocketChannel.class)//设置客户端通道的实现类（反射）
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new ProtobufEncoder());
							pipeline.addLast(new NettyClientHandler());//加入自己的处理器
						}
					});
			System.out.println("client is ok");
			//启动客户端去连接服务器端
			//关于ChannelFuture，涉及到netty的异步模型
			ChannelFuture sync = bootstrap.connect("127.0.0.1", 6666).sync();
			//给关闭通道进行监听
			sync.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
}
