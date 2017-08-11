package com.vmware.eucenablement.sample.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.session.SessionHandler;
import org.json.JSONObject;

import com.vmware.eucenablement.oauth.OAuth;
import com.vmware.eucenablement.oauth.OAuthException;
import com.vmware.eucenablement.sample.idp.MyIDP;
import com.vmware.samltoolkit.idp.SAMLSsoRequest;

/**
 * Created by chenzhang on 2017-08-03.
 */
public class WeChatServlet  implements Servlet {

    public static final String APP_ID = "wxd9d651418ab66fdf";
    public static final String APP_SECRET = "5beceb1752824514e2a9bc8c09aa32e0";
    public static final String REDIRECT_URL = "https://127.0.0.1:8443/MyAuthServer/wxLoginAction";

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        Request request = (Request) req;
        Response response = (Response) res;

/*
        String access_token = request.getParameter("access_token"), openid=request.getParameter("openid");

        // Get user info
        if (access_token!=null && openid!=null) {
            String url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=en",
                    access_token, openid);

            // parse
            JSONObject jsonObject=new JSONObject(get(url));

            if (jsonObject.has("errmsg")) {
                response.sendRedirect("wxLogin.jsp?errmsg="+jsonObject.optString("errmsg"));
                return;
            }

            String nickname=jsonObject.optString("nickname");
            String province=jsonObject.optString("city");
            String city=jsonObject.optString("city");
            String country=jsonObject.optString("country");

            response.sendRedirect(String.format("wxLogin.jsp?openid=%s&nickname=%s&province=%s&city=%s&country=%s",
                    openid, nickname, province, city, country));
            return;
        }
*/

        String code=request.getParameter("code");
        String state=request.getParameter("state");

        if ("oauth".equals(request.getParameter("query"))) {
            HttpSession session = request.getSession();
            String username=(String)session.getAttribute("username");
            if (username!=null) {
                response.getWriter().write(new JSONObject().put("code", 0).toString());
            }
            else {
                response.getWriter().write(new JSONObject().put("code", -1).toString());
            }
            return;
        }

        if ("login".equals(request.getParameter("action"))) {

            // try to login...

            HttpSession session = request.getSession();
            String username=(String)session.getAttribute("username");

            // login failed
            if (username==null) {
                response.sendRedirect("wxLogin.jsp?errmsg="+ URLEncoder.encode("You haven't passed the WeChat OAuth!", "utf-8"));
                return;
            }

            // oauth success: login
            SAMLSsoRequest ssoRequest = (SAMLSsoRequest) session.getAttribute("request");
            if (ssoRequest==null) {
                // invalid sso: need to set SAML
                response.sendRedirect("wxLogin.jsp?errmsg="+ URLEncoder.encode("Invalid SSO request: Have you set the vIDM server correctly?", "utf-8"));
                return;
            }

            // confirm login
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(MyIDP.getIDPService().getSSOResponseByPostBinding(ssoRequest, username.substring(0, 20)).getBytes());
            response.getOutputStream().flush();
            return;

        }

        // get jsessionid by state
        if (state!=null) {
            String jsessionid=OAuth.decode(state);
            if (jsessionid==null) {
                response.sendRedirect("wxLogin.jsp?errmsg="+
                    URLEncoder.encode("Invalid qrcode: refresh the qrcode page and re-scan.", "utf-8"));
                return;
            }
            // get openid
            if (code!=null) {
                try {
                    String openid=OAuth.wxOAuthGetOpenId(APP_ID, APP_SECRET, code);

                    // Get the session by jsessionid
                    request.getSessionHandler().getHttpSession(jsessionid).setAttribute("username", openid);

                    // Login success in WeChat
                    //TODO: FIXME: question by steng: why you need parameter username ? A hacker may send any username, so please use SESSION
                    response.sendRedirect("wxLogin.jsp?openid="+openid);
                }
                catch (OAuthException e) {
                    response.sendRedirect("wxLogin.jsp?errmsg="+URLEncoder.encode(e.getMessage(),"utf-8"));
                }
                return;
            }
        }

        response.sendRedirect("wxLogin.jsp");

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }

}
