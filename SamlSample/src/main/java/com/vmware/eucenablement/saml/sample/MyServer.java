/*
 * VMware Identity Manager SAML Toolkit
 * 
 * Copyright (c) 2016 VMware, Inc. All Rights Reserved.
 * 
 * This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License. 
 * 
 * This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file. 
 * 
 */
package com.vmware.eucenablement.saml.sample;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Simple HTTP server for demo purpose. 
 *
 */
public class MyServer {

	public static void main(String[] args) {
		
		Server server = new Server();
		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);

		connector.setReuseAddress(false);
		server.addConnector(connector);
		server.setStopAtShutdown(true);

		server.addConnector(connector);

		// Set JSP to use Standard JavaC always
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

		WebAppContext webAppContext = new WebAppContext();

		String webapp = "webapp";
		webAppContext.setDescriptor(webapp + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(webapp);
		webAppContext.setContextPath("/SamlSample");
		webAppContext.setParentLoaderPriority(true);
		webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());

		webAppContext.addServlet(ConsumerServlet.class.getCanonicalName(), "/consume");

		server.setHandler(webAppContext);
		try {
			server.start();
			
			String url = "http://localhost:8080/SamlSample";
			System.out.println("Open your browser to view the demo: " + url);
			
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}