package com.aop.server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class WebServer {

	/**
	 * Port on which we listen for the connections
	 */
	private int port = 80; // by default we take 80

	/**
	 * Root Path of the web apps to load from
	 */
	private String rootPath = "";

	private Map<String, WebApp> apps = new HashMap<>();

	public void start() {
		loadApps();
		try {
			ServerSocket ss = new ServerSocket(getPort());
			while (true) {
				Socket accept = ss.accept();
				new RequestHandler(this, accept).start();
			}
		} catch (IOException e) {
			System.out.println(
					"Unable to create server socket. " + "May be some other program is already using this port");
			e.printStackTrace();
		}
	}

	private void loadApps() {
		// for each folder in root create a webapp
		File root = new File(this.rootPath);
		for (File sub : root.listFiles()) {
			if (sub.isFile()) {
				continue;
			}
			// we need directories only
			WebApp app = new WebApp();
			app.setName(sub.getName());
			app.setPath(sub);

			this.apps.put(sub.getName(), app);
			System.out.println("Loading app" + sub.getName());
			try {
				app.load();
			} catch (Exception e) {
				System.out.println("Failed to load app");
				e.printStackTrace();
			}
		}
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public WebApp getWebApp(String site) {
		return this.apps.get(site);
	}

}
