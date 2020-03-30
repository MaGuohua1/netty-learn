package com.ma.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ChatClient {

	private Selector selector;
	private SocketChannel channel;
	private static final String HOSTNAME = "127.0.0.1";
	private static final int PORT = 6666;
	private String username;

	public ChatClient() {
		try {
			selector = Selector.open();
			channel = SocketChannel.open(new InetSocketAddress(HOSTNAME, PORT));
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ);
			username = channel.getLocalAddress().toString().substring(1);
			System.out.println(channel.getLocalAddress().toString().substring(1));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void send(String info) {
		String msg = username + "说：" + info;
		try {
			channel.write(ByteBuffer.wrap(msg.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read() {
		try {
			int count = selector.select();
			if (count > 0) {
				Set<SelectionKey> keys = selector.selectedKeys();
				for (Iterator<SelectionKey> iterator = keys.iterator(); iterator.hasNext();) {
					SelectionKey key = (SelectionKey) iterator.next();
					channel = (SocketChannel) key.channel();
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					StringBuilder builder = new StringBuilder();
					while (channel.read(buffer) > 0) {
						builder.append(new String(buffer.array()));
					}
					System.out.println("客户端 [" + username + "] 的消息是：" + builder.toString());
					iterator.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		new Thread(() -> {
			while (true) {
				client.read();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String next = scanner.nextLine();
			client.send(next);
		}
	}
}
