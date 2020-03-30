package com.ma.websocket;

import java.time.LocalDateTime;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//TextWebSocketFrameHandler:表示一个文本帧（frame）
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		System.out.println(msg.text());
		ctx.channel().writeAndFlush(new TextWebSocketFrame("server "+ LocalDateTime.now()+msg.text()));
	}

	//当web客户端连接后，触发方法
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		//id表示唯一的值，longtext 是唯一的 ；shortText不是唯一
		System.out.println("handlerAdded "+ctx.channel().id().asLongText());
		System.out.println("handlerAdded "+ctx.channel().id().asShortText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		System.out.println("handlerRemoved "+ctx.channel().id().asLongText());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("异常发生 "+ cause.getMessage());
		ctx.close();
	}

}
