

# vidm-saml-toolkit

## Overview

VIDM Saml Toolkit is a simple Java SDK (software development kit) for web developers who want to implement "Sign In with VMware Identity Manager" for their web applications. 
VMware Identity Manager is an Identity as a Service (IDaaS) offering, providing application provisioning, self-service catalog, conditional access controls and Single Sign-On (SSO) for SaaS, web, cloud and native mobile applications. For implementing SSO with vIDM, web developers should follow SAML 2.0 standard to encode SAML request and decode SAML response. It is not a small task without using any SAML library. 
This toolkit can save developers' effort by providing some easy to use functions. A web developer can implement SSO function with vIDM, even if he has no knowledge about SAML 2.0. This toolkit is platform independent, since it is written in Java. 
This Toolkit supports the following 4 functions:      
1, Initialize SSO service with vIDM URL      
2, Create an authentication request to vIDM      
3, Process an authentication result from vIDM      
4, Sign Out

## Try it out

### Prerequisites

* A valid vIDM administrator account, so you can add a web application from vIDM adminstration console
* JDK 1.6 and Maven are required to build this project. The suggested development IDE is Eclipse + Maven plugin. You can choose any other IDE that you like. 

### Build

Option 1: Build in Eclipse

1, Import these 3 projects into your workspace: vidmsaml, SamlSample, and SamlToolkit
2. Run -> Run Configurations -> Maven Build, select "vidmsaml" as base directory, use "clean package" as goals. 

Option 2: Build with Maven command line tool

Execute "mvn clean package" command in "vidmsaml" folder

### Run
We have a Sample web server which guide you to integrate with vIDM server easily. It can be started with one of the following options:

Option 1: Use the startup script for Windows OS after your build is successful

Go to "vidm-saml-toolkit\SamlSample\target" folder, double click "startup.bat"

Option 2: Debug in Eclipse

Select "MyServer.java" in SamlSample project, Run As -> Java Application.

### Access

Access http://localhost:8080/SamlSample with any Internet browser after your Sample web server is started.

## Documentation

Find "SamlToolkit-jar-with-dependencies.jar" and "SamlToolkit.jar" in "vidm-saml-toolkit\SamlToolkit\target" folder.
You may need the following Java Classes to integrate your web application with vIDM:
com.vmware.samltoolkit.SSOService
com.vmware.samltoolkit.SAMLToolkitConf
com.vmware.samltoolkit.SamlSsoResponse

## Contributing

The vidm-saml-toolkit project team welcomes contributions from the community. If you wish to contribute code and you have not
signed our contributor license agreement (CLA), our bot will update the issue when you open a Pull Request. For any
questions about the CLA process, please refer to our [FAQ](https://cla.vmware.com/faq). For more detailed information,
refer to [CONTRIBUTING.md](CONTRIBUTING.md).

## License

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

