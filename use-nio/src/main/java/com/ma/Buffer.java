package com.ma;

import java.nio.IntBuffer;

public class Buffer {

	private static IntBuffer byteBuffer;

	public static void main(String[] args) {
		allocatTest();
		putTest();
		flipTest();
		getTest();
		rewindTest();
		reRead();
		afterReset();
		clearDemo();
	}

	public static void allocatTest() {
		byteBuffer = IntBuffer.allocate(20);
		System.out.println("------------after allocate------------------");
		print();
	}

	public static void putTest() {
		for (int i = 0; i < 5; i++) {
			byteBuffer.put(i);
		}
		System.out.println("------------after put------------------");
		print();
	}

	public static void flipTest() {
		byteBuffer.flip();
		System.out.println("------------after flip------------------");
		print();
	}

	public static void getTest() {
		System.out.println("------------after &getTest 2------------------");
		for (int i = 0; i < 2; i++) {
			int j = byteBuffer.get();
			System.out.println("j = " + j);
		}
		print();
		System.out.println("------------after &getTest 3------------------");
		for (int i = 0; i < 3; i++) {
			int j = byteBuffer.get();
			System.out.println("j = " + j);
		}
		print();
	}

	public static void rewindTest() {
		byteBuffer.rewind();
		System.out.println("------------after flipTest ------------------");
		print();
	}

	public static void reRead() {
		System.out.println("------------after reRead------------------");
		for (int i = 0; i < 5; i++) {
			int j = byteBuffer.get();
			System.out.println("j = " + j);
			if (i == 2) {
				byteBuffer.mark();
			}
		}
		print();
	}

	public static void afterReset() {
		System.out.println("------------after reset------------------");
		byteBuffer.reset();
		print();
	}

	public static void clearDemo() {
		System.out.println("------------after clear------------------");
		byteBuffer.clear();
		print();
	}

	private static void print() {
		System.out.println("position=" + byteBuffer.position());
		System.out.println("limit=" + byteBuffer.limit());
		System.out.println("capacity=" + byteBuffer.capacity());
	}

}
