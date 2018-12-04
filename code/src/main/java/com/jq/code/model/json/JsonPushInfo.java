package com.jq.code.model.json;

import com.jq.code.model.PushInfo;

import java.util.ArrayList;


public class JsonPushInfo {

    private ArrayList<PushInfo> info;

    public ArrayList<PushInfo> getInfo() {
        return info;
    }


    public void setInfo(ArrayList<PushInfo> info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "JsonFLastArtcleInfo [info=" + info + "]";
    }
}
