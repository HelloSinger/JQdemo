package com.jq.code.model;

/**
 * Created by xulj on 2016/5/5.
 */
public class XHelpEntity {
    private int position ;
    private String textStr ;

    public XHelpEntity(int position, String textStr) {
        this.position = position;
        this.textStr = textStr;
    }

    public String getTextStr() {
        return textStr;
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
