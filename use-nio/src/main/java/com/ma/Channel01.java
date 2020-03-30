package com.ma;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * @author mgh_2
 *
 * @desription 使用channel生成输出文件
 */
public class Channel01 {

	public static void main(String[] args) throws IOException {
		String string = "hello world";
		// 创建一个输出流
		FileOutputStream outputStream = new FileOutputStream("e:/file.txt");
		// 获取通道
		FileChannel channel = outputStream.getChannel();
		// 创建字节缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(string.length());
		// 写入数据
		buffer.put(string.getBytes());
		// 读写转换
		buffer.flip();
		// 从buffer写入数据到通道
		channel.write(buffer);
		// 关闭输出流
		channel.close();
		outputStream.close();
	}
}
