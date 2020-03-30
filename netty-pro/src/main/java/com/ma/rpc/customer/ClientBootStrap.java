package com.ma.rpc.customer;

import com.ma.rpc.netty.Client;
import com.ma.rpc.public_interface.HelloService;

public class ClientBootStrap {

	public static final String HEADER = "HelloService#hello#";
	
	public static void main(String[] args) {
		Client client = new Client();
		HelloService proxy = (HelloService) client.getProxy(HelloService.class, HEADER);
		String hello = proxy.hello("hello dubbo");
		System.out.println("ddd "+hello);
	}
	
}
