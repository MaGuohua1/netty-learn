package com.ma.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class NettyByteBuf {

	public static void main(String[] args) {
		
		/*
		1、创建ByteBuf，包含byte[10]的数组
		2、不需要使用flip
		3、底层维护了readerIndex和writeIndex
		 */
		ByteBuf buffer = Unpooled.buffer(10);
		for (int i = 0; i < 11; i++) {
			buffer.writeByte(i);
		}
		
		for (int i = 0; i < buffer.capacity(); i++) {
			System.out.print(buffer.getByte(i)+",");
		}
		System.out.println();
		System.out.println("=========="+buffer.capacity()+"============");//10
		for (int i = 0; i < buffer.writerIndex(); i++) {
			System.out.print(buffer.readByte()+",");
		}
		System.out.println();
	}
}
