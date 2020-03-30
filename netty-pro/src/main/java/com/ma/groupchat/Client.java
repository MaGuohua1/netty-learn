package com.ma.groupchat;

import java.util.Scanner;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class Client {
	private String host;
	private int port;

	public Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@SuppressWarnings("resource")
	private void run() {
		
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group)// 设置线程组
					.channel(NioSocketChannel.class)// 设置客户端通道的实现类（反射）
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
						 	pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
							pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
							pipeline.addLast(new ClientHandler());// 加入自己的处理器
						}
					});
			ChannelFuture sync = bootstrap.connect(host, port).sync();
			Channel channel = sync.channel();
			System.out.println("======="+channel.localAddress()+"=========");
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String nextLine = scanner.nextLine();
				channel.writeAndFlush(Unpooled.copiedBuffer(nextLine, CharsetUtil.UTF_8));
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Client("127.0.0.1", 10000).run();
	}
}
