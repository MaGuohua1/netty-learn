package com.ma.rpc.netty;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class Client {

	private static ClientHandler client;
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public Object getProxy(final Class<?> cls, final String header) {
		return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class<?>[] { cls }, (proxy, method, args) -> {
					if (client == null) {
						initClient();
					}
					client.setParam(header + args[0].toString());
					return executor.submit(client).get();
				});
	}

	private void initClient() {
		client = new ClientHandler();
		EventLoopGroup group = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(group)// 设置线程组
		.channel(NioSocketChannel.class)// 设置客户端通道的实现类（反射）
		.option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
				pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
				pipeline.addLast(client);// 加入自己的处理器
			}
		});
		System.out.println("client is ok");
		try {
			bootstrap.connect("127.0.0.1", 6666).sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
