package com.vmware.eucenablement.oauth.impl;

import com.vmware.eucenablement.oauth.OAuth2;
import com.vmware.eucenablement.oauth.OAuth2Config;
import com.vmware.eucenablement.oauth.OAuthException;
import com.vmware.eucenablement.oauth.OAuthUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzhang on 2017-08-16.
 */
public class WeiboOAuth2Impl extends OAuth2 {

    public WeiboOAuth2Impl(OAuth2Config config) {
        super(config);
    }

    /**
     * Get authorization url for redirect.
     *
     * @param state
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    @Override
    public String getAuthorizationUrl(String state, Map<String, String> additionalParams) throws OAuthException, IOException {
        StringBuilder builder=new StringBuilder();
        builder.append(String.format("https://api.weibo.com/oauth2/authorize?client_id=%s&state=%s&response_type=code&redirect_uri=%s",
                oAuth2Config.get_APP_ID(), state, oAuth2Config.get_REDIRECT_URL_ENCODED()));
        OAuthUtil.additionalParamsToStringBuilder(builder, additionalParams);
        return builder.toString();
    }

    /**
     * Get access_token url
     *
     * @param code
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    @Override
    public String getAccessTokenUrl(String code, Map<String, String> additionalParams) throws OAuthException, IOException {
        StringBuilder builder=new StringBuilder();
        builder.append(String.format("https://api.weibo.com/oauth2/access_token?client_id=%s&client_secret=%s&grant_type=authorization_code&redirect_uri=%s&code=%s",
                oAuth2Config.get_APP_ID(), oAuth2Config.get_APP_SECRET(), oAuth2Config.get_REDIRECT_URL_ENCODED(), code));
        OAuthUtil.additionalParamsToStringBuilder(builder, additionalParams);
        return builder.toString();
    }

    /**
     * Weibo OAuth interface doesn't support refreshing token
     *
     * @param refresh_token
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    @Override
    public String getRefreshTokenUrl(String refresh_token, Map<String, String> additionalParams) throws OAuthException, IOException {
        return null;
    }

    @Override
    public String getUserInfoUrl(Map<String, String> additionalParams) throws OAuthException, IOException {
        if (accessToken==null || !accessToken.isValid())
            throw new OAuthException("Access token is invalid!");
        StringBuilder builder=new StringBuilder();
        builder.append(String.format("https://api.weibo.com/2/users/show.json?access_token=%s&uid=%s",
                getAccessTokenString(), getUid()));
        OAuthUtil.additionalParamsToStringBuilder(builder, additionalParams);
        return builder.toString();
    }

    /**
     * Get the user's UID
     * @return
     */
    public String getUid() {
        return getValue("uid");
    }

}
