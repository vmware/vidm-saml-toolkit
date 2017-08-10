package com.vmware.eucenablement.oauth;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by chenzhang on 2017-08-07.
 */
public class OAuth {

    public static String wxOAuthRedirect(String APP_ID, String redirect_uri, String jsessionid) throws IOException {
        return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                APP_ID, URLEncoder.encode(redirect_uri, "utf8"), jsessionid);
    }

    public static String wxOAuthGetOpenId(String APP_ID, String APP_SECRET, String code) throws IOException, OAuthException {
        if (code==null || "".equals(code.trim()))
            throw new OAuthException("Code is null or empty!");

        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                APP_ID, APP_SECRET, code);

        JSONObject jsonObject=new JSONObject(HttpUtil.http(url));

        if (jsonObject.has("errmsg")) {
            throw new OAuthException(jsonObject.optString("errmsg"));
        }

        // access_token: json.optString("access_token")

        return jsonObject.optString("openid");

    }




}
