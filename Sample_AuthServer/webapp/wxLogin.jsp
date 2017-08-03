<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.vmware.eucenablement.saml.sample.idp.MyIDP"%>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>WeChat Login</title>
</head>

<jsp:include page="headertpl.html"></jsp:include>
<body id="page-top" data-spy="scroll" data-target=".navbar-fixed-top">
    <jsp:include page="navtpl.html"></jsp:include>

    <section id="userLogin" class="content-section text-center">
        <div class="idpDiscovery-section">
            <div class="container">
                <%
                    String header = request.getHeader("User-Agent");

                    String openid = request.getParameter("openid");
                    String errmsg = request.getParameter("errmsg");
                    if (errmsg!=null) {
                        // login failed
                        %>
                            <div><h2>Login Failed!</h2></div>
                            <div style="margin-top: 40px"><p><% out.print(errmsg); %></p></div>
                            <div>
                            <button class="btn btn-primary" style="margin-top: -15px"
                                onclick="window.location='wxLogin.jsp'">Retry</button>
                            </div>
                        <%
                    }
                    else if (openid!=null) {
                        // login Success
                        %>
                            <div><h2>Login Success!</h2></div>
                            <div style="margin-top: 40px">
                                <p>
                                    Your OpenId: <% out.print(openid); %>
                                    <br />
                                    Your Nickname:  <% out.print(request.getParameter("nickname")); %>
                                    <br />
                                    Your Province:  <% out.print(request.getParameter("province")); %>
                                    <br />
                                    Your City:  <% out.print(request.getParameter("city")); %>
                                    <br />
                                    Your Country:  <% out.print(request.getParameter("country")); %>
                                </p>
                            </div>

                        <%
                    }
                    else if (!header.contains("MicroMessenger")) {
                    // if(false) {
                        // 非微信打开，展示二维码
                        %>
                            <div><h2>WeChat Login</h2></div>
                            <div style="margin-top: -8px"><img src="img/wxQrcode.png" /></div>
                            <div style="margin-top: 20px"><p style="font-size: 22px">Use your WeChat to scan the QRCode.</p></div>

                        <%
                    }
                    else {
                        // 是微信打开，进行登录
                        %>
                            <div><h2>WeChat Login</h2></div>
                            <div>
                                <button class="btn btn-primary" style="margin-top: 30px"
                                    onclick="window.location='wxLoginAction'">Confirm Login</button>
                            </div>
                            <div style="margin-top: 30px"><p style="font-size: 22px">Click the button to confirm login with WeChat.</p></div>
                        <%


                    }
                %>

            </div>
        </div>
    </section>



</body>
</html>
