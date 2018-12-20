package com.jq.btc.bluettooth.report.haier.item;

import android.content.Context;
import android.util.Log;
import android.util.SparseIntArray;

import com.jq.btc.app.R;
import com.jq.btc.helper.WeighDataParser;
import com.jq.code.code.algorithm.CsAlgoBuilder;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.text.NumberFormat;

/**
 * Created by lijh on 2017/7/29.
 */

public class BuildItemsUtil {
    private static final String TAG = "BuildItems";
    final SparseIntArray mColorBiaoQingMap = new SparseIntArray();
    /**
     * 数字格式化工具类
     */
    final NumberFormat df = NumberFormat.getNumberInstance();

    {
        df.setMaximumFractionDigits(1);
        df.setGroupingUsed(false);

        mColorBiaoQingMap.put(R.color.corState1, R.mipmap.biaoqing_1);
        mColorBiaoQingMap.put(R.color.corState2, R.mipmap.biaoqing_2);
        mColorBiaoQingMap.put(R.color.corState3, R.mipmap.biaoqing_3);
        mColorBiaoQingMap.put(R.color.corState4, R.mipmap.biaoqing_5);
        mColorBiaoQingMap.put(R.color.corState5, R.mipmap.biaoqing_6);
        mColorBiaoQingMap.put(R.color.corState6, R.mipmap.biaoqing_7);
        mColorBiaoQingMap.put(R.color.corState7, R.mipmap.biaoqing_8);
        mColorBiaoQingMap.put(R.color.corState8, R.mipmap.biaoqing_4);
    }

    private CsAlgoBuilder csAlgoBuilder;

    private Context context;
    private WeightEntity mWeightEntity;
    private RoleInfo roleInfo;
    private String currentWeightUnit;

    public BuildItemsUtil(Context context, WeightEntity entity, RoleInfo roleInfo, CsAlgoBuilder csAlgoBuilder) {
        this.context = context;
        this.mWeightEntity = entity;
        this.roleInfo = roleInfo;
        currentWeightUnit = StandardUtil.getWeightExchangeUnit(context);
        this.csAlgoBuilder = csAlgoBuilder;
    }

    /**
     * 内脏脂肪
     */
    public IndexDataItem buildVisceraItem() {
        float viscera = mWeightEntity.getViscera();
        IndexDataItem visceraItem = new IndexDataItem();
        visceraItem.mIconRes = WeighDataParser.StandardSet.VISCERA.getIcon();
        visceraItem.nameRes = WeighDataParser.StandardSet.VISCERA.getName();
        visceraItem.valueText = df.format(viscera);
        visceraItem.mUnitText = context.getString(R.string.HaierReport_unit_level);
        float[] levelNums = WeighDataParser.StandardSet.VISCERA.getLevelNums();
        float max = levelNums[levelNums.length - 1];
        float criticalValue = levelNums[levelNums.length - 2];

        Log.e("AYD----->内脏脂肪", "max--->" + max + "criticalValue--->" + criticalValue);
        visceraItem.calLevel(max, criticalValue, viscera, levelNums, WeighDataParser.StandardSet.VISCERA);
        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = (int) levelNums[i] + "";
        }
        visceraItem.topStr = topStr;
        if (viscera == 0.0f) {
            visceraItem.mBiaoqingIcon = -1;
        } else {
            visceraItem.mBiaoqingIcon = mColorBiaoQingMap.get(visceraItem.mLevelColorRes);
        }

        return visceraItem;
    }

    /**
     * 水分
     */
    public IndexDataItem buildWaterItem() {
        float water = mWeightEntity.getWater();
        IndexDataItem waterItem = new IndexDataItem();
        waterItem.mIconRes = WeighDataParser.StandardSet.WATER.getIcon();
        waterItem.nameRes = WeighDataParser.StandardSet.WATER.getName();
        waterItem.valueText = context.getString(R.string.HaierReport_unit_percent, df.format(water));
        float[] waterStandard = WeighDataParser.getWaterStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity),
                WeighDataParser.getCalAge(roleInfo, mWeightEntity));
        float[] levelNumsWater = new float[waterStandard.length - 2];
        float max = waterStandard[waterStandard.length - 1];
        float criticalValue = waterStandard[waterStandard.length - 2];
        Log.e("AYD----->水分", "max--->" + max + "criticalValue--->" + criticalValue);

        System.arraycopy(waterStandard, 1, levelNumsWater, 0, levelNumsWater.length);
        waterItem.calLevel(max, criticalValue, water, levelNumsWater, WeighDataParser.StandardSet.WATER);

        String[] topStrWater = new String[levelNumsWater.length];
        for (int i = 0; i < levelNumsWater.length; i++) {
            topStrWater[i] = df.format(levelNumsWater[i]);
        }
        waterItem.topStr = topStrWater;
        waterItem.mBiaoqingIcon = mColorBiaoQingMap.get(waterItem.mLevelColorRes);
        return waterItem;
    }

    /**
     * 水分重量
     */
    public IndexDataItem buildWaterWeightItem() {
        float water = mWeightEntity.getWater();
        // 水含量
        float waterWeight = mWeightEntity.getWeight() * water / 100.f;
        String waterWeightStr = StandardUtil.getWeightExchangeValue(context, waterWeight, "", (byte) 1);

        IndexDataItem waterWeightItem = new IndexDataItem();
        waterWeightItem.mIconRes = WeighDataParser.StandardSet.CONTAINWATER.getIcon();
        waterWeightItem.nameRes = WeighDataParser.StandardSet.CONTAINWATER.getName();
        waterWeightItem.valueText = waterWeightStr;
        waterWeightItem.mUnitText = currentWeightUnit;
        float[] waterStandard = WeighDataParser.getWaterStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity),
                WeighDataParser.getCalAge(roleInfo, mWeightEntity));
        float[] levelNumsWater = new float[waterStandard.length - 2];

        float max = waterStandard[waterStandard.length - 1];
        float criticalValue = waterStandard[waterStandard.length - 2];
        Log.e("AYD----->水分重量", "max--->" + max + "criticalValue--->" + criticalValue);

        System.arraycopy(waterStandard, 1, levelNumsWater, 0, levelNumsWater.length);
        waterWeightItem.calLevel(max, criticalValue, water, levelNumsWater, WeighDataParser.StandardSet.WATER);

        String[] topStrWater = new String[levelNumsWater.length];
        for (int i = 0; i < levelNumsWater.length; i++) {
            topStrWater[i] = StandardUtil.getWeightExchangeValue(context, mWeightEntity.getWeight() * levelNumsWater[i] / 100, "", (byte) 1);
        }
        waterWeightItem.topStr = topStrWater;
        waterWeightItem.mBiaoqingIcon = mColorBiaoQingMap.get(waterWeightItem.mLevelColorRes);
        return waterWeightItem;
    }

    /**
     * 体重
     */
    public IndexDataItem buildWeightItem() {
        float weight = mWeightEntity.getWeight();
        IndexDataItem weightItem = new IndexDataItem();
        weightItem.mIconRes = WeighDataParser.StandardSet.WEIGHT.getIcon();
        weightItem.nameRes = WeighDataParser.StandardSet.WEIGHT.getName();
        weightItem.valueText = mWeightEntity.getDisplayWeight(context, currentWeightUnit);
        weightItem.mUnitText = currentWeightUnit;
        float[] bmiLevel = WeighDataParser.StandardSet.BMI.getLevelNums();
        float[] levelNums = new float[bmiLevel.length];
        // 单位为cm
        int height = WeighDataParser.getCalHeight(roleInfo, mWeightEntity);
        for (int i = 0; i < levelNums.length; i++) {
            levelNums[i] = bmiLevel[i] * height * height / 10000f;
        }

        weightItem.calLevel(weight, weight, weight, levelNums, WeighDataParser.StandardSet.WEIGHT);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = StandardUtil.getWeightExchangeValue(context, levelNums[i], "", (byte) 1);
        }
        weightItem.topStr = topStr;
        weightItem.mBiaoqingIcon = mColorBiaoQingMap.get(weightItem.mLevelColorRes);
        return weightItem;
    }

    /**
     * 体脂率
     */
    public IndexDataItem buildAxungeItem() {
        float axunge = mWeightEntity.getAxunge();
        IndexDataItem axungeItem = new IndexDataItem();
        axungeItem.mIconRes = WeighDataParser.StandardSet.AXUNGE.getIcon();
        axungeItem.nameRes = WeighDataParser.StandardSet.AXUNGE.getName();
        axungeItem.valueText = context.getString(R.string.HaierReport_unit_percent, df.format(axunge));
        float[] axungeStandard = WeighDataParser.getAxungeStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity),
                WeighDataParser.getCalAge(roleInfo, mWeightEntity));
        float[] levelNums = new float[axungeStandard.length - 2];
        float max = axungeStandard[axungeStandard.length - 1];
        float criticalValue = axungeStandard[axungeStandard.length - 2];
        Log.e("AYD----->体脂率", "max--->" + max + "criticalValue--->" + criticalValue);

        System.arraycopy(axungeStandard, 1, levelNums, 0, levelNums.length);
        axungeItem.calLevel(max, criticalValue, axunge, levelNums, WeighDataParser.StandardSet.AXUNGE);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = df.format(levelNums[i]);
        }
        axungeItem.topStr = topStr;
        axungeItem.mBiaoqingIcon = mColorBiaoQingMap.get(axungeItem.mLevelColorRes);
        return axungeItem;
    }

    /**
     * 脂肪重量
     */
    public IndexDataItem buildFatWeightItem() {
        float axunge = mWeightEntity.getAxunge();
        // 脂肪重量
        float axungeWeight = mWeightEntity.getWeight() * axunge / 100.f;
        String axugeWeightStr = StandardUtil.getWeightExchangeValue(context, axungeWeight, "", (byte) 1);

        IndexDataItem fatWeightItem = new IndexDataItem();
        fatWeightItem.mIconRes = WeighDataParser.StandardSet.AXUNGEWEIGHT.getIcon();
        fatWeightItem.nameRes = WeighDataParser.StandardSet.AXUNGEWEIGHT.getName();
        fatWeightItem.valueText = axugeWeightStr;
        fatWeightItem.mUnitText = currentWeightUnit;
        float[] axungeStandard = WeighDataParser.getAxungeStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity),
                WeighDataParser.getCalAge(roleInfo, mWeightEntity));
        float[] levelNums = new float[axungeStandard.length - 2];
        float max = axungeStandard[axungeStandard.length - 1];
        float criticalValue = axungeStandard[axungeStandard.length - 2];
        Log.e("AYD----->脂肪重量", "max--->" + max + "criticalValue--->" + criticalValue);
        System.arraycopy(axungeStandard, 1, levelNums, 0, levelNums.length);
        fatWeightItem.calLevel(max, criticalValue, axunge, levelNums, WeighDataParser.StandardSet.AXUNGE);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = StandardUtil.getWeightExchangeValue(context, mWeightEntity.getWeight() * levelNums[i] / 100, "", (byte) 1);
        }
        fatWeightItem.topStr = topStr;
        fatWeightItem.mBiaoqingIcon = mColorBiaoQingMap.get(fatWeightItem.mLevelColorRes);
        return fatWeightItem;
    }

    /**
     * 蛋白质
     */
    public IndexDataItem buildProteinItem() {
        float protein;
        if (mWeightEntity.getR1() > 0) {
            float slm = csAlgoBuilder.getSLMPercent(0);
            float tf = csAlgoBuilder.getTFR();

            String str_slm = df.format(slm);
            String str_tf = df.format(tf);
            protein = Float.parseFloat(str_slm) - Float.parseFloat(str_tf);

            Log.e(TAG, "time=" + mWeightEntity.getWeight_time() + ", slm=" + slm + ", tf=" + tf +
                    ", str_slm=" + str_slm + ", str_tf=" + str_tf + ", protein=" + protein);
        } else {
            protein = CsAlgoBuilder.calPM(mWeightEntity.getWeight(), csAlgoBuilder.getSex(), mWeightEntity.getAxunge(),
                    mWeightEntity.getWater());
        }

        IndexDataItem proteinItem = new IndexDataItem();
        proteinItem.mIconRes = WeighDataParser.StandardSet.PROTEIN.getIcon();
        proteinItem.nameRes = WeighDataParser.StandardSet.PROTEIN.getName();
        proteinItem.valueText = context.getString(R.string.HaierReport_unit_percent, df.format(protein));
        float[] levelNums = WeighDataParser.StandardSet.PROTEIN.getLevelNums();

        float max = levelNums[levelNums.length - 1];
        float criticalValue = levelNums[levelNums.length - 2];
        Log.e("AYD----->蛋白质", "max--->" + max + "criticalValue--->" + criticalValue);
        proteinItem.calLevel(max, criticalValue, protein, levelNums, WeighDataParser.StandardSet.PROTEIN);
        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = (int) levelNums[i] + "";
        }
        proteinItem.topStr = topStr;

        proteinItem.mBiaoqingIcon = mColorBiaoQingMap.get(proteinItem.mLevelColorRes);

        return proteinItem;
    }

    /**
     * 骨量
     */
    public IndexDataItem buildBoneItem() {
        float bone = mWeightEntity.getBone();
        IndexDataItem boneItem = new IndexDataItem();
        boneItem.mIconRes = WeighDataParser.StandardSet.BONE.getIcon();
        boneItem.nameRes = WeighDataParser.StandardSet.BONE.getName();
        boneItem.valueText = StandardUtil.getWeightExchangeValue(context, bone, "", (byte) 1);
        boneItem.mUnitText = currentWeightUnit;
        float[] boneStandard = WeighDataParser.getBoneStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity),
                WeighDataParser.getCalAge(roleInfo, mWeightEntity));
        float[] levelNums = new float[boneStandard.length - 2];

        float max = boneStandard[boneStandard.length - 1];
        float criticalValue = boneStandard[boneStandard.length - 2];
        Log.e("AYD----->骨量", "max--->" + max + "criticalValue--->" + criticalValue);
        System.arraycopy(boneStandard, 1, levelNums, 0, levelNums.length);
        boneItem.calLevel(max, criticalValue, bone, levelNums, WeighDataParser.StandardSet.BONE);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = StandardUtil.getWeightExchangeValue(context, levelNums[i], "", (byte) 1);
        }

        boneItem.topStr = topStr;
        boneItem.mBiaoqingIcon = mColorBiaoQingMap.get(boneItem.mLevelColorRes);
        return boneItem;
    }

    /**
     * 肌肉率
     */
    public IndexDataItem buildMuscleItem() {
        float muscle = mWeightEntity.getMuscle() / 2;
        IndexDataItem muscleItem = new IndexDataItem();
        muscleItem.mIconRes = WeighDataParser.StandardSet.MUSCLE.getIcon();
        muscleItem.nameRes = WeighDataParser.StandardSet.MUSCLE.getName();
        muscleItem.valueText = context.getString(R.string.HaierReport_unit_percent, df.format(muscle));
        float[] muscleStandard = WeighDataParser.getMuscleStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity));
        float[] levelNums = new float[muscleStandard.length - 2];
        float max = muscleStandard[muscleStandard.length - 1];
        float criticalValue = muscleStandard[muscleStandard.length - 2];
        Log.e("AYD----->肌肉率", "max--->" + max + "criticalValue--->" + criticalValue);
        System.arraycopy(muscleStandard, 1, levelNums, 0, levelNums.length);
        muscleItem.calLevel(max, criticalValue, muscle, levelNums, WeighDataParser.StandardSet.MUSCLE);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = (int) levelNums[i] + "";
        }

        muscleItem.topStr = topStr;
        muscleItem.mBiaoqingIcon = mColorBiaoQingMap.get(muscleItem.mLevelColorRes);
        return muscleItem;
    }

    /**
     * 肌肉重量
     */
    public IndexDataItem buildMuscleWeightItem() {
        float muscle = mWeightEntity.getMuscle();

        // 肌肉重量
        float muscleWeight = mWeightEntity.getWeight() * muscle / 200.f;
        String muscleWeightStr = StandardUtil.getWeightExchangeValue(context, muscleWeight, "", (byte) 1);

        IndexDataItem muscleWeightItem = new IndexDataItem();
        muscleWeightItem.mIconRes = WeighDataParser.StandardSet.MUSCLEWEIGHT.getIcon();
        muscleWeightItem.nameRes = WeighDataParser.StandardSet.MUSCLEWEIGHT.getName();
        muscleWeightItem.valueText = muscleWeightStr;
        muscleWeightItem.mUnitText = currentWeightUnit;
        float[] muscleStandard = WeighDataParser.getMuscleStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity));
        float[] levelNums = new float[muscleStandard.length - 2];

        float max = muscleStandard[muscleStandard.length - 1];
        float criticalValue = muscleStandard[muscleStandard.length - 2];

        Log.e("AYD----->肌肉重量", "max--->" + max + "criticalValue--->" + criticalValue);
        System.arraycopy(muscleStandard, 1, levelNums, 0, levelNums.length);
        muscleWeightItem.calLevel(max, criticalValue, muscle, levelNums, WeighDataParser.StandardSet.MUSCLE);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = StandardUtil.getWeightExchangeValue(context, mWeightEntity.getWeight() * levelNums[i] / 100, "", (byte) 1);
        }

        muscleWeightItem.topStr = topStr;
        muscleWeightItem.mBiaoqingIcon = mColorBiaoQingMap.get(muscleWeightItem.mLevelColorRes);
        return muscleWeightItem;
    }

    /**
     * 基础代谢
     */
    public IndexDataItem buildMetabolismItem() {
        float metabolism = mWeightEntity.getMetabolism();
        IndexDataItem metabolismItem = new IndexDataItem();
        metabolismItem.mIconRes = WeighDataParser.StandardSet.METABOLISM.getIcon();
        metabolismItem.nameRes = WeighDataParser.StandardSet.METABOLISM.getName();
        metabolismItem.valueText = df.format(metabolism);
        metabolismItem.mUnitText = context.getString(R.string.HaierReport_unit_car);
        float[] metabolismStandard = WeighDataParser.getMetabolismStandardRange(WeighDataParser.getCalSex(roleInfo, mWeightEntity),
                WeighDataParser.getCalAge(roleInfo, mWeightEntity));
        float[] levelNums = new float[metabolismStandard.length - 2];

        float max = metabolismStandard[metabolismStandard.length - 1];
        float criticalValue = metabolismStandard[metabolismStandard.length - 2];
        Log.e("AYD----->基础代谢", "max--->" + max + "criticalValue--->" + criticalValue);
        System.arraycopy(metabolismStandard, 1, levelNums, 0, levelNums.length);
        metabolismItem.calLevel(max, criticalValue, metabolism, levelNums, WeighDataParser.StandardSet.METABOLISM);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = df.format(levelNums[i]);
        }

        metabolismItem.topStr = topStr;
        metabolismItem.mBiaoqingIcon = mColorBiaoQingMap.get(metabolismItem.mLevelColorRes);
        return metabolismItem;
    }

    /**
     * 肥胖度
     */
    public IndexDataItem buildCorpulentItem() {
        float corpulent;
        if (mWeightEntity.getR1() > 0) {
            corpulent = csAlgoBuilder.getOD();
        } else {
            corpulent = CsAlgoBuilder.calOD(csAlgoBuilder.getH(), mWeightEntity.getWeight(), csAlgoBuilder.getSex(),
                    csAlgoBuilder.getAge());
        }

        IndexDataItem corpulentItem = new IndexDataItem();
        corpulentItem.mIconRes = WeighDataParser.StandardSet.CORPULENT.getIcon();
        corpulentItem.nameRes = WeighDataParser.StandardSet.CORPULENT.getName();
        corpulentItem.valueText = context.getString(R.string.HaierReport_unit_percent, df.format(corpulent));
        float[] levelNums = WeighDataParser.StandardSet.CORPULENT.getLevelNums();

        float max = levelNums[levelNums.length - 1];
        float criticalValue = levelNums[levelNums.length - 3];
        Log.e("AYD----->肥胖度", "max--->" + max + "criticalValue--->" + criticalValue);
        corpulentItem.calLevel(max, criticalValue, corpulent, levelNums, WeighDataParser.StandardSet.CORPULENT);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = levelNums[i] + "";
        }

        corpulentItem.topStr = topStr;
        corpulentItem.mBiaoqingIcon = mColorBiaoQingMap.get(corpulentItem.mLevelColorRes);

        return corpulentItem;
    }

    /**
     * BMI
     */
    public IndexDataItem buildBMIItem() {
        float bmi = mWeightEntity.getBmi();
        IndexDataItem bmiItem = new IndexDataItem();
        bmiItem.mIconRes = WeighDataParser.StandardSet.BMI.getIcon();
        bmiItem.nameRes = WeighDataParser.StandardSet.BMI.getName();
        bmiItem.valueText = df.format(bmi);
        float[] levelNums = WeighDataParser.StandardSet.BMI.getLevelNums();

        float max = levelNums[levelNums.length - 1];
        float criticalValue = levelNums[levelNums.length - 2];

        Log.e("AYD----->BMI", "max--->" + max + "criticalValue--->" + criticalValue);
        bmiItem.calLevel(max, criticalValue, bmi, levelNums, WeighDataParser.StandardSet.BMI);

        String[] topStr = new String[levelNums.length];
        for (int i = 0; i < levelNums.length; i++) {
            topStr[i] = df.format(levelNums[i]);
        }

        bmiItem.topStr = topStr;
        bmiItem.mBiaoqingIcon = mColorBiaoQingMap.get(bmiItem.mLevelColorRes);
        return bmiItem;
    }

    /**
     * 身体年龄
     */
    public IndexDataItem buildBodyAgeItem() {
        int bodyAge;
        if (mWeightEntity.getR1() > 0) {
            bodyAge = csAlgoBuilder.getBodyAge();
        } else {
            bodyAge = CsAlgoBuilder.calBodyAge(csAlgoBuilder.getH(), mWeightEntity.getWeight(), csAlgoBuilder.getSex(),
                    csAlgoBuilder.getAge(), mWeightEntity.getAxunge());
        }

        IndexDataItem bodyAgeItem = new IndexDataItem();
        bodyAgeItem.mIconRes = R.mipmap.icon_body_age;
        bodyAgeItem.nameRes = R.string.reportBodyAge;
        bodyAgeItem.valueText = bodyAge + "";
        bodyAgeItem.mUnitText = "岁";
        return bodyAgeItem;
    }

    /**
     * 去脂体重
     */
    public IndexDataItem buildThinItem() {
        float thinWeight = mWeightEntity.getWeight() * (1 - mWeightEntity.getAxunge() / 100f);
        IndexDataItem thinItem = new IndexDataItem();
        thinItem.mIconRes = WeighDataParser.StandardSet.THIN.getIcon();
        thinItem.nameRes = WeighDataParser.StandardSet.THIN.getName();
        thinItem.valueText = StandardUtil.getWeightExchangeValue(context, thinWeight, "", (byte) 1);
        thinItem.mUnitText = currentWeightUnit;
        return thinItem;
    }

}
