package com.ma;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 
 * @author mgh_2
 *
 * @desription 使用channel拷贝文件
 */
public class Channel03 {

	public static void main(String[] args) throws IOException {
		// 创建一个输入流
		File file = new File("e:/file.txt");
		FileInputStream inputStream = new FileInputStream(file);
		// 获取通道
		FileChannel source = inputStream.getChannel();
		// 创建字节缓冲区
		ByteBuffer buffer = ByteBuffer.allocate((int) file.length());

		// 创建一个输出流
		FileOutputStream outputStream = new FileOutputStream("e:/file2.txt");
		// 获取通道
		FileChannel target = outputStream.getChannel();

		while (source.read(buffer) > 0) {
			/*
			// 清空buffer
			buffer.clear();
			// 把数据读取到buffer
			int read = source.read(buffer);
			if (read == -1) {
				break;
			}
			*/
			// 读写转换
			buffer.flip();
			// 写入通道
			target.write(buffer);

		}

		source.close();
		target.close();
		inputStream.close();
		outputStream.close();
	}
}
