package com.ma;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * @author mgh_2
 *
 * @desription 使用channel读取文件数据
 */
public class Channel02 {

	public static void main(String[] args) throws IOException {
		// 创建一个输入流
		File file = new File("e:/file.txt");
		FileInputStream inputStream = new FileInputStream(file);
		// 获取通道
		FileChannel channel = inputStream.getChannel();
		// 创建字节缓冲区
		ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
		channel.read(buffer);
		System.out.println(new String(buffer.array()));

		channel.close();
		inputStream.close();
	}
}
