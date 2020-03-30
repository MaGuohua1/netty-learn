package com.ma.inbound_outbound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<Long> {

	private static Logger logger = LogManager.getLogger();
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
		
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("client send message");
//		ctx.writeAndFlush(12345L);
		
		/*
		 1、abcdabcdabcdabcd是16个字节
		 2、该处理器的前一个handler是LongToByteEncoder
		 3、LongToByteEncoder 父类 MessageToByteEncoder
		 4、父类 MessageToByteEncoder 的 write()方法中的
		 if(acceptOutboundMessage(msg)) //判断当前msg 是不是应该处理的类型，如果是，就处理，如果不是就跳过encode
		 因此我们编写Encoder是要注意传入的数据类型和处理的数据类型一致。
		 */
		ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd",CharsetUtil.UTF_8));
		logger.info("abcdabcdabcdabcd");
	}
	
}
