package com.vmware.eucenablement.saml.sample.idp;

import com.vmware.eucenablement.oauth.OAuth;
import com.vmware.eucenablement.oauth.OAuthException;

import org.json.JSONObject;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

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
        // get openid
        if (code!=null) {
            try {
                String openid=OAuth.wxOAuthGetOpenId(APP_ID, APP_SECRET, code);
                response.sendRedirect("wxLogin.jsp?openid="+openid);
            }
            catch (OAuthException e) {
                response.sendRedirect("wxLogin.jsp?errmsg="+e.getMessage());
            }
            return;

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


    private String get(String url) throws IOException {
        System.out.println("HTTP GET "+url);
        HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");

        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        String line;

        InputStream stream = connection.getInputStream();
        int responseCode = connection.getResponseCode();

        System.out.println(responseCode);

        br = new BufferedReader(new InputStreamReader(stream));
        while((line = br.readLine()) != null ){
            sb.append(line);
        }

        return sb.toString();
    }

}
