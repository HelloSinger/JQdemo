package com.jq.code.code.util;

import android.content.Context;

import com.jq.code.code.db.SportLocalDB;
import com.jq.code.model.sport.SportEntity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */

public class SportLocalDataUtils {
    private List<String> sportList = Arrays.asList("常用", "日常活动", "走路跑步", "休闲娱乐", "自行车运动", "力量运动", "健身器械", "健身操", "瑜伽",
            "户外运动", "工作", "舞蹈武术", "球类运动", "水上运动", "园艺活动", "乐器演奏", "其它体育运动");
    private SportLocalDB localDB;

    public SportLocalDataUtils(Context context) {
        localDB = SportLocalDB.getInstance(context);
    }

    public List<String> getTypeName() {
        return sportList;
//        return localDB.getSportType() ;
    }

    public List<SportEntity> getAllSport() {
        return localDB.getAllSprot();
    }
    public void insertCommonSport(SportEntity sportEntity){
        localDB.insertNewFoot(sportEntity);
    }
    public List<SportEntity> getSportDataFormType(String type) {
        return localDB.getSprotByType(type);
    }

}
