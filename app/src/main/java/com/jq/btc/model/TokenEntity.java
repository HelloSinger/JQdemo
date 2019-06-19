package com.jq.btc.model;

public class TokenEntity {

    /**
     * access_token : 15521ca9-f9c5-4784-bd93-ba85e0f47a4d
     * uhome_access_token :
     * refresh_token : 5df24129-ec90-4f29-a1e8-1217435ee849
     * uhome_user_id :
     * scope : equipments.admin users.admin openid clients.admin
     * token_type : bearer
     * expires_in : 855763
     */

    private String access_token;
    private String uhome_access_token;
    private String refresh_token;
    private String uhome_user_id;
    private String scope;
    private String token_type;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getUhome_access_token() {
        return uhome_access_token;
    }

    public void setUhome_access_token(String uhome_access_token) {
        this.uhome_access_token = uhome_access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUhome_user_id() {
        return uhome_user_id;
    }

    public void setUhome_user_id(String uhome_user_id) {
        this.uhome_user_id = uhome_user_id;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
