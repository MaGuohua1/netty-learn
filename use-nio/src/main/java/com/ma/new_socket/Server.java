package com.ma.new_socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

public class Server {

	@SuppressWarnings("null")
	public static void main(String[] args) throws IOException {
		SelectorProvider provider = SelectorProvider.provider();
		ServerSocketChannel socketChannel = provider.openServerSocketChannel();
		socketChannel.configureBlocking(false);
		Selector selector = provider.openSelector();
		socketChannel.register(selector, 0);
		socketChannel.bind(new InetSocketAddress(6666));
		SelectionKey key = socketChannel.keyFor(selector);
		key.interestOps(SelectionKey.OP_ACCEPT);
		
		// 循环等待客户端连接
		while (true) {
			// select监听
			if (selector.select(1000) == 0) {
				continue;
			}
			// 获取已经关注事件的selectedKeys集合
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			// 遍历selectedKeys
			for (Iterator<SelectionKey> iterator = selectedKeys.iterator(); iterator.hasNext();) {
				// 获取selectionKey
				SelectionKey selectionKey = (SelectionKey) iterator.next();
				// 获取channel
				SelectableChannel channel = selectionKey.channel();
				// accept事件
				if (selectionKey.isAcceptable()) {
					// 获取客户端连接
					channel = socketChannel.accept();
					// 设置ServerSocketChannel为非阻塞
					channel.configureBlocking(false);
					// socketChannel注册到selector,关注事件为OP_READ，同时给socketChannel关联一个buffer
					channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
					// read事件
				} else if (selectionKey.isReadable()) {
					// 获取buffer
					ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
					// 把channel数据读取到buffer
					((SocketChannel) channel).read(buffer);
					System.out.println(new String(buffer.array()));
				} 
				// 移除selectionKey
				selectedKeys.remove(selectionKey);
			}
		}
	}
}
