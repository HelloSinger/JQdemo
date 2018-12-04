package com.jq.btc.helper;

import android.content.Context;

import com.jq.btc.app.R;
import com.jq.btc.bluettooth.report.haier.item.ReportDetalis;
import com.jq.btc.bluettooth.report.haier.item.Rn8Item;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.code.code.algorithm.CsAlgoBuilder;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.util.DecimalFormatUtils;
import com.jq.code.code.util.PrefsUtil;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.DataType;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hfei on 2016/5/3.
 */
public class WeighDataParser extends PrefsUtil {

    private static Context context;
    private static WeighDataParser instance;
    private Account mAccount;

    public static WeighDataParser create(Context context) {
        if (instance == null) {
            synchronized (WeighDataParser.class) {
                if (instance == null) {
                    instance = new WeighDataParser(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private WeighDataParser(Context context) {
        super(context);
        this.context = context;
        mAccount = Account.getInstance(context);
    }

    public void fillFatWithImpedance(ArrayList<PutBase> fatScales, RoleInfo roleInfo) {
        for (PutBase entity : fatScales) {
            fillFatWithImpedance((WeightEntity) entity, roleInfo);
        }
    }

    /*
    * 增加滤波功能
    * */
    public void fillFatWithSmoothImpedance(WeightEntity fatScale, RoleInfo roleInfo) {
        try {
            fatScale.setBmi(getBMI(roleInfo.getHeight(), fatScale.getWeight()));
            fatScale.setRole_id(roleInfo.getId());
            fatScale.setAccount_id(roleInfo.getAccount_id());
            if (roleInfo.getRole_type() == 1) {
                //孕妇模式
                fatScale.setSex(2);
                fatScale.setR1(0);
            } else {
                fatScale.setSex(roleInfo.getSex().equals(context.getString(R.string.women)) ? 0 : 1);
            }

            fatScale.setHeight(roleInfo.getHeight());
            fatScale.setAge(TimeUtil.getAgeThroughBirthday(roleInfo.getBirthday()));
            if (fatScale.getR1() == 0) return;
            byte sex = (byte) fatScale.getSex();
            int age = fatScale.getAge();
            float weight = fatScale.getWeight();
            float height = roleInfo.getHeight();
            if (age > 5 && roleInfo.getRole_type() == 0) {
                WeightEntity lastWeightData = WeightDataDB.getInstance(context).findLastRoleDataByRoleId(roleInfo);
                CsAlgoBuilder csAlgoBuilder;
                if (lastWeightData == null || lastWeightData.getR1() == 0) {
                    if (fatScale.getRn8() != null && fatScale.getRn8().trim().length() > 0) {
                        csAlgoBuilder = new CsAlgoBuilder(height, weight, sex, age, fatScale.getR1(), fatScale.getRn8());
                    } else {
                        csAlgoBuilder = new CsAlgoBuilder(height, weight, sex, age, fatScale.getR1());
                    }
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    if (fatScale.getRn8() != null && fatScale.getRn8().trim().length() > 0) {
                        csAlgoBuilder = new CsAlgoBuilder(height, sex, age, weight, fatScale.getR1(), fatScale.getRn8(), sdf.parse(fatScale.getWeight_time()),
                                lastWeightData.getR1(), lastWeightData.getRn8(), sdf.parse(lastWeightData.getWeight_time()));
                        fatScale.setR1(csAlgoBuilder.getZ());
                        fatScale.setRn8(csAlgoBuilder.getRemark());
                    } else {
                        csAlgoBuilder = new CsAlgoBuilder(height, sex, age, weight, fatScale.getR1(), sdf.parse(fatScale.getWeight_time()), lastWeightData.getR1(), sdf.parse(lastWeightData.getWeight_time()));
                        fatScale.setR1(csAlgoBuilder.getZ());
                    }
                }
                fatScale.setAxunge(csAlgoBuilder.getBFR());
                fatScale.setViscera(csAlgoBuilder.getVFR());
                fatScale.setWater(csAlgoBuilder.getTFR());

                fatScale.setBone(csAlgoBuilder.getMSW());
                fatScale.setMuscle(csAlgoBuilder.getSLMPercent(0));
                fatScale.setMetabolism(csAlgoBuilder.getBMR());
                fatScale.setBody_age(csAlgoBuilder.getBodyAge());
                fatScale.setScore((int) csAlgoBuilder.getScore());
                fatScale.setBw(csAlgoBuilder.getBW());
            } else {
                fatScale.setAxunge(0);
                fatScale.setViscera(0);
                fatScale.setWater(0);
                fatScale.setBone(0);
                fatScale.setMuscle(0);
                fatScale.setMetabolism(0);
                fatScale.setBody_age(0);
                fatScale.setScore(0);
                fatScale.setBw(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void fillFatWithImpedance(WeightEntity fatScale, RoleInfo roleInfo) {
        fatScale.setBmi(getBMI(roleInfo.getHeight(), fatScale.getWeight()));
        fatScale.setRole_id(roleInfo.getId());
        fatScale.setAccount_id(roleInfo.getAccount_id());
        if (roleInfo.getRole_type() == 1) {
            fatScale.setSex(2);
        } else {
            fatScale.setSex(roleInfo.getSex().equals(context.getString(R.string.women)) ? 0 : 1);
        }
        fatScale.setHeight(roleInfo.getHeight());
        fatScale.setAge(TimeUtil.getAgeThroughBirthday(roleInfo.getBirthday()));
        if (fatScale.getR1() == 0) return;
        byte sex = (byte) fatScale.getSex();
        int age = fatScale.getAge();
        float weight = fatScale.getWeight();
        float height = roleInfo.getHeight();
        if (age > 5) {
           // CsAlgoBuilder csAlgoBuilder = new CsAlgoBuilder(height, weight, sex, age, fatScale.getR1());
            CsAlgoBuilder csAlgoBuilder = new CsAlgoBuilder(height, weight, sex, age, fatScale.getR1(), fatScale.getRn8());
            fatScale.setAxunge(csAlgoBuilder.getBFR());
            fatScale.setViscera(csAlgoBuilder.getVFR());
            fatScale.setWater(csAlgoBuilder.getTFR());

            fatScale.setBone(csAlgoBuilder.getMSW());
            fatScale.setMuscle(csAlgoBuilder.getSLMPercent(0));
            fatScale.setMetabolism(csAlgoBuilder.getBMR());
            fatScale.setBody_age(csAlgoBuilder.getBodyAge());
            fatScale.setScore((int) csAlgoBuilder.getScore());
            fatScale.setBw(csAlgoBuilder.getBW());
        } else {
            fatScale.setAxunge(0);
            fatScale.setViscera(0);
            fatScale.setWater(0);
            fatScale.setBone(0);
            fatScale.setMuscle(0);
            fatScale.setMetabolism(0);
            fatScale.setBody_age(0);
            fatScale.setScore(0);
            fatScale.setBw(0);
        }
        return;
    }

    /**
     * 处理手动添加数据
     *
     * @param value
     * @param scaleValue
     * @param scaleProperty
     * @return
     */
    public WeightEntity onJustWeight(float value, String scaleValue, byte scaleProperty) {
        WeightEntity weightEntity = new WeightEntity();
        weightEntity.setAccount_id(mAccount.getAccountInfo().getId());
        weightEntity.setWeight(value);
        weightEntity.setScaleweight(scaleValue);
        weightEntity.setScaleproperty(scaleProperty);
        //约定101代表手工添加数据;
        weightEntity.setProductid(101);
        weightEntity.setWeight_time(TimeUtil
                .getCurrentTime(TimeUtil.TIME_FORMAT1));
        return weightEntity;
    }


    /**
     * 将称重数据转换成角色数据 适用于人体秤
     *
     * @param weight
     * @return
     */
    public WeightEntity csWeightRoleDataInfo(float weight, String scaleWeight, byte scaleProp, float r1, int devicieType) {
        WeightEntity info = new WeightEntity();
        info.setAccount_id(mAccount.getAccountInfo().getId());
        info.setWeight(weight);
        info.setScaleweight(scaleWeight);
        info.setScaleproperty(scaleProp);
        info.setProductid(ScaleParser.getInstance(context).getScale().getProduct_id());
        info.setWeight_time(TimeUtil.getCurDateAndTime());
        info.setMeasure_time(info.getWeight_time());
        info.setMtype(DataType.WEIGHT.getType());
        info.setSync_time("0000-00-00 00:00:00");
        if (devicieType == 2) {
            info.setAxunge(-1);
        } else {
            info.setAxunge(0);
        }
        info.setR1(r1);
        return info;
    }

    /**
     * 将称重数据转换成角色数据 适用于脂肪秤
     *
     * @param scale
     * @return
     */
    public WeightEntity csFatRoleDataInfo(CsFatScale scale) {
        WeightEntity info = new WeightEntity();
        info.setAccount_id(mAccount.getAccountInfo().getId());
        info.setRole_id(scale.getRoleId());
        info.setWeight((float) scale.getWeight());
        info.setWeight_time(TimeUtil.getCurDateAndTime());
        info.setSync_time("0000-00-00 00:00:00");
        info.setAxunge(((scale.getAxunge() == 0 && scale.getDeviceType() != 1) ? -1 : (float) scale.getAxunge()));
        info.setBody_age((float) scale.getAge());
        info.setBone((float) scale.getBone());
        info.setMetabolism((float) scale.getBmr());
        info.setMuscle((float) scale.getMuscle());
        info.setViscera((float) scale.getVisceral_fat());
        info.setWater((float) scale.getWater());
        info.setR1(scale.getImpedance());
        info.setScaleproperty(scale.getScaleProperty());
        info.setScaleweight(scale.getScaleWeight());
        info.setProductid(ScaleParser.getInstance(context).getScale().getProduct_id());
        info.setRn8(scale.getRemark());
        return info;
    }


    /**
     * 获取上周周比数据
     *
     * @return
     */
    public String getLastWeekCompare(WeightEntity weightEntity) {
        lastWeekCompareTip = "";
        if (!mAccount.isAccountLogined() || weightEntity == null)
            return "- -";
        String[] range = TimeUtil.getWeekDateRange(weightEntity.getWeight_time());
        String startTime = TimeUtil.minusDay(range[0], 7);
        String endTime = TimeUtil.minusDay(range[1], 7);
        WeightEntity lastAvg = WeightDataDB.getInstance(context).getAvg(weightEntity.getAccount_id(), weightEntity.getRole_id(), startTime + " 00:00:00", endTime + " 23:59:59");
        if (lastAvg != null) {
            float sum = weightEntity.getWeight() - lastAvg.getWeight();
            if (sum < 0) {
                float f1 = Math.abs(sum);
                String unitValue = StandardUtil.getWeightExchangeValue(context, f1, "", weightEntity.getScaleproperty());
                lastWeekCompareTip = context.getString(R.string.main_lastWeekCompareTip1);
                return unitValue;
            } else if (sum == 0) {
                lastWeekCompareTip = context.getString(R.string.main_lastWeekCompareTip1);
                return "" + sum;
            } else {
                String unitValue = StandardUtil.getWeightExchangeValue(context, sum, "", weightEntity.getScaleproperty());
                lastWeekCompareTip = context.getString(R.string.main_lastWeekCompareTip2);
                return unitValue;
            }
        }
        return "- -";
    }

    private String lastWeekCompareTip;

    public String getLastWeekCompareTip() {
        return lastWeekCompareTip;
    }

    /**
     * 计算数据分数
     */
    public int calculateCode(WeightEntity info, RoleInfo curRoleInfo) {
        if (info.getScore() > 0) {
            return info.getScore();
        } else {
            float score = 0;
            if (info.getAxunge() > 0.0f) {
                byte sex = (byte) (getCalSex(curRoleInfo, info).equals("男") ? 1 : 0);
                int age = getCalAge(curRoleInfo, info);
                int height = getCalHeight(curRoleInfo, info);
                float slm = info.getWeight() * (info.getMuscle() / 100.0f);

                if (info.getR1() > 0) {
                    score = CsAlgoBuilder.calScore(height, info.getWeight(), sex, age, info.getAxunge(), slm, info.getViscera());
                } else {
                    score = CsAlgoBuilder.calScore(height, info.getWeight(), sex, age, info.getAxunge(), slm, 0);
                }
            }
            return (int) score;
        }

//        float bmi = getBmiCode(info);
//        float axunge = getAxungeCode(curRoleInfo, info);
//        float bone = getBoneCode(curRoleInfo, info);
//        float metabolism = getMetabolismCode(curRoleInfo, info);
//        float muscle = getMuscleCode(curRoleInfo, info);
//        float viscera = getVisceraCode(info);
//        float water = getWaterCode(curRoleInfo, info);
//        return Math.round(bmi * 4 + axunge * 4f + 0.4f * (bone + metabolism + muscle + viscera + water));

    }

    public static float getOd(WeightEntity info) {
        //测量体重
        float wt = info.getWeight();
        //标准体重
        float bw = info.getBw();
        if (bw == 0) {
            return 0;
        }
        float od = ((wt - bw) / bw) * 100;
        return Float.parseFloat(DecimalFormatUtils.getTwo(od));
    }

    public static float getThinWeight(WeightEntity info, RoleInfo roleInfo) {
        //测量体重
        float weight = info.getWeight();
        //瘦体重
        float thinWeight = weight * (1 - info.getAxunge() / 100);

        return roleInfo.getSex().equals(context.getString(R.string.man)) ? Float.parseFloat(DecimalFormatUtils.getTwo(thinWeight)) : 0;
    }

    public static float getWaterWeight(WeightEntity info) {
        float waterWeight = info.getWeight() * info.getWater() / 100;
        return waterWeight;
    }

    public static float getProtein(WeightEntity info) {
        float protein = info.getMuscle() - info.getWater();
        return Float.parseFloat(DecimalFormatUtils.getTwo(protein));
    }

    public static int getProteinLevel(float protenin) {
        if (protenin < 16) {
            return 1;
        } else if (protenin >= 16 && protenin <= 20) {
            return 2;
        } else {
            return 3;
        }
    }

    public static boolean judgeRlAndBw(WeightEntity mRoleDataInfo) {
        if (mRoleDataInfo.getR1() > 0 && mRoleDataInfo.getBw() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int getCorpulentLevel(float od) {
        if (od <= 10 && od >= -10) {
            return 0;
        } else if (od >= 10 && od <= 20) {
            return 1;
        } else if (od >= 20 && od <= 30) {
            return 2;
        } else if (od >= 30 && od <= 50) {
            return 3;
        } else if (od > 50) {
            return 4;
        } else {
            return 0;
        }
    }

    public static int getCorpulentLevelForRDetail(float od) {
        if (od < -10 && od >= -20) {
            return 1;
        } else if (od <= 10 && od >= -10) {
            return 2;
        } else if (od >= 10 && od <= 20) {
            return 3;
        } else if (od >= 20 && od <= 30) {
            return 4;
        } else if (od >= 30 && od <= 50) {
            return 5;
        } else if (od > 50) {
            return 6;
        } else {
            return 0;
        }
    }

    public static int getThinLevel(float thinWeight, RoleInfo roleInfo) {
        float height = roleInfo.getHeight() / 100.0f;
        if (thinWeight <= 18 * height * height) {
            return 1;
        } else if (thinWeight >= 18 * height * height && thinWeight <= 20 * height * height) {
            return 2;
        } else if (thinWeight >= 20 * height * height && thinWeight <= 23 * height * height) {
            return 3;
        } else if (thinWeight >= 23 * height * height) {
            return 4;
        }
        return 1;
    }

    /**
     * 获得当前bmi的参数的等级
     */
    public static int getBmiLevel(WeightEntity info) {
        float bmi = info.getBmi();
        float[] levelNums = WeighDataParser.
                StandardSet.BMI.getLevelNums();
        int bmiLevel = 0;
        for (int i = 0; i < levelNums.length; i++) {
            if (bmi < levelNums[i]) {
                break;
            } else {
                bmiLevel++;
            }
        }
        // 因为调用的地方都减1
        return bmiLevel + 1;
    }

    /**
     * 获得bmi分数
     *
     * @param info
     * @return
     */
    public static float getBmiCode(WeightEntity info) {
        float bmi = info.getBmi();
        float code;
        float[] bmiStandardRange = getBMIStandardRange();
        if (bmi <= 0) {
            code = 5;
        } else if (bmi <= bmiStandardRange[0]) {
            code = 1;
        } else if (bmi < bmiStandardRange[1]) {
            code = (4 * (bmi - bmiStandardRange[0]) / (bmiStandardRange[1] - bmiStandardRange[0])) + 1;
        } else if (bmi <= bmiStandardRange[2]) {
            code = 5;
        } else if (bmi <= bmiStandardRange[3]) {
            code = (2 * (bmiStandardRange[3] - bmi) / (bmiStandardRange[3] - bmiStandardRange[2])) + 3;
        } else if (bmi <= bmiStandardRange[4]) {
            code = (2 * (bmiStandardRange[4] - bmi) / (bmiStandardRange[4] - bmiStandardRange[3])) + 1;
        } else {
            code = 1;
        }
        return code / 5 * (100 / 10);
    }

    /**
     * 获得当前Axunge的参数的等级
     */
    public static int getAxungeLevel(RoleInfo roleInfo, WeightEntity info) {
        float[] axungeStandardRange = getAxungeStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        if (info.getAxunge() < axungeStandardRange[1]) {
            return 1;
        } else if (info.getAxunge() < axungeStandardRange[2]) {
            return 2;
        } else if (info.getAxunge() < axungeStandardRange[3]) {
            return 3;
        } else {
            return 4;
        }
    }

    public static float getAxungeCode(RoleInfo roleInfo, WeightEntity info) {
        float axunge = info.getAxunge();
        float code;
        float[] axungeStandardRange = getAxungeStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        if (axunge <= 0) {
            code = 5;
        } else if (axunge <= axungeStandardRange[0]) {
            code = 1;
        } else if (axunge < axungeStandardRange[1]) {
            code = (4 * (axunge - axungeStandardRange[0]) / (axungeStandardRange[1] - axungeStandardRange[0])) + 1;
        } else if (axunge <= axungeStandardRange[2]) {
            code = 5;
        } else if (axunge <= axungeStandardRange[3]) {
            code = (2 * (axungeStandardRange[3] - axunge) / (axungeStandardRange[3] - axungeStandardRange[2])) + 3;
        } else if (axunge <= axungeStandardRange[4]) {
            code = (2 * (axungeStandardRange[4] - axunge) / (axungeStandardRange[4] - axungeStandardRange[3])) + 1;
        } else {
            code = 1;
        }
        return code / 5 * (100 / 10);
    }

    /**
     * 获得当前水分的参数的等级
     */
    public static int getWaterLevel(RoleInfo roleInfo, WeightEntity info) {
        float[] waterStandardRange = getWaterStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        if (info.getWater() < waterStandardRange[1]) {
            return 1;
        } else if (info.getWater() <= waterStandardRange[2]) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 获得当前水分的分数
     */
    public static float getWaterCode(RoleInfo roleInfo, WeightEntity info) {
        float[] waterStandardRange = getWaterStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        float water = info.getWater();
        float code = 1;
        if (water <= 0) {
            code = 5;
        } else if (water <= waterStandardRange[0]) {
            code = 1;
        } else if (water < waterStandardRange[1]) {
            code = (4 * (water - waterStandardRange[0]) / (waterStandardRange[1] - waterStandardRange[0])) + 1;
        } else {
            code = 5;
        }
        return code / 5 * (100 / 10);
    }

    /**
     * 获得当前肌肉的参数的等级
     */
    public static int getMuscleLevel(RoleInfo roleInfo, WeightEntity info) {
        float[] muscleStandardRange = getMuscleStandardRange(getCalSex(roleInfo, info));
        if (info.getMuscle() < muscleStandardRange[1]) {
            return 1;
        } else if (info.getMuscle() <= muscleStandardRange[2]) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 获得当前肌肉的分数
     */
    public static float getMuscleCode(RoleInfo roleInfo, WeightEntity info) {
        float[] muscleStandardRange = getMuscleStandardRange(getCalSex(roleInfo, info));
        float muscle = info.getMuscle();
        float code = 1;
        if (muscle <= 0) {
            code = 5;
        } else if (muscle <= muscleStandardRange[0]) {
            code = 1;
        } else if (muscle < muscleStandardRange[1]) {
            code = (4 * (muscle - muscleStandardRange[0]) / (muscleStandardRange[1] - muscleStandardRange[0])) + 1;
        } else {
            code = 5;
        }
        return code / 5 * (100 / 10);
    }

    /**
     * 获得当前内脏的参数的等级
     */
    public static int getVisceraLevel(WeightEntity info) {
        float value = info.getViscera();
        float[] visceraStandardRange = getVisceraStandardRange();
        if (value <= visceraStandardRange[1]) {
            return 1;
        } else if (value <= visceraStandardRange[2]) {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     * 获得当前内脏的参数的等级
     */
    public static float getVisceraCode(WeightEntity info) {
        float[] visceraStandardRange = getVisceraStandardRange();
        float viscera = info.getViscera();
        float code = 1;
        if (viscera <= 0) {
            code = 5;
        } else if (viscera <= visceraStandardRange[1]) {
            code = 5;
        } else if (viscera >= visceraStandardRange[3]) {
            code = 1;
        } else {
            code = (4 * (visceraStandardRange[3] - viscera) / (visceraStandardRange[3] - visceraStandardRange[1])) + 1;
        }
        return code / 5 * (100 / 10);
    }

    /**
     * 获得新城代谢标准 1 低 2 正常 3 高
     */
    public static int getMetabolismLevel(RoleInfo roleInfo, WeightEntity info) {
        float[] metabolismStandardRange = getMetabolismStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        if (info.getMetabolism() < metabolismStandardRange[1]) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * 获得新城代谢分数
     */
    /**
     * 获得新城代谢标准 1 低 2 正常 3 高
     */
    public static float getMetabolismCode(RoleInfo roleInfo, WeightEntity info) {
        float[] metabolismStandardRange = getMetabolismStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        float metabolism = info.getMetabolism();
        float code = 1;
        if (metabolism <= 0) {
            code = 5;
        } else if (metabolism <= metabolismStandardRange[0]) {
            code = 1;
        } else if (metabolism < metabolismStandardRange[1]) {
            code = (4 * (metabolism - metabolismStandardRange[0]) / (metabolismStandardRange[1] - metabolismStandardRange[0])) + 1;
        } else {
            code = 5;
        }
        return code / 5 * (100 / 10);
    }

    /**
     * 获得当前内脏的参数的等级
     */
    public static int getBoneLevel(RoleInfo roleInfo, WeightEntity info) {
        float value = info.getBone();
        float[] boneStandardRange = getBoneStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        if (value < boneStandardRange[1]) {
            return 1;
        } else if (value <= boneStandardRange[2]) {
            return 2;
        } else {
            return 3;
        }
    }

    public static float getBoneCode(RoleInfo roleInfo, WeightEntity info) {
        float[] boneStandardRange = getBoneStandardRange(getCalSex(roleInfo, info), getCalAge(roleInfo, info));
        float bone = info.getBone();
        float code = 1;
        if (bone <= 0) {
            code = 5;
        } else if (bone <= boneStandardRange[0]) {
            code = 1;
        } else if (bone < boneStandardRange[1]) {
            code = (4 * (bone - boneStandardRange[0]) / (boneStandardRange[1] - boneStandardRange[0])) + 1;
        } else {
            code = 5;
        }
        return code / 5 * (100 / 10);
    }

    /**
     * 获得bmi的数值
     *
     * @param height cm级别 如 170cm
     * @param weight
     */
    public static float getBMI(int height, double weight) {
        if (height == 0 || (int) weight == 0) {
            return 0;
        }
        float bmi_result;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        double height_double = height;
        height_double = height_double / 100;
        bmi_result = Float.valueOf(new DecimalFormat("##0.0", symbols)
                .format((weight / (height_double * height_double))));
        return bmi_result;
    }

    /**
     * 获得的体重标准范围
     */
    public static float[] getWeightStandard(float height) {
        float[] standardRange = new float[2];
        standardRange[0] = getWeightFromBmi(height, 18.5f);
        standardRange[1] = getWeightFromBmi(height, 24.0f);
        return standardRange;
    }

    public static float getWeightFromBmi(float height, float bmi) {
        return height * height / 10000.0f * bmi;
    }

    /**
     * 获取基础代谢标准范围值
     *
     * @return
     */
    public static float[] getMetabolismStandardRange(String calSex, int age) {
        int tmpvalue = 0;
        if (calSex.equals("男")) {
            if (age <= 2) {
                tmpvalue = 700;
            } else if (age <= 5) {
                tmpvalue = 900;
            } else if (age <= 8) {
                tmpvalue = 1090;
            } else if (age <= 11) {
                tmpvalue = 1290;
            } else if (age <= 14) {
                tmpvalue = 1480;
            } else if (age <= 17) {
                tmpvalue = 1610;
            } else if (age <= 29) {
                tmpvalue = 1550;
            } else if (age <= 49) {
                tmpvalue = 1500;
            } else if (age <= 69) {
                tmpvalue = 1350;
            } else {
                tmpvalue = 1220;
            }
        } else {
            if (age <= 2) {
                tmpvalue = 700;
            } else if (age <= 5) {
                tmpvalue = 860;
            } else if (age <= 8) {
                tmpvalue = 1000;
            } else if (age <= 11) {
                tmpvalue = 1180;
            } else if (age <= 14) {
                tmpvalue = 1340;
            } else if (age <= 17) {
                tmpvalue = 1300;
            } else if (age <= 29) {
                tmpvalue = 1210;
            } else if (age <= 49) {
                tmpvalue = 1170;
            } else if (age <= 69) {
                tmpvalue = 1110;
            } else {
                tmpvalue = 1010;
            }
        }
        return new float[]{tmpvalue * 0.6f, tmpvalue, tmpvalue * 2f};
    }

    /**
     * 获取体重标准范围值
     *
     * @return
     */
    public static float[] getWeightStandardRange(int calHeight) {
        float[] bmiStandardRange = getBMIStandardRange();
        float precent1 = WeighDataParser.getWeightFromBmi(calHeight, bmiStandardRange[0]);
        float precent2 = WeighDataParser.getWeightFromBmi(calHeight, bmiStandardRange[1]);
        float precent3 = WeighDataParser.getWeightFromBmi(calHeight, bmiStandardRange[2]);
        float precent4 = WeighDataParser.getWeightFromBmi(calHeight, bmiStandardRange[3]);
        float precent5 = WeighDataParser.getWeightFromBmi(calHeight, bmiStandardRange[4]);
        return new float[]{precent1, precent2, precent3, precent4, precent5};
    }

    /**
     * 获取水分标准范围值
     *
     * @return
     */
    public static float[] getWaterStandardRange(String calSex, int age) {
        float[] range = null;
        if (calSex.equals("男")) {
            if (age <= 30) {
                range = new float[]{37.8f, 53.6f, 57.0f, 66.0f};
            } else {
                range = new float[]{37.8f, 52.3f, 55.6f, 66.0f};
            }
        } else {
            if (age <= 30) {
                range = new float[]{37.8f, 49.5f, 52.9f, 66.0f};
            } else {
                range = new float[]{37.8f, 48.1f, 51.5f, 66.0f};
            }
        }
        return range;
    }

    /**
     * 获取内脏脂肪标准范围值
     *
     * @return
     */
    public static float[] getVisceraStandardRange() {
        return new float[]{1f, 9f, 14f, 30f};
    }

    /**
     * 获取肌肉标准范围值
     *
     * @return
     */
    public static float[] getMuscleStandardRange(String calSex) {
        float[] ranges = null;
        if (calSex.equals("男")) {
            ranges = new float[]{26f, 31f, 39f, 45.0f};
        } else {
            ranges = new float[]{21f, 25f, 30f, 35f};
        }
        return ranges;
    }

    /**
     * 获取骨量标准范围值
     *
     * @return
     */
    public static float[] getBoneStandardRange(String calSex, int age) {
        float tmpValue = 1;
        if (calSex.equals("男")) {
            if (age <= 54) {
                tmpValue = 2.4f;
            } else if (age < 75) {
                tmpValue = 2.8f;
            } else if (age >= 75) {
                tmpValue = 3.1f;
            }
        } else if (calSex.equals("女")) {
            if (age <= 39) {
                tmpValue = 1.7f;
            } else if (age <= 60) {
                tmpValue = 2.1f;
            } else if (age > 60) {
                tmpValue = 2.4f;
            }
        }
        return new float[]{tmpValue * 0.7f, tmpValue, tmpValue * 1.3f, 5f};
    }

    /**
     * 获取BMI标准范围值
     *
     * @return
     */
    public static float[] getBMIStandardRange() {
        return new float[]{14f, 18.5f, 23.9f, 28f, 40f};
    }

    /**
     * 获取脂肪标准范围值
     *
     * @param calSex 当时性别
     * @param age    当时的年龄
     * @return
     */
    public static float[] getAxungeStandardRange(String calSex, int age) {
        float[] standardRange = null;
        if (calSex.equals("男")) {
            if (age <= 39) {
                standardRange = new float[]{5f, 11f, 22f, 27f, 45f};
            } else if (age <= 59) {
                standardRange = new float[]{5f, 12f, 23f, 28f, 45.0f};
            } else {
                standardRange = new float[]{5f, 14f, 25f, 30f, 45.0f};
            }
        } else {
            if (age <= 39) {
                standardRange = new float[]{5.0f, 21f, 35f, 40f, 45.0f};
            } else if (age <= 59) {
                standardRange = new float[]{5.0f, 22f, 36f, 41f, 45.0f};
            } else {
                standardRange = new float[]{5.0f, 23f, 37f, 42f, 45.0f};
            }
        }
        return standardRange;
    }

    /**
     * 获取水分标准范围值
     *
     * @return
     */
    public static float[] getWaterContainStandardRange(WeightEntity info, String calSex, int age) {
        float[] range = null;
        if (calSex.equals("男")) {
            if (age <= 30) {
                float value0 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 37.8f / 100.0));
                float value1 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 53.6 / 100.0));
                float value2 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 57.0f / 100.0));
                float value3 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 66.0f / 100.0));
                range = new float[]{value0, value1, value2, value3};
            } else {
                float value0 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 37.8f / 100.0));
                float value1 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 52.3f / 100.0));
                float value2 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 55.6f / 100.0));
                float value3 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 66.0f / 100.0));
                range = new float[]{value0, value1, value2, value3};
            }
        } else {
            if (age <= 30) {
                float value0 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 37.8f / 100.0));
                float value1 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 49.5f / 100.0));
                float value2 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 52.9f / 100.0));
                float value3 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 66.0f / 100.0));
                range = new float[]{value0, value1, value2, value3};
            } else {
                float value0 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 37.8f / 100.0));
                float value1 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 48.1f / 100.0));
                float value2 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 51.5f / 100.0));
                float value3 = Float.parseFloat(DecimalFormatUtils.getOne(info.getWeight() * 66.0f / 100.0));
                range = new float[]{value0, value1, value2, value3};
            }
        }
        return range;
    }

    /**
     * 获取瘦体重标准范围值
     *
     * @return
     */
    public static float[] getThinStandardRange(int calHeight) {
        float height = calHeight / 100.0f;
        float[] range = null;
        range = new float[]{16 * height * height, 18 * height * height, 20 * height * height, 23 * height * height, 26 * height * height};
        return range;
    }

    /**
     * 获取蛋白质标准范围值
     *
     * @return
     */
    public static float[] getProteinStandardRange() {
        float[] range = null;
        range = new float[]{10, 16, 20, 30};
        return range;
    }

    /**
     * 获取蛋白质标准范围值
     *
     * @return
     */
    public static float[] getCorpulentStandardRange() {
        float[] range = null;
        range = new float[]{-10, 10, 20, 30, 50, 70};
        return range;
    }

    public static int getCalAge(RoleInfo roleInfo, WeightEntity weightEntity) {
        return weightEntity.getAge() == 0 ? TimeUtil.getAgeThroughBirthday(roleInfo.getBirthday()) : weightEntity.getAge();
    }

    public static String getCalSex(RoleInfo roleInfo, WeightEntity weightEntity) {
        if (weightEntity.getHeight() > 0) {
            return weightEntity.getSex() == 1 ? "男" : "女";
        } else {
            return roleInfo.getSex();
        }
    }

    public static int getCalHeight(RoleInfo roleInfo, WeightEntity weightEntity) {
        return weightEntity.getHeight() == 0 ? roleInfo.getHeight() : weightEntity.getHeight();
    }


    public ReportDetalis getReportDetalis(RoleInfo currRoleInfo, WeightEntity weightEntity) {
        int age = getCalAge(currRoleInfo, weightEntity);
        ReportDetalis reportDetalis = new ReportDetalis();
        int height = getCalHeight(currRoleInfo, weightEntity);
        String sexStr = getCalSex(currRoleInfo, weightEntity);
        byte sex = (byte) (sexStr.equals(context.getString(R.string.women)) ? 0 : 1);
        if (weightEntity.getR1() > 0 && age > 5) {
            CsAlgoBuilder csAlgoBuilder = new CsAlgoBuilder(height, weightEntity.getWeight(), sex, age, weightEntity.getR1(), weightEntity.getRn8());
            setRn8ReportDetalis(reportDetalis, csAlgoBuilder, weightEntity);
        }
        return reportDetalis;
    }


    public void setRn8ReportDetalis(ReportDetalis reportDetalis, CsAlgoBuilder csAlgoBuilder, WeightEntity weightEntity) {
        if (csAlgoBuilder.is8FatScale(weightEntity.getRn8())) {
            reportDetalis.setHaveRn8(csAlgoBuilder.is8FatScale(weightEntity.getRn8()));
            List<Rn8Item> rn8Items = new ArrayList<>();
            rn8Items.add(Rn8StandartUtils.getRn8Item(true, csAlgoBuilder.getLABFR(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(true, csAlgoBuilder.getRABFR(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(true, csAlgoBuilder.getLLBFR(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(true, csAlgoBuilder.getRLBFR(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(true, csAlgoBuilder.getTRBFR(), true, weightEntity.getSex(), weightEntity.getAge()));

            rn8Items.add(Rn8StandartUtils.getRn8Item(false, csAlgoBuilder.getLASLM(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(false, csAlgoBuilder.getRASLM(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(false, csAlgoBuilder.getLLSLM(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(false, csAlgoBuilder.getRLSLM(), false, weightEntity.getSex(), weightEntity.getAge()));
            rn8Items.add(Rn8StandartUtils.getRn8Item(false, csAlgoBuilder.getTRSLM(), true, weightEntity.getSex(), weightEntity.getAge()));
            reportDetalis.setRn8Items(rn8Items);
        }
    }


    public enum StandardSet {
        BODILY(R.string.detailBodily,
                R.mipmap.report_bodily_icon,
                0,
                0,
                new int[]{R.color.corState1, R.color.corState3, R.color.corState4, R.color.corState5, R.color.corState6},
                new int[]{R.string.reportBodilyState1, R.string.reportBodilyState2, R.string.reportBodilyState3, R.string.reportBodilyState4, R.string.reportBodilyState5},
                new int[]{R.string.reportBodilyState1Text, R.string.reportBodilyState2Text, R.string.reportBodilyState3Text,
                        R.string.reportBodilyState4Text, R.string.reportBodilyState5Text}),
        CORPULENT(R.string.detailCorpulent,
                R.mipmap.report_corpulent_icon,
                R.mipmap.detail_corpulent,
                R.mipmap.progress5,
                new int[]{R.color.corState1, R.color.corState2, R.color.corState3, R.color.corState4, R.color.corState5, R.color.corState6, R.color.corState7},
                new int[]{R.string.corState1, R.string.corState2, R.string.corState3, R.string.corState4, R.string.corState5, R.string.corState6, R.string.corState7},
                new int[]{R.string.reportCorStatTip1, R.string.reportCorStatTip2, R.string.reportCorStatTip3, R.string.reportCorStatTip4, R.string.reportCorStatTip5, R.string.reportCorStatTip6, R.string.reportCorStatTip7},
                new float[]{-20, -10, 10, 20, 30, 50}),
        WEIGHT(R.string.detailWeight,
                R.mipmap.report_weight_icon,
                R.mipmap.detail_weight,
                R.mipmap.progress4,
                new int[]{R.color.corState2, R.color.corState3, R.color.corState4, R.color.corState6},
                new int[]{R.string.reportSlim, R.string.reportStandard, R.string.reportExtraBaggage, R.string.reportObesity},
                new int[]{R.string.reportBMISlimTip, R.string.reportBMIStandardTip, R.string.reportBMIExtraTip, R.string.reportBMIObesityTip}),
        BMI(R.string.detailBmi,
                R.mipmap.report_bmi_icon,
                R.mipmap.detail_bmi,
                R.mipmap.progress4,
                new int[]{R.color.corState2, R.color.corState3, R.color.corState4, R.color.corState6},
                new int[]{R.string.reportSlim, R.string.reportStandard, R.string.reportExtraBaggage, R.string.reportObesity},
                new int[]{R.string.reportBMISlimTip, R.string.reportBMIStandardTip, R.string.reportBMIExtraTip, R.string.reportBMIObesityTip},
                new float[]{18.5f, 24, 28}),
        AXUNGE(R.string.detailAxunge,
                R.mipmap.report_axunge_icon,
                R.mipmap.detail_axunge,
                R.mipmap.progress4,
                new int[]{R.color.corState2, R.color.corState3, R.color.corState4, R.color.corState6},
                new int[]{R.string.reportSlim, R.string.reportStandard, R.string.reportExtraBaggage, R.string.reportObesity},
                new int[]{R.string.reportAxungeSlimTip, R.string.reportAxungeStandardTip, R.string.reportAxungeExtraTip, R.string.reportAxungeObesityTip}),
        /**
         * Excel文档没有这项的说明
         */
        AXUNGEWEIGHT(R.string.detailAxungeWeight,
                R.mipmap.report_axunge_weight_icon,
                R.mipmap.detail_axunge,
                R.mipmap.progress4,
                new int[]{R.color.corState2, R.color.corState3, R.color.corState4, R.color.corState6},
                new int[]{R.string.reportSlim, R.string.reportStandard, R.string.reportExtraBaggage, R.string.reportObesity},
                new int[]{R.string.reportAxungeSlimTip, R.string.reportAxungeStandardTip, R.string.reportAxungeExtraTip, R.string.reportAxungeObesityTip}),
        /**
         * Excel文档没有这项的说明
         */
        MUSCLE(R.string.detailMuscle,
                R.mipmap.report_muscle_icon,
                R.mipmap.detail_muscle,
                R.mipmap.progress3,
                new int[]{R.color.corState6, R.color.corState3, R.color.corState8},
                new int[]{R.string.reportInsufficient, R.string.reportStandard, R.string.reportExcellent},
                new int[]{R.string.reportMuscleLowTip, R.string.reportMuscleStandardTip, R.string.reportMuscleHightTip}),
        MUSCLEWEIGHT(R.string.detailMuscleWeight,
                R.mipmap.report_muscle_weight_icon,
                R.mipmap.detail_muscle,
                R.mipmap.progress3,
                new int[]{R.color.corState6, R.color.corState3, R.color.corState8},
                new int[]{R.string.reportInsufficient, R.string.reportStandard, R.string.reportExcellent},
                new int[]{R.string.reportMuscleLowTip, R.string.reportMuscleStandardTip, R.string.reportMuscleHightTip}),
        VISCERA(R.string.detailViscera,
                R.mipmap.report_viscera_icon,
                R.mipmap.detail_viscera,
                R.mipmap.progress3_1,
                new int[]{R.color.corState3, R.color.corState4, R.color.corState6},
                new int[]{R.string.reportStandard, R.string.reportBevigilant, R.string.reportDanger},
                new int[]{R.string.reportVisceraStandardTip, R.string.reportVisceraExtraTip, R.string.reportVisceraObesityTip},
                new float[]{9, 14}),
        WATER(R.string.detailWater,
                R.mipmap.report_water_icon,
                R.mipmap.detail_water,
                R.mipmap.progress3,
                new int[]{R.color.corState6, R.color.corState3, R.color.corState8},
                new int[]{R.string.reportInsufficient, R.string.reportStandard, R.string.reportExcellent},
                new int[]{R.string.reportWaterLowTip, R.string.reportWaterStandardTip, R.string.reportWaterHightTip}),
        METABOLISM(R.string.detailMetabolism,
                R.mipmap.report_metabolism_icon,
                R.mipmap.detail_metabolism,
                R.mipmap.progress2,
                new int[]{R.color.corState6, R.color.corState3},
                new int[]{R.string.reportMetabolismLow, R.string.reportMetabolismHigh},
                new int[]{R.string.reportMetabolismLowTip, R.string.reportMetabolismHightTip}),
        PROTEIN(R.string.detailProtein,
                R.mipmap.report_protein_icon,
                R.mipmap.detail_protein,
                R.mipmap.progress3_2,
                new int[]{R.color.corState2, R.color.corState3, R.color.corState4},
                new int[]{R.string.reportLow, R.string.reportStandard, R.string.reportHigh1},
                new int[]{R.string.reportProteinStatTip1, R.string.reportProteinStatTip2, R.string.reportProteinStatTip3},
                new float[]{16, 20}),
        BONE(R.string.detailBone,
                R.mipmap.report_bone_icon,
                R.mipmap.detail_bone,
                R.mipmap.progress3,
                new int[]{R.color.corState6, R.color.corState3, R.color.corState8},
                new int[]{R.string.reportLow, R.string.reportStandard, R.string.reportExcellent},
                new int[]{R.string.reportBoneLowTip, R.string.reportBoneStandardTip, R.string.reportBoneHightTip}),
        THIN(R.string.detailThinWeight,
                R.mipmap.report_thin_weight_icon,
                R.mipmap.detail_thin,
                R.mipmap.progress4_1,
                new int[]{R.color.standard6, R.color.standard3, R.color.standard8, R.color.standard8},
                new int[]{R.string.thinState1, R.string.thinState2, R.string.thinState3, R.string.thinState4},
                new int[]{R.string.reportThinStatTip1, R.string.reportThinStatTip2, R.string.reportThinStatTip3, R.string.reportThinStatTip4}),
        CONTAINWATER(R.string.detailWaterContain,
                R.mipmap.report_water_icon,
                R.mipmap.detail_watercontain,
                R.mipmap.progress3,
                new int[]{R.color.standard6, R.color.standard3, R.color.standard8},
                new int[]{R.string.reportInsufficient, R.string.reportStandard, R.string.reportExcellent},
                new int[]{R.string.reportWaterContainStatTip1, R.string.reportWaterContainStatTip2, R.string.reportWaterContainStatTip3});


        int name;
        int icon;
        int[] colors;
        int[] standards;
        int[] tips;
        int detalisIcon;
        int progressBgRes;
        float[] levelNums;

        StandardSet(int name, int icon, int detalisIcon, int progressBgRes, int[] colors, int[] standards, int[] tips) {
            this(name, icon, detalisIcon, progressBgRes, colors, standards, tips, null);
        }

        StandardSet(int name, int icon, int detalisIcon, int progressBgRes, int[] colors, int[] standards, int[] tips, float[] levelNums) {
            this.name = name;
            this.icon = icon;
            this.detalisIcon = detalisIcon;
            this.progressBgRes = progressBgRes;
            this.colors = colors;
            this.standards = standards;
            this.tips = tips;
            this.levelNums = levelNums;
        }

        public int getName() {
            return name;
        }

        public int getIcon() {
            return icon;
        }

        public int[] getColor() {
            return colors;
        }

        public int[] getStandards() {
            return standards;
        }

        public int[] getTips() {
            return tips;
        }

        public int getDetalisIcon() {
            return detalisIcon;
        }

        public int getProgressBgRes() {
            return progressBgRes;
        }

        public float[] getLevelNums() {
            return levelNums;
        }
    }
}
