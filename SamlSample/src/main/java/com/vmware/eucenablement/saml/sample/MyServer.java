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
import java.security.KeyStoreException;
import java.util.Enumeration;

import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import com.vmware.eucenablement.saml.sample.idp.MyIDP;
import com.vmware.eucenablement.saml.sample.idp.MyIDPServlet;
import com.vmware.eucenablement.saml.service.AbstractSAMLService;

/**
 * Simple HTTP server for demo purpose.
 *
 */
public class MyServer {

	public static void main(String[] args) throws KeyStoreException {

		Server server = new Server();


         // HTTPS configuration
         HttpConfiguration https = new HttpConfiguration();
         https.addCustomizer(new SecureRequestCustomizer());

         // Configuring SSL
         SslContextFactory sslContextFactory = new SslContextFactory();

         String keystorepath = MyServer.class.getResource("sslkeystore").toExternalForm();
         // Defining keystore path and passwords
         sslContextFactory.setKeyStorePath(keystorepath);
         sslContextFactory.setKeyStorePassword("123456");

         sslContextFactory.setTrustAll(true);
         sslContextFactory.setNeedClientAuth(false);

         // Configuring the connector
         ServerConnector sslConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
         sslConnector.setPort(8443);

         // Setting HTTP and HTTPS connectors
         // server.setConnectors(new Connector[]{connector, sslConnector});
         server.addConnector(sslConnector);



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

		webAppContext.addServlet(MyIDPServlet.class.getCanonicalName(), "/saml2postlogin");


		server.setHandler(webAppContext);
		try {
			server.start();

			String url = "https://localhost:8443/SamlSample";
			System.out.println("Open your browser to view the demo: " + url);
			Enumeration<String> enums = sslContextFactory.getKeyStore().aliases();
	        while(enums.hasMoreElements()){
	        	String name = enums.nextElement();
	        	System.out.println("credential name:"+ name);
	        	AbstractSAMLService.setPrivateCredential( sslContextFactory.getKeyStore(), name, "123456");

	        }

			MyIDP.initIDPService();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}