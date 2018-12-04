package com.jq.code;

import java.util.HashMap;

/**
 * 为了解决引用不到类的问题
 * Created by lijh on 2017/8/5.
 */

public class WholeClasss {
    public static final String KEY_FamilyMemberActivity = "FamilyMemberActivity";
    private static final HashMap<String, Class> map = new HashMap<>();
    public static void put(String name, Class cls) {
        map.put(name, cls);
    }

    public static Class get(String name) {
        return map.get(name);
    }
}
