
# vidm-saml-toolkit

## Overview

vIDM (VMware Identity Manager) SAML Toolkit is a simple Java SDK (software development kit) for web developers who want to integrate vIDM with their web applications.

VIDM provides Single-Sign-On (SSO) to SAML (Security Assertion Markup Language) compliant applications, but it is not a small task for web developers to make their applications SAML compliant.

This toolkit can save developers' effort by providing some easy to use functions. With this toolkit, a web developer can implement SSO function with vIDM, even if he has no knowledge about SAML. This toolkit is platform independent, since it is written in Java. 

vIDM can play two different roles: identity provider (IDP) or service provider (SP).

### Role 1: Use vIDM as IDP

If you use vIDM as IDP, your web application will use vIDM as the SSO server. When user accesses your web application without a valid session, he will be redirected to vIDM for SSO. vIDM will tell your web application the user's ID if the user has been authenticated successfully.

In such case, this toolkit supports the following functions:
1. Initialize an SSO service with vIDM URL      
2. Create an SSO request to vIDM      
3. Process an SSO result from vIDM      
4. Sign Out


### Role 2: Use vIDM as SP

If you use vIDM as SP, your web application should act as an IDP, and vIDM will use your web application as the SSO server. Once a user has logged in your web application, he can access vIDM and the applications managed by vIDM without typing ID/password again.

In such case, this toolkit supports the following functions:
1. Initialize a local IDP service and configure which vIDM can use this IDP  
2. Process an SSO request from vIDM     
3. Create an SSO result to vIDM      
4. Sign Out


## Try it out

### Prerequisites

* A valid vIDM administrator account, so you can add a web application from vIDM adminstration console
* JDK 1.6 and Maven are required to build this project. The suggested development IDE is Eclipse + Maven plugin. You can choose any other IDE that you like.
* Windows 7 platform or above
* User can change the sslkeystore to their own keystore file under src/main/java from both the sample projects.

### Build

Option 1: Build in Eclipse

1. Import these 4 projects as existing Maven project into your workspace: vidmsaml, Sample_WebApp, Sample_AuthServer, and SamlToolkit

2. Run -> Run Configurations -> Maven Build, select "vidmsaml" as base directory, use "clean package" as goals. 
![Maven Build Image Step 1](/img/mvnbuild1.png)
![Maven Build Image Step 2](/img/mvnbuild2.png)

Option 2: Build with Maven command line tool

Execute "mvn clean package" command in "vidmsaml" folder

Once your build is successful, you should get "SamlToolkit-jar-with-dependencies.jar" and "SamlToolkit.jar" in "vidm-saml-toolkit\SamlToolkit\target". 

You also get 2 sample web servers which guide you to integrate with vIDM server easily in "vidm-saml-toolkit\Sample_WebApp\target" and "vidm-saml-toolkit\Sample_AuthServer\target".

* Sample_WebApp demonstrates the scenario when samltoolkit plays the role of SP and vIDM is IDP.

* Sample_AuthServer demonstrates the scenario when samltoolkit plays the role of IDP and vIDM is SP.

### Run

The Sample web server can be started with one of the following options:

Option 1: Use the startup script for Windows OS 

Go to "vidm-saml-toolkit\Sample_WebApp\target" or "vidm-saml-toolkit\Sample_AuthServer\target" folder, double click "start.bat"

Option 2: Debug in Eclipse

Select "MyWebServer.java" in Sample_WebApp project or "MyAuthServer.java" in Sample_AuthApp, Run As -> Java Application.
![Run Server in Eclipse](/img/runserver.png)

### Access

Access URL below with any Internet browser after your Sample server is started.
* Sample_WebApp: https://localhost:8443/WebApp
* Sample_AuthApp: https://localhost:8443/MyAuthServer

## Documentation

### Role 1: Use vIDM as IDP

Refer to the sample project "Sample_WebApp"

You may need the following Java Classes in "SamlToolkit.jar" to integrate your web application with vIDM:

    com.vmware.samltoolkit.SSOService

    com.vmware.samltoolkit.SAMLToolkitConf

    com.vmware.samltoolkit.SamlSsoResponse

Step 1. New an SSOService object when your web server is started

    SAMLToolkitConf conf = new SAMLToolkitConf();
   
    conf.setIdpURL("tenantxxx.vmwareidentity.com");
   
    conf.setConsumerURL("http://localhost:8080/SamlSample/consume");
   
    conf.setByPassSSLCertValidation(true);
   
    SSOService service = new SSOService(conf);
  
Step 2. Redirect the login request to vIDM. 
   
    String redirectURL = service.getSSOURLRedirect("");

    response.sendRedirect(redirectURL);
   
Step 3. Implement your own SAML consumer service like this (can be a Servlet, JSP, or any other):
   
	String samlresponse = ((HttpServletRequest)req).getParameter("SAMLResponse");	
		
	SAMLSsoResponse ssoResponse = service.decodeSSOResponse(samlresponse);
		
	if((ssoResponse != null) && (ssoResponse.isValid()) && (ssoResponse.ssoSucceed())) {
			   
		String username = ssoResponse.getNameId();
			   
		//TODO: SSO is successful, continue your service with this username
	}else{
		
		//TODO: SSO is failed, show an error page or remind the user to login again
		
	}



### Role 2: Use vIDM as SP

Refer to the sample project Sample_AuthServer (in folder "Sample_AuthServer")

You may need the following Java Classes in "SamlToolkit.jar" to use vIDM as SP, and use your web application as IDP:

    com.vmware.samltoolkit.idp.IDPService

    com.vmware.samltoolkit.idp.SAMLIDPConf

    com.vmware.samltoolkit.idp.SamlSsoRequest

Step 1. New an IDPService object when your web server is started

    SAMLIDPConf conf = new SAMLIDPConf(issuer, kestoreStream, keystorepwd);

	 service = new IDPService(conf);
  
Step 2. (Optional) Process the SSO request from vIDM. If necessary, redirect the user to your login page.
   
	 String relay = request.getParameter("RelayState");
	 SAMLSsoRequest ssoRequest = service.decodeSAMLRequest(s);
	 if (ssoRequest != null && ssoRequest.isValid() ) {
			session.setAttribute("request", ssoRequest);
			response.sendRedirect("idpLogin.jsp");
			return;
	 }
   
Step 3. If the user has logged in, redirect the user back to vIDM with SAMLSsoResponse
   
Option 1: If the login is initiated by vIDM and you have received the SSO request, use the request to generate SSO response
	  
	 ssoresponse = MyIDP.getIDPService().getSSOResponseByPostBinding(ssoRequest, user);
	 response.getOutputStream().write(ssoresponse.getBytes());
	  
Option 2:  You can generate SSO response directly with vIDM URL, and redirect the user to vIDM web page
	  
	 ssoresponse = MyIDP.getIDPService().getSSOResponseByPostBinding(vidm, user, relay);
	 response.getOutputStream().write(ssoresponse.getBytes());	
	
## Contributing

The vidm-saml-toolkit project team welcomes contributions from the community. If you wish to contribute code and you have not
signed our contributor license agreement (CLA), our bot will update the issue when you open a Pull Request. For any
questions about the CLA process, please refer to our [FAQ](https://cla.vmware.com/faq). For more detailed information,
refer to [CONTRIBUTING.md](CONTRIBUTING.md).

## License

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

