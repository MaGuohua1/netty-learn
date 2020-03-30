package com.ma.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {

	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private static final int PORT = 6666;

	public ChatServer() {
		try {
			// 创建selector
			selector = Selector.open();
			// 创建serverSocketChannel
			serverSocketChannel = ServerSocketChannel.open().bind(new InetSocketAddress(PORT));
			// 开启非阻塞
			serverSocketChannel.configureBlocking(false);
			// 注册到selector，事件类型OP_ACCEPT
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 监听
	private void listen() {
		try {
			while (true) {
				// 事件监听
				int count = selector.select(1000);
				if (count > 0) {
					// 获取事件
					Set<SelectionKey> keys = selector.selectedKeys();
					// 遍历
					for (Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext();) {
						SelectionKey key = (SelectionKey) iterator.next();
						// accept
						if (key.isAcceptable()) {
							register();
						}
						// read
						if (key.isReadable()) {
							// 读取数据
							String data = readData(key);
							sendMsg(data,key);
						}
						// 移除当前事件
						iterator.remove();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// socket注册
	private void register() throws IOException, ClosedChannelException {
		// 获取连接
		SocketChannel channel = serverSocketChannel.accept();
		// 设置非阻塞
		channel.configureBlocking(false);
		// 注册，事件为OP_READ
		channel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
		System.out.println(channel.getRemoteAddress().toString().substring(1) + " 用户上线了");
	}

	// 读取数据
	private String readData(SelectionKey key) {
		try {
			SocketChannel channel = (SocketChannel) key.channel();
			ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
			StringBuilder builder = new StringBuilder();
			while (channel.read(byteBuffer) > 0) {
				builder.append(new String(byteBuffer.array()));
			}
//			System.out.println("客户端 [" + channel.getRemoteAddress().toString().substring(1) + "] 的消息是：" + builder.toString());
			return builder.toString();
		} catch (IOException e) {
			close(key);
			return "";
		}
	}
	
	//发送数据
	private void sendMsg(String data,SelectionKey key) throws IOException {
		Set<SelectionKey> keys = selector.keys();
		for (Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext();) {
			SelectionKey targetKey = null;
			try {
				targetKey = (SelectionKey) iterator.next();
				SelectableChannel target = targetKey.channel();
				if (targetKey != key && target != serverSocketChannel) {
					SocketChannel channel = (SocketChannel) target;
					ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
					channel.write(buffer);
				} 
			} catch (IOException e) {
				close(targetKey);
			}
		}
	}
	
	private void close(SelectionKey key) {
		try {
			if (key.channel()==serverSocketChannel) {
				return;
			}
			SocketChannel channel = (SocketChannel) key.channel();
			System.out.println("客户端 [" + channel.getRemoteAddress().toString().substring(1) + "] 关闭连接");
			key.channel().close();
			key.cancel();
		} catch (IOException e) {
			e.getStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatServer server = new ChatServer();
		server.listen();
	}
}
