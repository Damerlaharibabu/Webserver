package com.aop.server;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WebApp {

	private String name;

	private File path;

	private Map<String, String> servlets = new HashMap<>();

	private Map<String, String> servletMappings = new HashMap<>();

	public void processRequest(ServletRequestImpl request) {
		String query = request.getQuery();

		String name = this.servletMappings.get(query);
		if (name == null) {
			// we dont have any servlet
		} else {
			// we have servlet
			// load and execute it
			String clsName = this.servlets.get(name);

			if (clsName == null) {
				// no servlet found with given name

			} else {
				// load the class
				MyClassLoader mc=new MyClassLoader(this.getClass().getClassLoader());
				try {
					Class<HttpServlet> servletCls= mc.loadFromPath(new File(path,"WEB-INF/classes"),clsName);
					HttpServlet servlet = servletCls.newInstance();
					ServletResponseImpl resp = new ServletResponseImpl(request.getSocket());
					
					servlet.service(request, resp);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Unable to load servlet");
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getPath() {
		return path;
	}

	public void setPath(File sub) {
		this.path = sub;
	}

	public void load() throws ParserConfigurationException, SAXException, IOException {
		File webFile = new File(this.path, "WEB-INF/web.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(webFile);

		NodeList servletElements = document.getElementsByTagName("servlet");
		for (int x = 0; x < servletElements.getLength(); x++) {
			Element servletElement = (Element) servletElements.item(x);
			Element nameElement = (Element) servletElement.getElementsByTagName("servlet-name").item(0);
			Element classElement = (Element) servletElement.getElementsByTagName("servlet-class").item(0);

			String name = nameElement.getTextContent();
			String clsName = classElement.getTextContent();
			System.out.println(name + ":" + clsName);
			this.servlets.put(name, clsName);
		}

		NodeList servletMappingElements = document.getElementsByTagName("servlet-mapping");
		for (int x = 0; x < servletMappingElements.getLength(); x++) {
			Element servletMappingElement = (Element) servletMappingElements.item(x);
			Element nameElement = (Element) servletMappingElement.getElementsByTagName("servlet-name").item(0);
			Element urlElement = (Element) servletMappingElement.getElementsByTagName("url-pattern").item(0);

			String name = nameElement.getTextContent();
			String url = urlElement.getTextContent();
			System.out.println(name + ":" + url);
			this.servletMappings.put(url, name);
		}

	}

}
