package com.ma.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient {

	public static void main(String[] args) throws IOException {
		SocketChannel channel = SocketChannel.open();
		//提供服务器的ip和端口
		InetSocketAddress inetSocketAddress = new InetSocketAddress(6666);
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		//连接服务器
		if (!channel.connect(inetSocketAddress)) {
			while (!channel.finishConnect()) {
				System.out.println("正在连接中...");
			}
		}
		//已连接
		while (true) {
			String next = scanner.next();
			ByteBuffer buffer = ByteBuffer.wrap(next.getBytes());
			channel.write(buffer);
			buffer.clear();
		}
		
	}
}
