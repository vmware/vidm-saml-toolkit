package com.vmware.eucenablement.oauth;

import com.vmware.eucenablement.oauth.util.HttpRequest;
import com.vmware.eucenablement.oauth.util.OAuthUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenzhang on 2017-08-16.
 */
public abstract class OAuth2 {

    protected OAuth2Config oAuth2Config;
    protected OAuth2AccessToken accessToken;

    public OAuth2(OAuth2Config config) {
        oAuth2Config=config;
    }

    /**
     * Get authorization url for redirect.
     * @param state
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public String getAuthorizationUrl(String state) throws OAuthException, IOException {
        return getAuthorizationUrl(state, new HashMap<String, String>());
    }

    /**
     * Get authorization url for redirect.
     * @param state
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public abstract String getAuthorizationUrl(String state, Map<String, String> additionalParams) throws OAuthException, IOException;

    /**
     * Get access_token url
     * @param code
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public String getAccessTokenUrl(String code) throws OAuthException, IOException {
        return getAccessTokenUrl(code, new HashMap<String, String>());
    }

    /**
     * Get access_token url
     * @param code
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public abstract String getAccessTokenUrl(String code, Map<String, String> additionalParams) throws OAuthException, IOException;

    /**
     * Require access_token from OAuth Server
     * @param code
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public OAuth2AccessToken getAccessToken(String code) throws OAuthException, IOException {
        return getAccessToken(code, new HashMap<String, String>());
    }

    protected HttpRequest.Method getAccessTokenMethod() {
        return HttpRequest.Method.GET;
    }

    /**
     * Require access_token from OAuth Server
     * @param code
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public OAuth2AccessToken getAccessToken(String code, Map<String, String> additionalParams) throws OAuthException, IOException {
        HttpRequest.Method method=getAccessTokenMethod();
        String url=getAccessTokenUrl(code, additionalParams);

        HttpRequest request=HttpRequest.http(url, method);
        if (method== HttpRequest.Method.POST)
            request.form(additionalParams);
        String response=request.body();
        JSONObject jsonObject=new JSONObject(response);

        String errmsg=getErrorMessageFromResponse(jsonObject);
        if (!OAuthUtil.isStringNullOrEmpty(errmsg))
            throw new OAuthException(errmsg);
        return getAccessTokenFromResponse(jsonObject);
    }

    /**
     * Get error message from JSON response. Return null if no error occurs.
     * @param jsonObject
     * @return
     */
    protected String getErrorMessageFromResponse(JSONObject jsonObject) {
        if (jsonObject.has("error_description"))
            return jsonObject.getString("error_description");
        if (jsonObject.has("error")) {
            if (jsonObject.has("message"))
                return jsonObject.getString("message");
            return jsonObject.getString("error");
        }
        if (jsonObject.has("errmsg"))
            return jsonObject.getString("errmsg");
        return null;
    }

    /**
     * Decode access_token from JSON response
     * @param jsonObject
     * @return
     */
    protected OAuth2AccessToken getAccessTokenFromResponse(JSONObject jsonObject) {
        OAuth2AccessToken oAuth2AccessToken=new OAuth2AccessToken();
        oAuth2AccessToken.setAccessToken(jsonObject.optString("access_token", null));
        oAuth2AccessToken.setExpiresIn(jsonObject.optLong("expires_in", 300L));
        oAuth2AccessToken.setRefreshToken(jsonObject.optString("refresh_token", null));

        Map<String, Object> map=new HashMap<>();
        for (String key: jsonObject.keySet()) {
            map.put(key, jsonObject.opt(key));
        }

        oAuth2AccessToken.setInfos(map);
        this.accessToken=oAuth2AccessToken;
        return oAuth2AccessToken;
    }

    /**
     * Get the refresh_token url
     * @param refresh_token
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public String getRefreshTokenUrl(String refresh_token) throws OAuthException, IOException {
        return getRefreshTokenUrl(refresh_token, new HashMap<String, String>());
    }

    /**
     * Get the refresh_token url
     * @param refresh_token
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public abstract String getRefreshTokenUrl(String refresh_token, Map<String, String> additionalParams) throws OAuthException, IOException;

    /**
     * Refresh the access_token
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public OAuth2AccessToken refreshAccessToken() throws OAuthException, IOException {
        return refreshAccessToken(new HashMap<String, String>());
    }

    protected HttpRequest.Method refreshAccessTokenMethod() {
        return HttpRequest.Method.GET;
    }

    /**
     * Refresh the access_token
     * @param additionalParams
     * @return
     * @throws OAuthException
     * @throws IOException
     */
    public OAuth2AccessToken refreshAccessToken(Map<String, String> additionalParams)
            throws OAuthException, IOException {
        if (accessToken==null || OAuthUtil.isStringNullOrEmpty(accessToken.getAccessToken()))
            throw new OAuthException("Access Token is Invalid!");

        HttpRequest.Method method=refreshAccessTokenMethod();

        String url=getRefreshTokenUrl(accessToken.getRefreshToken(), additionalParams);
        if (url==null)
            throw new OAuthException("Refreshing token is not supported!");

        HttpRequest request=HttpRequest.http(url, method);
        if (method== HttpRequest.Method.POST)
            request.form(additionalParams);
        String response=request.body();
        JSONObject jsonObject=new JSONObject(response);

        String errmsg=getErrorMessageFromResponse(jsonObject);
        if (!OAuthUtil.isStringNullOrEmpty(errmsg))
            throw new OAuthException(errmsg);
        OAuth2AccessToken accessToken1 = getAccessTokenFromResponse(jsonObject);
        accessToken.setAccessToken(accessToken1.getAccessToken());
        accessToken.setExpiresIn(accessToken1.getExpiresIn());
        accessToken.addInfos(accessToken1.getInfos());
        return accessToken;
    }

    public String getUserInfoUrl() throws OAuthException, IOException {
        return getUserInfoUrl(new HashMap<String, String>());
    }

    public abstract String getUserInfoUrl(Map<String, String> additionalParams) throws OAuthException, IOException;

    /**
     * Get user info
     * @return
     */
    public OAuth2AccessToken getUserInfo() throws OAuthException, IOException {
        return getUserInfo(new HashMap<String, String>());
    }

    protected HttpRequest.Method getUserInfoMethod() {
        return HttpRequest.Method.GET;
    }

    /**
     * Get user info
     * @return
     */
    public OAuth2AccessToken getUserInfo(Map<String, String> additionalParams) throws OAuthException, IOException {
        String url=getUserInfoUrl(additionalParams);
        if (url==null)
            throw new OAuthException("Userinfo is not supported!");
        HttpRequest.Method method=getUserInfoMethod();
        HttpRequest request=HttpRequest.http(url, method);
        if (method==HttpRequest.Method.POST)
            request.form(additionalParams);
        String response=request.body();
        JSONObject jsonObject=new JSONObject(response);
        String errmsg=getErrorMessageFromResponse(jsonObject);
        if (!OAuthUtil.isStringNullOrEmpty(errmsg))
            throw new OAuthException(errmsg);
        Map<String, Object> map=new HashMap<>();
        for (String key: jsonObject.keySet()) {
            map.put(key, jsonObject.opt(key));
        }
        accessToken.addInfos(map);
        return accessToken;
    }

    /**
     * Get the access token from local.
     * @return
     */
    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    /**
     * Get the access token string from local.
     * @return
     */
    public String getAccessTokenString() {
        return accessToken==null?null:accessToken.getAccessToken();
    }

    /**
     * Get values from access token
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getValue(String key) {
        return accessToken==null?null:(T)accessToken.getValue(key);
    }

    /**
     * Get values from access token
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public <T> T getValue(String key, String defaultValue) {
        return accessToken==null?null:(T)accessToken.getValue(key, defaultValue);
    }

}
