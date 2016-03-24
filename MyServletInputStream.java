package com.aop.server;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class MyServletInputStream extends ServletInputStream {

	private InputStream inputStream;

	public MyServletInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setReadListener(ReadListener readListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public int read() throws IOException {
		return this.inputStream.read();
	}

}
