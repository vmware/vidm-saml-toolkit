package com.vmware.eucenablement.oauth;

/**
 * Created by chenzhang on 2017-08-16.
 */
public class OAuth2Authorization {

    private String code;
    private String state;
    private long timestamp;
    private long expireTime;

    public OAuth2Authorization(String code, String state) {
        this.code=code; this.state=state;
        timestamp=System.currentTimeMillis();
        expireTime=300;
    }

    public void setExpireTime(long seconds) {
        expireTime=seconds;
    }

    /**
     * Whether the authorization has been expired, default is 300 seconds
     * @return
     */
    public boolean isValid() {
        return System.currentTimeMillis()-timestamp<=1000*expireTime;
    }

    public String getCode() {return code;}
    public String getState() {return state;}

}
