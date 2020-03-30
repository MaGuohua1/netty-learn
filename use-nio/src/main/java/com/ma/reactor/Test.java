package com.ma.reactor;

import java.io.IOException;

public class Test {

	public static void main(String[] args) throws IOException {
		Reactor reactor = new Reactor(6666);
		new Thread(reactor).start();
	}
}
