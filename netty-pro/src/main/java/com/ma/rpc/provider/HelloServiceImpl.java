package com.ma.rpc.provider;

import com.ma.rpc.public_interface.HelloService;

public class HelloServiceImpl implements HelloService {

	@Override
	public String hello(String msg) {
		System.out.println("get the message ["+msg+"]");
		if (msg==null) {
			return "hello client, this getted the message";
		} else {
			return "hello client, this getted the message ["+msg+"]";
		}
	}

}
