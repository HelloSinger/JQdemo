package com.jq.btc.bluettooth.report.haier.item;

import com.jq.btc.helper.WeighDataParser;

import java.util.ArrayList;

/**
 * 每一项的数据
 * Created by lijh on 2017/7/29.
 */

public class IndexDataItem {
    public int mIconRes;
    public int nameRes;
    public String valueText;
    public int mLevelColorRes;
    public int mLevelTextRes = -1;
    public String mUnitText;

    // 画区间值相关
    public int level;
    /**
     * 提示语
     */
    public int mLevelTipRes;
    public WeighDataParser.StandardSet mStandard;
    public float[] levelNums;
    public ArrayList<Integer> colors;
    public String[] topStr;
    public int[] bottomStr;
    public float value;
    public int mBiaoqingIcon;

    public float mLevelMaxtRes;
    public float criticalValue;

    public void calLevel(float mLevelMaxtRes, float criticalValue, float va, float[] levelNums, WeighDataParser.StandardSet standard) {
        mStandard = standard;
        value = va;
        this.levelNums = levelNums;
        this.mLevelMaxtRes=mLevelMaxtRes;
        this.criticalValue=criticalValue;

        // 计算级别
        int level = 0;
        for (int i = 0; i < levelNums.length; i++) {
            if (va < levelNums[i]) {
                break;
            } else {
                level++;
            }
        }
        mLevelColorRes = standard.getColor()[level];
        mLevelTextRes = standard.getStandards()[level];

        colors = new ArrayList<>();
        for (int i = 0; i < standard.getColor().length; i++) {
            colors.add(standard.getColor()[i]);
        }
        bottomStr = standard.getStandards();

        mLevelTipRes = standard.getTips()[level];
        this.level = level;
    }
}
