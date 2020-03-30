package com.ma.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// HttpServerCodec是netty提供的支持http编解码器
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("myHttpServerCodec", new HttpServerCodec());
		pipeline.addLast("myHttpServerHandler", new HttpServerHandler());
	}

}
