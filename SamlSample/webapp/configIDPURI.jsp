<!--  
 VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.


-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="sample.MySSO" %>
<%@ page import="java.util.List" %>
<%@ page import="com.vmware.samltoolkit.SsoServiceFacade"%>
<%@ page import="com.vmware.samltoolkit.SAMLToolkitConf"%>
<%@ page import="java.util.Map" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<!-- header -->
	<jsp:include page="headertpl.html"></jsp:include>
	<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">
		 <!-- Navigation -->
    	<jsp:include page="navtpl.html"></jsp:include>
    	
    	<!-- get related configuration-->
		<%
		String idpDomainURI = request.getParameter("vidmidp");	
		String spIssuer= request.getParameter("issuer");
		String spConsumer = spIssuer + "/consume";
		
		boolean byPassCert = Boolean.valueOf(request.getParameter("byPassCert"));
		SsoServiceFacade facade = MySSO.initSsoServiceFacade(idpDomainURI, spConsumer, byPassCert);
		SAMLToolkitConf conf = null;
		if(facade != null) {
			conf = facade.getSAMLToolkitConf();
		}
		%>		
		
    	<section id="ConfigDone" class="content-section text-center" style="height:100%;width:100%;">
        	<div class="ConfigDone-section">
            	<div class="container">
                <div class="col-lg-8 col-lg-offset-2">
					<%if((facade != null) && (conf != null)) {
					%>         
						<form action="configvidm.jsp" method="get" style="padding: 2rem;">
							<p>Congratulations. Your SSO is ready.</p>
							<p>Consumer URL is <%= spConsumer %></p>
							<p>vIDM URL is <%= idpDomainURI %></p>
							<div>
								 <a href="#sp-detail" class="btn btn-circle page-scroll">
			                            <i class="fa fa-angle-double-down animated"></i>
			                     </a>
	                    	</div>
	                    	<br/>
	                    	<br/>
	                    	<br/>
							<br/>
	                    	<br/>
							<div><input type=submit value="Configure application on VIDM" class="btn btn-default btn-lg">
							</div>
							<br/>
						</form>
						
					<%} else {%>
						<div class="jumbotron" style="color:#000">
				            <h2>Cannot fetch data from Idp Service!</h2>
				            <p>Hints: Bypass certificate validation if vIDM is using self-signed certificate. Check your vIDM URL and VIDM service.</p>
				         </div>
				         <div>
		                	<form action="idpDiscovery.jsp" method="get">
								<div><input type=submit value="<<< Back" class="btn btn-default btn-lg">
								</div>
							</form>
						</div>
					<%} %>
				</div>
				</div>
			</div>
		</section>
		<%if((facade != null) && (conf != null)) {%>
			<section id="sp-detail" class="content-section text-center" style="height:100%;width:100%;">
		        <div class="sp-detail">
		            <div class="container">
		               <div class="row row-offcanvas">
				        <div class="col-xs-12 col-sm-12">
				          <div class="row">
				            <div class="col-xs-6 col-lg-6">
				              <h2>Vidm URI</h2>
				              <p><%= conf.getIdpURL() %> </p>
				            </div><!--/.col-xs-6.col-lg-6-->
				            <div class="col-xs-6 col-lg-6">
				              <h2>Sp Consumer</h2>
				              <p><%= conf.getConsumerURL() %></p>
				            </div><!--/.col-xs-6.col-lg-6-->
				            <div class="col-xs-6 col-lg-6">
				              <h2>Sp Issuer</h2>
				              <p><%=conf.getIssuerName() %> </p>
				            </div><!--/.col-xs-6.col-lg-6-->
				            <div class="col-xs-6 col-lg-6">
				              <h2>Idp Signing Certificate</h2>
				              <div><%=conf.getCertificate().substring(0, 100) %></div>
				              <p><a class="btn btn-default" href="#signcert" role="button" data-toggle="modal">View details »</a></p>
				            </div>
				            <div class="col-xs-6 col-lg-6">
				              <h2>SSO BINDING</h2>
				              <%
				              	Map<String, String> loginBindings = conf.getLoginBindings();
				              	for(Map.Entry<String, String> entry : loginBindings.entrySet()) {
				              %>
				              	<div>Binding: <%=entry.getKey() %></div>
				              	<div>Location: <%=entry.getValue() %></div>
				              	<br/>
				              <%
				              	}
				              %>
				            </div><!--/.col-xs-6.col-lg-6-->
				            <div class="col-xs-6 col-lg-6">
				              <h2>Single logout BINDING</h2>
				             <%
				              	Map<String, String> logoutBindings = conf.getLogoutBindings();
				              	for(Map.Entry<String, String> entry : logoutBindings.entrySet()) {
				              %>
				              	<div>Binding: <%=entry.getKey() %></div>
				              	<div>Location: <%=entry.getValue() %></div>
				              	<br/>
				              <%
				              	}
				              %>
				            </div><!--/.col-xs-6.col-lg-6-->
				            
					        <div class="col-xs-12 col-lg-12" style="padding-top:50px">
						          <form action="configvidm.jsp" method="get"">
										<div><input type=submit value="Configure application on VIDM" class="btn btn-default btn-lg"></div>
								 </form>
							 </div>
				          </div><!--/row-->
				        </div><!--/.col-xs-12.col-sm-12-->
				      </div>
		            </div>
		        </div>
	    	</section>
	    	
	    	<div id="signcert" class="modal fade">
		        <div class="modal-dialog" style="boder-color:#fff">
		            <div class="modal-content">
		                <div class="modal-header black-background green-font">
		                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		                    <h4 class="modal-title">Signing Certificate</h4>
		                </div>
		                <div class="modal-body black-background">
		                    <div><%=conf.getCertificate()%></div>
		                </div>
		            </div>
		        </div>
		    </div>
    	<%} %>
	</body>
</html>