
# vidm-saml-toolkit

## Overview

vIDM (VMware Identity Manager) SAML Toolkit is a simple Java SDK (software development 
kit) for web developers who want to integrate vIDM with their web applications.

VIDM provides Single-Sign-On (SSO) to SAML (Security Assertion Markup Language) compliant
applications, but it is not a small task for web developers to make their applications
SAML compliant.

This toolkit can save developers' effort by providing some easy to use functions. With
this toolkit, a web developer can implement SSO function with vIDM, even if he has no
knowledge about SAML. This toolkit is platform independent, since it is written in Java. 

vIDM can play two different roles: identity provider (IDP) or service provider (SP).

### Role 1: Use vIDM as IDP

If you use vIDM as IDP, your web application will use vIDM as the SSO server. When user
accesses your web application without a valid session, he will be redirected to vIDM for
SSO. vIDM will tell your web application the user's ID if the user has been authenticated
successfully.

In such case, this toolkit supports the following functions:
1. Initialize an SSO service with vIDM URL      
2. Create an SSO request to vIDM      
3. Process an SSO result from vIDM      
4. Login to your webapp successfully


### Role 2: Use vIDM as SP

If you use vIDM as SP, your web application should act as an IDP, and vIDM will use your
web application as the SSO server. Once a user has logged in your web application, he can
access vIDM and the applications managed by vIDM without typing ID/password again.

In such case, this toolkit supports the following functions:
1. Initialize a local IDP service and configure which vIDM can use this IDP  
2. Process an SSO request from vIDM     
3. Authorize the user in your web application  
3.1 Authorize the user by username & password  
3.2 Authorize the user by OAuth
4. Create an SSO result to vIDM
5. Login to vIDM successfully

## Samples

We provided three samples, to show how the toolkit works.

### Sample_AuthServer
[Sample_AuthServer](Sample_AuthServer/) is a demo of using vIDM as SP. You can start your
web application as an IDP, config your vIDM, and authorize through your web application.

### Sample_WebApp
[Sample_WebApp](Sample_WebApp/) is a demo of using vIDM as IDP. You can start your web
application, and user without authorization will be redirected to vIDM, and vIDM will tell
you the user's id if the user has been authenticated successfully.

### Sample_OAuth
[Sample_OAuth](Sample_OAuth/) is also a demo of starting
your web application as an IDP. What's different from Sample_AuthServer, you don't need to the user by yourself, 
instead, you can send OAuth request to OAuth Server (such as WeChat), and verify by OAuth.

## Try it out

### Prerequisites

* A valid vIDM administrator account.
* JDK 1.6+, Maven.

### Build & Run

This is a maven project, just install the toolkit on the project base directory:

```
mvn clean install       # Reinstall the project
```

Then, you may enter the sample directory, and start the sample:
```
cd Sample_AuthServer/   # Enter the sample directory you want to run
mvn compile exec:java   # Start the sample
```

There will be some configuration, just check ReadMe.md in each sample directory for more details.


## Contributing

The vidm-saml-toolkit project team welcomes contributions from the community. If you wish to contribute code and you have not
signed our contributor license agreement (CLA), our bot will update the issue when you open a Pull Request. For any
questions about the CLA process, please refer to our [FAQ](https://cla.vmware.com/faq). For more detailed information,
refer to [CONTRIBUTING.md](CONTRIBUTING.md).

## License

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.

