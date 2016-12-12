<!--  
 VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.


-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<!-- header -->
	<jsp:include page="headertpl.html"></jsp:include>
	<body id="page-top" data-target=".navbar-fixed-top">
		<!-- Navigation -->
    	<jsp:include page="navtpl.html"></jsp:include>
		<section id="configvidm" class="content-section text-center">
	        <div class="configvidm-section">
	            <div class="container">
	               <div class="row row-offcanvas row-offcanvas-right">
	               
	        <div class="col-xs-12 col-sm-12">
	          <div class="jumbotron" style="color:#000">
	            <h2>Hello, VIDM!</h2>
	            <p>VIDM is an Identity as a Service (IDaaS) offering, providing application provisioning, self-service catalog, conditional access controls and Single Sign-On (SSO) for SaaS, web, cloud and native mobile applications.</p>
	          </div>
	          <br/>
	          <br/>
	          <div class="row">
	            <div class="col-xs-6 col-lg-6">
		              <h2>service provider on VIDM</h2>
		              <p>This is one document to describe how to build service provider on VIDM. </p>
		              <p><a class="btn btn-default" target="_new" href="http://www.vmware.com/content/dam/digitalmarketing/vmware/en/pdf/techpaper/vmware-identity-manager-on-premises-deployment-considerations.pdf" role="button">View document »</a></p>
	            </div>
	            
	            <div class="col-xs-6 col-lg-6" style="padding-top:50px">
		             <form action="ssoLogin.jsp" method="post">
						<p><input type=submit value="Switch to SSO Login >>>" class="btn btn-default btn-lg"></p>
						</form>
	            </div>
	          </div><!--/row-->
	        </div>
	        </div><!--/.sidebar-offcanvas-->
	      </div>
	      </div>
	    </section>
	</body>
</html>
