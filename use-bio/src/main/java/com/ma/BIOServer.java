package com.ma;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

	public static void main(String[] args) {
		ExecutorService pool = Executors.newCachedThreadPool();
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(6666);
			System.out.println("服务器启动");
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("获取到一个连接");
				pool.execute(() -> handler(socket));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handler(Socket socket) {
		try {
			InputStream stream = socket.getInputStream();
			byte[] b = new byte[1024];
			int len;
			while ((len = stream.read(b)) != -1) {
				// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
				System.out.println(Thread.currentThread().getName() + " : " + new String(b, 0, len, "UTF-8"));
				socket.getOutputStream().write(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
