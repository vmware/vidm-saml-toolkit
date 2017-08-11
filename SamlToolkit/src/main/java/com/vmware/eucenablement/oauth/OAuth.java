package com.vmware.eucenablement.oauth;

import org.json.JSONObject;
import org.opensaml.xml.util.Base64;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * Created by chenzhang on 2017-08-07.
 */
public class OAuth {

    public static String wxOAuthRedirect(String APP_ID, String redirect_uri, String state) throws IOException {
        return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=%s#wechat_redirect",
                APP_ID, URLEncoder.encode(redirect_uri, "utf8"), state);
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

    /**
     * Encode jsessionid with current timestamp, to get"state"
     * @param jsessionid
     * @return
     */
    public static String encode(String jsessionid) {
        // md5 the current timestamp to get keys
        String time=String.valueOf(System.currentTimeMillis()/10000);
        byte[] keys=md5(time.substring(time.length()-6));

        // encode the bytes by keys
        byte[] bytes=jsessionid.getBytes().clone();
        for (int i=0,j=0;i<bytes.length;i++) {
            bytes[i]=(byte)((bytes[i]+keys[j]));
            j++;
            if (j>=keys.length) j=0;
        }

        return Base64.encodeBytes(bytes).replace('+','-').replace('=','_');
    }

    public static String decode(String state) {
        state=state.replace('-','+').replace('_','=');
        long currtime=System.currentTimeMillis()/10000;

        // 3 minutes, 180 seconds
        for (int t=0;t<18;t++) {
            // get keys
            String time=String.valueOf(currtime-t);
            byte[] keys=md5(time.substring(time.length()-6));

            // decode
            byte[] bytes=Base64.decode(state);
            for (int i=0,j=0;i<bytes.length;i++) {
                bytes[i]=(byte)((bytes[i]-keys[j]));
                j++;
                if (j>=keys.length) j=0;
            }

            String jsessionid=new String(bytes);
            // check if it's an valid jsessionid
            if (jsessionid.matches("[a-z0-9]{20,40}\\.[a-z0-9]{5}")) {
                return jsessionid;
            }
        }

        // can not decode: invalid
        return null;
    }

    private static byte[] md5(String s) {
        try {
            MessageDigest digest=MessageDigest.getInstance("MD5");
            return Arrays.copyOfRange(digest.digest(s.getBytes()), 0, 6);
        }
        catch (Exception e) {
            return Base64.encodeBytes(s.getBytes()).substring(0, 6).getBytes();
        }
    }

}
