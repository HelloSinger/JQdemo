package com.jq.btlib.util;

import android.os.Build;

/**
 * Created by lixun on 2016/5/24.
 */
public class ConfigurableDeviceUtil {
    private static final String TAG = "CsBtUtil_v11";
    private static final CsBtUtil_v11.CONNECT_MODE CONNECT_MODE =CsBtUtil_v11.CONNECT_MODE.Alway_Conn ;

    //判断蓝牙是否需要连接扫描或者只扫描一次
    public static boolean IsContinuousScann(boolean isLollipop){
        boolean bRet=true;
        if(isLollipop) {
            if(android.os.Build.BRAND.toLowerCase().equals("samsung")){
                if(Build.MODEL.contains("SM-G9300")){
                    bRet=false;
                }else if(Build.MODEL.contains("SM-G9008")){
                    bRet=false;
                }else if(Build.VERSION.SDK_INT>23){
                    bRet=false;
                }
            }else if(android.os.Build.BRAND.toLowerCase().equals("yanbochuang")){
                if(Build.MODEL.equalsIgnoreCase("A106")){
                    bRet=false;
                }
            }else if(Build.VERSION.SDK_INT>23){
                bRet=false;
            }
        }
        return bRet;
    }

    public static CsBtUtil_v11.CONNECT_MODE getConnectMode(){
//        CsBtUtil_v11.CONNECT_MODE bRet=CONNECT_MODE.Alway_Conn;
//
//        if(android.os.Build.BRAND.toLowerCase().equals("google")){
//            if(Build.MODEL.equalsIgnoreCase("nexus 5")){
//                bRet=CONNECT_MODE.FSAC;
//            }else if(Build.MODEL.equalsIgnoreCase("nexus 6p")){
//                bRet=CONNECT_MODE.FSAC;
//            }
//        }else if(android.os.Build.BRAND.toLowerCase().equals("meizu")){
//            if(Build.MODEL.equalsIgnoreCase("PRO 5")){
//                bRet=CONNECT_MODE.FSAC;
//            }
//        }else if(android.os.Build.BRAND.toLowerCase().equals("xiaomi")){
//            if(Build.MODEL.equalsIgnoreCase("mi note lte")){
//                bRet=CONNECT_MODE.FSAC;
//            }
//        }else if(android.os.Build.BRAND.toLowerCase().equals("samsung")){
//            if(Build.MODEL.equalsIgnoreCase("SM-G9300")){
//                bRet=CONNECT_MODE.FSAC;
//            }
//        }else if(android.os.Build.BRAND.toLowerCase().equals("huawei")){
//            if(Build.MODEL.startsWith("EVA-")){
//                //华为P9
//                bRet=CONNECT_MODE.FSAC;
//            }
//        }
//        return bRet;
        CsBtUtil_v11.CONNECT_MODE bRet= CsBtUtil_v11.CONNECT_MODE.FSAC;
        if(android.os.Build.BRAND.toLowerCase().equals("xiaomi")){
            if(Build.DEVICE.startsWith("HM")){
                bRet= CsBtUtil_v11.CONNECT_MODE.Alway_Conn;
            }
        }
        return bRet;
    }
}
