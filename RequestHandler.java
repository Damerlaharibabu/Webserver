package com.aop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler extends Thread {

	private Socket socket;
	private WebServer server;

	public RequestHandler(WebServer server,Socket accept) {
		this.server = server;
		this.socket = accept;
		setName("Request Handler");
	}

	public void run() {
		try {
			ServletRequestImpl request = readRequest();
			if (request != null) {
				WebApp app = findWebApp(request);
				if (app != null) {
					app.processRequest(request);
				}else{
					socket.close();
				}
			}else{
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private WebApp findWebApp(ServletRequestImpl request) {
		String site = request.getHeader("Host");
		return this.server.getWebApp(site);
	}

	private void writeResponse(ServletRequestImpl request) throws IOException {
		PrintWriter pr = new PrintWriter(socket.getOutputStream());
		pr.write("HTTP/1.0 200 OK\r\n");
		pr.write("Content-Type: text/html\r\n");
		pr.write("\r\n");
		pr.write("<html><body><strong>Hello World " + request.getQuery() + " </strong></body></html>");
		pr.flush();
		pr.close();
		socket.close();
	}

	private ServletRequestImpl readRequest() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String line = br.readLine();
		if (line == null) {
			socket.close();
			return null;
		}
		ServletRequestImpl request = new ServletRequestImpl(line,socket);
		while (true) {
			String header = br.readLine();
			if (header.equals("")) {
				break;
			}
			request.addHeader(header);
		}
		return request;
	}

}
