package com.jq.code.model.haierLogon;

/**
 * Created by Administrator on 2017/7/7.
 */

public class LoginResult {
    private String access_token  ;
    private long expires_in ;
    private String token_type ;
    private String refresh_token ;
    private String uhome_access_token ;
    private String uhome_user_id ;
    private int code ;
    private String error ;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUhome_access_token() {
        return uhome_access_token;
    }

    public void setUhome_access_token(String uhome_access_token) {
        this.uhome_access_token = uhome_access_token;
    }

    public String getUhome_user_id() {
        return uhome_user_id;
    }

    public void setUhome_user_id(String uhome_user_id) {
        this.uhome_user_id = uhome_user_id;
    }
}
