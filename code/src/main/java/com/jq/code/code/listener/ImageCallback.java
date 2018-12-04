package com.jq.code.code.listener;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2016/7/8.
 */
public interface ImageCallback {
    void onImage(LinkedHashMap<String, SoftReference<Bitmap>> map);
}
