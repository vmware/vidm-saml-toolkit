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
import java.net.URL;
import java.security.KeyStoreException;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;



/**
 * Simple HTTP server for demo purpose.
 *
 */
public class MyWebServer {

	public static void main(String[] args) throws KeyStoreException {

		Server server = new Server();
		// HTTPS configuration
        HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());

        // Configuring SSL
        SslContextFactory sslContextFactory = new SslContextFactory();
        URL keystoreurl = MyWebServer.class.getResource("/sslkeystore");

        System.out.println("keystore path:"+ keystoreurl.getPath());
        // Defining keystore path and passwords
        sslContextFactory.setKeyStorePath(keystoreurl.getPath());
        String keystorepwd = "123456";
        sslContextFactory.setKeyStorePassword(keystorepwd);

        sslContextFactory.setTrustAll(true);
        sslContextFactory.setNeedClientAuth(false);

        // Configuring the connector
        ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
        sslConnector.setPort(8443);

        server.addConnector(sslConnector);

		
		server.setStopAtShutdown(true);

		// Set JSP to use Standard JavaC always
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");

		WebAppContext webAppContext = new WebAppContext();

		String webapp = "Sample_WebApp/webapp";
		webAppContext.setDescriptor(webapp + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(webapp);
		webAppContext.setContextPath("/WebApp");
		webAppContext.setParentLoaderPriority(true);
		webAppContext.setClassLoader(Thread.currentThread().getContextClassLoader());

		webAppContext.addServlet(ConsumerServlet.class.getCanonicalName(), "/consume");
		
		server.setHandler(webAppContext);
		try {
			server.start();

			String url = "https://localhost:8443/WebApp";
			System.out.println("Open your browser to view the demo: " + url);
			
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}