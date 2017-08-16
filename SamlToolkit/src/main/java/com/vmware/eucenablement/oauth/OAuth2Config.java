package com.vmware.eucenablement.oauth;

import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by chenzhang on 2017-08-16.
 */
public class OAuth2Config {

    private String APP_ID;
    private String APP_SECRET;
    private String REDIRECT_URL;

    public OAuth2Config(String APP_ID, String APP_SECRET, String REDIRECT_URL) {
        this.APP_ID=APP_ID;
        this.APP_SECRET=APP_SECRET;
        this.REDIRECT_URL=REDIRECT_URL;
    }

    public String get_APP_ID() {return APP_ID;}
    public String get_APP_SECRET() {return APP_SECRET;}
    public String get_REDIRECT_URL() {return REDIRECT_URL;}
    public String get_REDIRECT_URL_ENCODED() throws IOException {return URLEncoder.encode(REDIRECT_URL, "utf-8");}

}
