package com.aop.server;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServlet;

import org.apache.commons.io.FileUtils;

public class MyClassLoader extends ClassLoader {

	public MyClassLoader(ClassLoader parent) {
		super(parent);
	}

	/**
	 * clsName = com.aop.btrack.LoginServlet
	 * 
	 * @param file
	 * @param clsName
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public Class<HttpServlet> loadFromPath(File file, String clsName) throws IOException {
		String fileName = clsName.replace('.', '/') + ".class";
		File clsFile = new File(file, fileName);
		byte[] clsBytes = FileUtils.readFileToByteArray(clsFile);

		Class<?> definedClass = defineClass(clsName, clsBytes, 0, clsBytes.length);
		return (Class<HttpServlet>) definedClass;
	}

}
