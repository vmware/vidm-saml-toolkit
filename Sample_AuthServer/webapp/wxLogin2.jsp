<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.vmware.eucenablement.sample.idp.MyIDP"%>
<%@ page import="java.util.*" %>
<%@ page import="com.vmware.eucenablement.oauth.util.OAuthUtil" %>
<%@ page import="com.vmware.eucenablement.sample.servlet.WeChatServlet" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="org.opensaml.xml.signature.Q" %>

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
                            <div><h2>Oops...</h2></div>
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
                                </p>
                            </div>

                        <%
                    }
                    else {
                        // login

                        String state=OAuthUtil.encode(request.getRequestedSessionId());
                        response.sendRedirect(WeChatServlet.getWeChatOAuth(request).getAuthorizationQrcodeUrl(state));

                    }
                %>

            </div>
        </div>
    </section>
</body>
</html>
