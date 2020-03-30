package com.ma;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 
 * @author mgh_2
 *
 * @desription 使用channel的transferFrom
 */
public class Channel04 {

	public static void main(String[] args) throws IOException {

		FileInputStream inputStream = new FileInputStream("e:/file.txt");
		FileChannel source = inputStream.getChannel();

		FileOutputStream outputStream = new FileOutputStream("e:/file2.txt");
		FileChannel target = outputStream.getChannel();

		target.transferFrom(source, 0, source.size());

		source.close();
		target.close();
		outputStream.close();
		inputStream.close();
	}
}
