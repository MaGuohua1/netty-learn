package com.ma.rpc.provider;

import com.ma.rpc.netty.Server;

public class ServerBootStrap {
	public static void main(String[] args) {
		Server.startServer("127.0.0.1", 6666);
	}
}
