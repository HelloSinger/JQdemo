package com.jq.btc;

/**
 * Create by AYD on 2018/12/18
 */
public class ConstantUrl {
    //http://enxcook.xcook.cn:8888/
    private static String BASE_URL = "http://enxcook.xcook.cn:8888/CookbookResourcePlatform-api/v1/";

    public static String GET_USER_URL = "https://sso.xcook.cn/v3/family/familymember.info.list";

    /**
     * 上传体脂数据
     */
    public static String UPDATA_URL = BASE_URL + "cookbook/addBodyScale";

    /**
     * 匹配体重相似家庭成员
     */
    public static String MATCH_MEMBERS_URL = BASE_URL + "cookbook/findBodyScale";
    /**
     * 折线图获取数据
     */
    public static String GET_CHARTLINE_DATA_URL = BASE_URL + "cookbook/findBodyScaleListEveryday";

    /**
     * 查询当前用户最后一次体重
     */
    public static String GET_USER_LAST_WEIGHT = BASE_URL + "cookbook/findBodyScaleList";
}
