package com.jq.code.code.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by garfield on 10/26/16.
 */
public class AppLogMaker {
    public static final String[] heads = {"method", "uri", "start", "time","msg"};

    private Map<String,String> log;
    private AppLogMaker(){
        log = new HashMap<String,String>();
    }
    public static AppLogMaker makeAppLog(){
        return new AppLogMaker();
    }

    public AppLogMaker withMethod(String method){
        log.put("method",method);
        return this;
    }

    public AppLogMaker withUri(String uri){
        log.put("uri",uri);
        return this;
    }

    public AppLogMaker withStart(long start){
        log.put("start",String.valueOf(start));
        return this;
    }

    public AppLogMaker withTime(long time){
        log.put("time",String.valueOf(time));
        return this;
    }

    public AppLogMaker withMsg(String msg){
        log.put("msg", msg);
        return this;
    }

    public Map<String,String> build(){
//        assertAllFieldProvided();
        return log;
    }

    private void assertAllFieldProvided() {
        for (String key : heads) {
            if (log.get(key) == null){
                throw new AssertionError(key + " in AppLog can not be null!");
            }
        }
    }

}
