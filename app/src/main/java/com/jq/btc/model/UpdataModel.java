package com.jq.btc.model;

/**
 * Create by AYD on 2018/12/18
 */
public class UpdataModel {


    /**
     * ok : true
     * code : 200
     * message : 保存成功
     */

    private String ok;
    private String code;
    private String message;

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
