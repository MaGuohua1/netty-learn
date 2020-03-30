package com.ma.http;

import java.net.URI;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author mgh_2
 *
 * @desription SimpleChannelInboundHandler
 *             是ChannelInboundHandler的子类，HttpObject客户端和服务器相互通信的数据被封装成HttpObject
 * 
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

		if (msg instanceof HttpRequest) {

			// 过滤特定的请求
			HttpRequest req = (HttpRequest) msg;
			URI uri = new URI(req.uri());
			if ("/favicon.ico".equals(uri.getPath())) {
				return;
			}

			System.out.println(msg.getClass());
			System.out.println(ctx.channel().remoteAddress());

			ByteBuf content = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);

			// 构建一个http响应的response
			HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

			ctx.writeAndFlush(response);
		}

	}

}
