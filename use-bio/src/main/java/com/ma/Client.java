package com.ma;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1", 6666);
			Scanner scanner = new Scanner(System.in);
			while (true) {
				String next = scanner.next();
				socket.getOutputStream().write(next.getBytes());
				InputStream inputStream = socket.getInputStream();
				byte[] b = new byte[1024];
				int len = inputStream.read(b);
				// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
				System.out.println(Thread.currentThread().getName() + " : " + new String(b, 0, len, "UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
