package com.aop.server;

public class WebServerTester {
	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.setRootPath("c:\\users\\padmaja\\websites\\");
		server.start();
		
	}
}
