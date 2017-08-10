<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.vmware.eucenablement.saml.sample.idp.MyIDP"%>
<%@ page import="java.util.*" %>
<%@ page import="com.vmware.eucenablement.oauth.OAuth" %>
<%@ page import="com.vmware.eucenablement.saml.sample.idp.WeChatServlet" %>
<%@ page import="java.net.URLEncoder" %>

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
<%--                                    <br />
                                    Your Nickname:  <% out.print(request.getParameter("nickname")); %>
                                    <br />
                                    Your Province:  <% out.print(request.getParameter("province")); %>
                                    <br />
                                    Your City:  <% out.print(request.getParameter("city")); %>
                                    <br />
                                    Your Country:  <% out.print(request.getParameter("country")); %>  --%>
                                </p>
                            </div>

                        <%
                    }
                    else if (!header.contains("MicroMessenger")) {
                        // show qrCode
                        %>
                            <div><h2>WeChat Login</h2></div>
                            <div style="margin-top: -8px"><img src="http://pan.baidu.com/share/qrcode?w=180&h=180&url=<%
                                out.print(URLEncoder.encode(request.getRequestURL().toString()
                                +"?JSESSIONID="+request.getRequestedSessionId(),"utf-8"));
                            %>" /></div>
                            <div style="margin-top: 20px"><p style="font-size: 22px">Use your WeChat to scan the QRCode.</p></div>
                <script type="text/javascript">
                    function check() {
                        $.get("wxLoginAction?query=username", function (result) {
                            if (result.code==0) {
                                window.location="saml2postlogin?username="+result.username;
                            }
                        }, "json");
                    }
                    $(document).ready(function () {
                        setInterval(check, 1500);
                    });
                </script>
                        <%
                    }
                    else {
                        // login
                        %>
                            <div><h2>WeChat Login</h2></div>
                            <div>
                                <button class="btn btn-primary" style="margin-top: 30px"
                                    onclick="window.location='<%
                                    String jsessionid=request.getParameter("JSESSIONID");
                                    if (jsessionid==null || "".equals(jsessionid))
                                        jsessionid=request.getRequestedSessionId();
                                    out.print(OAuth.wxOAuthRedirect(WeChatServlet.APP_ID, WeChatServlet.REDIRECT_URL, jsessionid));%>'">
                                    Confirm Login</button>
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
