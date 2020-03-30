package com.ma.codec2;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 
 * @author mgh_2
 *
 * @desription 我们自定义一个Handler，需要继承netty规定好的HandlerAdapter
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<DataInfo.Mymessage> {

	// 数据读取完毕
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// writeAndFlush 是write + flush
		// 将数据写入到缓存，并刷新
		// 一般将，我们对这个发送的数据进行编码
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8));
	}

	//处理异常，一般是需要关闭通道
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Mymessage msg) throws Exception {
		DataInfo.Mymessage.DateType dataType = msg.getDataType();
		if (dataType == DataInfo.Mymessage.DateType.UserType) {
			DataInfo.User user = msg.getUser();
			System.out.println("user "+user.getId()+":"+user.getName());
		} else if (dataType == DataInfo.Mymessage.DateType.WorkerType) {
			DataInfo.Worker worker = msg.getWorker();
			System.out.println("worker "+worker.getName()+":"+worker.getAge());
		} else {
			System.out.println("传输的类型不正确");
		}
		System.out.println();
	}

}
