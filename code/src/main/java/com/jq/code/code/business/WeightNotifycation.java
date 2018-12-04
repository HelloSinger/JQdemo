package com.jq.code.code.business;

import android.content.Context;

import com.jq.code.R;
import com.jq.code.code.util.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/12/23.
 */

public class WeightNotifycation {

    private static Integer[] notifyIds = new Integer[]{R.string.weighTip10, R.string.weighTip11, R.string.weighTip12, R.string.weighTip13, R.string.weighTip14, R.string.weighTip15,
            R.string.weighTip16, R.string.weighTip17, R.string.weighTip18, R.string.weighTip19, R.string.weighTip20, R.string.weighTip21, R.string.weighTip22,
            R.string.weighTip23, R.string.weighTip24, R.string.weighTip25, R.string.weighTip26, R.string.weighTip27, R.string.weighTip28, R.string.weighTip29,
            R.string.weighTip30, R.string.weighTip31, R.string.weighTip32, R.string.weighTip33, R.string.weighTip34, R.string.weighTip35, R.string.weighTip36,
            R.string.weighTip37, R.string.weighTip38, R.string.weighTip39, R.string.weighTip40, R.string.weighTip41, R.string.weighTip42, R.string.weighTip43,
            R.string.weighTip44, R.string.weighTip45};
    private static int tmpNotify;

    public static String getNotifycation(Context context) {
        return random(context);
    }

    private static String random(Context context) {
        List<Integer> entities = new ArrayList<>();
        String curDateAndTime = TimeUtil.getCurDateAndTime();
        int hour = Integer.valueOf(curDateAndTime.split(" ")[1].split(":")[0]);
        if (hour >= 21 && hour < 24) {
            entities.add(R.string.weighTip1);
        }
        if (hour >= 14 && hour < 18) {
            entities.add(R.string.weighTip2);
        }
        if (hour >= 13 && hour < 19) {
            entities.add(R.string.weighTip3);
        }
        if (hour >= 7 && hour < 8) {
            entities.add(R.string.weighTip4);
            entities.add(R.string.weighTip6);
        }
        if (hour >= 12 && hour < 13) {
            entities.add(R.string.weighTip5);
        }
        if (hour >= 17 && hour < 20) {
            entities.add(R.string.weighTip7);
        }
        if (hour >= 18 && hour < 19) {
            entities.add(R.string.weighTip8);
            entities.add(R.string.weighTip9);
        }
        entities.addAll(Arrays.asList(notifyIds));
        Random random = new Random();
        if (tmpNotify == 0) {
            tmpNotify = random.nextInt(entities.size());
        }
        return context.getString(entities.get(tmpNotify));
    }
}
