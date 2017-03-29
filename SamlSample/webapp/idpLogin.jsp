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


	<div class="login-page login-bg new-login"
		style="background-color: rgb(235, 239, 241);">
		<div class="row-fluid login-body">
			<div class="well shadow "
				style="background-color: rgb(255, 255, 255);">
				<div class="well-wrapper">

					<div class="login-form">
						<div id="notification"
							class="hide text-sm text-error container-notify margin-bottom-15"></div>
						<div>
							<form id="loginForm" class="form-login" action="saml2postlogin"
								method="POST" autocomplete="off">

								<div class="clearfix">
									<div class="login-field">

										<input type="text" autocorrect="off" autocapitalize="none"
											class="username login-input noEnterSubmit" id="username"
											name="username" value="" placeholder="username">
									</div>
								</div>


								<div class="clearfix">
									<div class="login-field">

										<input type="password"
											class="password login-input noEnterSubmit" id="password"
											name="password" value="" placeholder="password">
									</div>
								</div>




								<div class="clearfix bottom-section">
									<input type="submit" name="loginFormSubmit"
										id="loginFormSubmit" value="Sign in"
										class="btn btn-primary btn-full-width field-submit login-signin-button"
										style="background: rgb(110, 178, 70); color: rgb(255, 255, 255);">



								</div>
							</form>

						</div>
					</div>
				</div>

			</div>
		</div>
	</div>

</body>
</html>