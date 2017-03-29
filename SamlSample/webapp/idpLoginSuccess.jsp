<!--  
 VMware Identity Manager SAML Toolkit

Copyright (c) 2016 VMware, Inc. All Rights Reserved.

This product is licensed to you under the BSD-2 license (the "License").  You may not use this product except in compliance with the BSD-2 License.

This product may include a number of subcomponents with separate copyright notices and license terms. Your use of these subcomponents is subject to the terms and conditions of the subcomponent's license, as noted in the LICENSE file.


-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.vmware.eucenablement.saml.sample.idp.MyIDP"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>
</head>
<body>

 <form method="post" action=""https://vidm.stengdomain.fvt/SAAS/auth/saml/response" >
    <input type="hidden" name="SAMLResponse" value='<%= session.getAttribute("SAMLResponse") %>' />
   
  </form>
  <script type="javascript">
  window.onload = function () { document.forms[0].submit(); }
  </script>

</body>
</html>