package com.jq.code.code.algorithm;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/23.
 * 2017.4.14   公式版本V2.3.2  华为数据1154+10例
 */

public class CsAlgoBuilder {
    //身高
    private float H;
    //体重
    private float Wt;
    //性别 1-男 0--女
    private byte Sex;
    //年龄
    private int Age;
    //全身电阻 -Z34
    private float Z;

    private float Z12;
    private float Z13;
    private float Z14;
    private float Z23;
    private float Z24;
    private float Z34;

    //是否使用8电极算法
    private boolean isEightR=false;



    public float getH() {
        return H;
    }

    public byte getSex() {
        return Sex;
    }

    public int getAge() {
        return Age;
    }

    /**
     * 构造函数（用于测量报告后续调用）
     *
     */
    public CsAlgoBuilder(float height,float weight,byte sex,int age,float resistance){
        H=height;
        Wt=weight;
        Sex=sex;
        Age=age;
        Z=resistance;
        isEightR=false;
    }

    /**
     * 构造函数（适用于8电级）
     *
     */
    public CsAlgoBuilder(float height,float weight,byte sex,int age,float resistance,String remark){
        H=height;
        Wt=weight;
        Sex=sex;
        Age=age;

        CsResistanceArray rArray=parserZ(remark);
        if(rArray!=null){
            Z12=rArray.Z12;
            Z13=rArray.Z13;
            Z14=rArray.Z14;
            Z23=rArray.Z23;
            Z24=rArray.Z24;
            Z34=resistance;
            Z=(Z14+Z23)/2.0f;
            isEightR=true;
        }else{
            Z=resistance;
            isEightR=false;
        }

    }

    public static boolean is8FatScale(String rn8string){
        if(rn8string==null) return false;
        if(rn8string.trim().length()==0) return false;

        boolean bRet=false;
        String[] sClass= rn8string.trim().split("\\|");
        if(sClass.length>0) {
            for (int i = 0; i < sClass.length; i++) {
                String[] sType = sClass[i].trim().split("\\:");
                if (sType.length > 1) {
                    if (sType[0].equals("1")) {
                        String[] sValues = sType[1].trim().split("\\,");
                        if (sValues.length == 5) {
                            bRet = true;
                        }
                    }
                }
            }
        }
        return bRet;
    }

    private CsResistanceArray parserZ(String rn8string){
        CsResistanceArray rArray=null;
        if(rn8string==null) return null;
        if(rn8string.trim().length()==0) return null;
        String[] sClass= rn8string.trim().split("\\|");
        if(sClass.length>0){
            for(int i=0;i<sClass.length;i++){
                String[] sType=sClass[i].trim().split("\\:");
                if(sType.length>1){
                    if(sType[0].equals("1")){
                        String[] sValues=sType[1].trim().split("\\,");
                        if(sValues.length==5) {
                            rArray=new CsResistanceArray();
                            rArray.Z12 = Float.parseFloat(sValues[0]);
                            rArray.Z13 = Float.parseFloat(sValues[1]);
                            rArray.Z14 = Float.parseFloat(sValues[2]);
                            rArray.Z23 = Float.parseFloat(sValues[3]);
                            rArray.Z24 = Float.parseFloat(sValues[4]);
                        }
                    }
                }
            }
        }
        return rArray;
    }

    private String resetRemark(String curRemark,String newR8String){
        if(curRemark==null) return "";
        if(curRemark.trim().length()==0) return "";

        StringBuilder sb=new StringBuilder();
        String[] sClass= curRemark.trim().split("\\|");
        if(sClass.length>0){
            for(int i=0;i<sClass.length;i++){
                String[] sType=sClass[i].trim().split("\\:");
                if(sType.length>1){
                    if(sType[0].equals("1")){
                        if(sb.length()>0){
                            sb.append("|");
                            sb.append(newR8String);
                        }else{
                            sb.append(newR8String);
                        }

                    }else{
                        if(sb.length()>0){
                            sb.append("|");
                            sb.append(sClass[i]);
                        }else{
                            sb.append(sClass[i]);
                        }
                    }
                }
            }
        }
        return sb.toString();
    }



    /**
     * 加入电阻滤波后的构造函数（用于在根据蓝牙秤上传的数据后调用并计算结果）
     * 注意：使用此构造函数后，需要调用getZ获取滤波后的电阻值并存入数据库
     *
     */
    public CsAlgoBuilder(float height, byte sex, int age, float curWeight, float curR, Date curTime, float lastR, Date lastTime){
        H=height;
        Wt=curWeight;
        Sex=sex;
        Age=age;

        long betweenSecond=(curTime.getTime()-lastTime.getTime())/1000;
        long betweenMin=betweenSecond / 60;
        Z=calResistance(betweenMin,curR,lastR);
        isEightR=false;
    }

    /**
     * 构造函数（适用于8电级）
     *
     */
    public CsAlgoBuilder(float height, byte sex, int age, float curWeight, float curR, String curRemark, Date curTime, float lastR,String lastRemark, Date lastTime){
        H=height;
        Wt=curWeight;
        Sex=sex;
        Age=age;

        long betweenSecond=(curTime.getTime()-lastTime.getTime())/1000;
        long betweenMin=betweenSecond / 60;
        CsResistanceArray curRArray=parserZ(curRemark);
        CsResistanceArray lastRArray=parserZ(lastRemark);

        if(curRArray!=null) {
            if(lastRArray==null){
                lastRArray=new CsResistanceArray();
            }
            Z34 = calResistance(betweenMin, curR, lastR);
            Z12 = calResistance(betweenMin, curRArray.Z12, lastRArray.Z12);
            Z13 = calResistance(betweenMin, curRArray.Z13, lastRArray.Z13);
            Z14 = calResistance(betweenMin, curRArray.Z14, lastRArray.Z14);
            Z23 = calResistance(betweenMin, curRArray.Z23, lastRArray.Z23);
            Z24 = calResistance(betweenMin, curRArray.Z24, lastRArray.Z24);
            Z = (Z14 + Z23) / 2.0f;
            remark = "";
            if (Z12 > 0 && Z13 > 0 && Z14 > 0 && Z23 > 0 && Z24 > 0) {
                //String newR8String="1:" + Z12 + "," + Z13 + "," + Z14 + "," + Z13 + "," + Z23 + "," + Z24;
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.');
                DecimalFormat df1 = new DecimalFormat("#0.0", symbols);
                String newR8String = "1:" + df1.format(Z12) + "," + df1.format(Z13) + "," + df1.format(Z14) + "," + df1.format(Z23) + "," + df1.format(Z24);
                remark = resetRemark(curRemark, newR8String);
            }
            isEightR = true;
        }else{
            Z=curR;
            isEightR = false;
        }
    }

    private String remark;

    public String getRemark(){
        return remark;
    }

    public float getZ(){
        return Z;
    }


    private float calResistance(long between,float curR,float lastR){
        float fRet=curR;
        if(curR==0.0f || lastR==0.0f){
            return fRet;
        }

        if(curR==lastR){
            return fRet;
        }

        if(between>30){
            return fRet;
        }
        float fDifference=curR-lastR;
        if(fDifference<=24.0f && fDifference>0.0f){
            fRet=lastR + (fDifference/16.0f);
        }else if(fDifference<0.0f && fDifference>=-24.0f){
            fRet=lastR + (fDifference/16.0f);
        }else if(fDifference>24.0f && fDifference<=32.0f){
            fRet=lastR + (fDifference/4.0f);
        }else if(fDifference<-24.0f && fDifference>=-32.0f){
            fRet=lastR + (fDifference/4.0f);
        }else if(fDifference>32.0f && fDifference<=64.0f){
            fRet=lastR + (fDifference/2.0f);
        }else if(fDifference<-32.0f && fDifference>=-64.0f){
            fRet=lastR + (fDifference/2.0f);
        }else if(fDifference>64.0f && fDifference<=89.0f){
            fRet=(lastR + curR * 3) / 4.0f;
        }else if(fDifference<-64.0f && fDifference>=-89.0f){
            fRet=(lastR + curR * 3) / 4.0f;
        }else{
            //大于89欧姆
            fRet=curR;
        }
        return fRet;
    }

    /**
     * @return 获取BMI
     */
    public float getBMI(){
        return (Wt/(H*H)) * 100.0f * 100.0f;
    }

    /**
     * @return 获取脂肪重
     */
    public float getFM(){
        return getBFR() * Wt /100.0f;
    }


    private float getBFR_Raw(){
        float BFR=0;

        if(isEightR){
            if(Sex==1) {
                BFR= (-0.2893f*H + 0.6143f * Wt + 0.0355f * Age + 0.0207f * Z + 7.9298f)/Wt * 100.0f;
            } else {
                BFR= (-0.2923f*H + 0.7870f * Wt + 0.0071f * Age + 0.0139f * Z + 9.6791f)/Wt * 100.0f;
            }
        }else {
            if (Sex == 1) {
                BFR = (float) ((-0.3315f * H + 0.6216f * Wt + 0.0183f * (Age * 1.0f) + 0.0085f * Z + 22.554f) / Wt * 100.0f);
            } else {
                BFR = (float) ((-0.3332f * H + 0.7509f * Wt + 0.0196f * (Age * 1.0f) + 0.0072f * Z + 22.7193f) / Wt * 100.0f);
            }
        }

        return BFR;
    }


    /**
     * @return 获取脂肪百份比
     */
    public float getBFR(){
        float BFR=0;

        BFR=getBFR_Raw();
        if(BFR<5.0f){BFR=5.0f;}
        if(BFR>45.0f){BFR=45.0f;}
        BigDecimal bigConvert=new BigDecimal(BFR);
        return bigConvert.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * @return 获取内脏脂肪等级 17及以下不做计算
     */
    public float getVFR(){
        float VFR=0;
        if(Age>17){

            if(isEightR) {
                if (Sex == 1) {
                    VFR = (-0.2488f * H + 0.3715f * Wt + 0.1346f * Age + 0.0082f * Z + 15.9014f);
                } else {
                    VFR = (-0.0983f * H + 0.2885f * Wt + 0.0729f * Age + 0.0086f * Z -4.7060f);
                }
            }else {
                if (Sex == 1) {
                    VFR = (float) (-0.2675f * H + 0.4200f * Wt + 0.1462f * (Age * 1.0f) + 0.0123f * Z + 13.9871f);
                } else {
                    VFR = (float) (-0.1651f * H + 0.2628f * Wt + 0.0649f * (Age * 1.0f) + 0.0024f * Z + 12.3445f);
                }
            }

            int iVFR=(int)VFR;
            if((VFR-iVFR)<0.5f){
                VFR=iVFR;
            }else{
                VFR=iVFR + 0.5f;
            }
            if(VFR<1.0f){
                VFR=1.0f;
            }else if(VFR>59.0f){
                VFR=59.0f;
            }

        }
        return VFR;
    }

    /**
     * @return 获取总水重
     */
    public float getTF(){
        if(Age>17) {
            return getTFR() * Wt / 100.0f;
        }else{
            return 0;
        }
    }

    /**
     * @return 获取水分百分比
     */
    public float getTFR(){
        if(Age>17) {
            float TFR = 0;

            if(isEightR) {
                if (Sex == 1) {
                    TFR = (0.2224f * H + 0.1670f * Wt -0.0411f * Age -0.0311f * Z + 6.9026f) / Wt * 100.0f;
                } else {
                    TFR = (0.1058f * H + 0.2168f * Wt -0.0016f * Age -0.0160f * Z + 9.8471f) / Wt * 100.0f;
                }
            }else {
                if (Sex == 1) {
                    TFR = (0.0939f * H + 0.3758f * Wt - 0.0032f * (Age * 1.0f) - 0.006925f * Z + 0.097f) / Wt * 100.0f;
                } else {
                    TFR = (0.0877f * H + 0.2973f * Wt + 0.0128f * (Age * 1.0f) - 0.00603f * Z + 0.5175f) / Wt * 100.0f;
                }
            }

            if (TFR < 20.0f) {
                TFR = 20.0f;
            }
            if (TFR > 85.0f) {
                TFR = 85.0f;
            }

            BigDecimal bigConvert = new BigDecimal(TFR);
            return bigConvert.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }else{
            return 0;
        }

    }

    private float getSLM_Raw(){
        float SLM=0;

        float BFR=getBFR_Raw();
        if(BFR>45.0f){
            SLM=Wt-0.45f*Wt-4;
        }else if(BFR<5.0f){
            SLM=Wt-0.05f*Wt-1;
        }else{
            if(isEightR) {
                if (Sex == 1) {
                    SLM = (0.2764f * H + 0.3662f * Wt -0.0337f * Age -0.0199f * Z -7.7390f);
                } else {
                    SLM = (0.2676f * H + 0.1948f * Wt -0.0063f * Age -0.0127f * Z -7.8411f);
                }
            }else {
                if (Sex == 1) {
                    SLM = (0.2867f * H + 0.3894f * Wt - 0.0408f * (Age * 1.0f) - 0.01235f * Z - 15.7665f);
                } else {
                    SLM = (0.3186f * H + 0.1934f * Wt - 0.0206f * (Age * 1.0f) - 0.0132f * Z - 16.4556f);
                }
            }

            if(SLM<20.0f){ SLM= 20.0f;}
            if(SLM>70.0f){ SLM= 70.0f;}

        }



        return SLM;
    }

    /**
     * @return 获取骨骼肌
     */
    public float getSMM(){
        return 0.2573f*H+0.1745f*Wt-0.0161f*Age+2.4269f*Sex-0.0170f*Z-20.2165f;
    }

    /**
     * @return 获取肌肉重
     */
    public float getSLM(){
        float SLM=getSLM_Raw();

        BigDecimal bigConvert=new BigDecimal(SLM);
        return bigConvert.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

    }

    /**
     * @param  slm 肌肉重，可传0，由系统计算
     * @return 获取肌肉百份比
     */
    public float getSLMPercent(float slm){
        float SLMPercent=0;
        //将肌肉重转换成百份比
        if(slm==0.0f){
            SLMPercent=(getSLM_Raw()/Wt) * 100.0f;
        }else{
            SLMPercent=(slm/Wt) * 100.0f;
        }



        BigDecimal bigConvert=new BigDecimal(SLMPercent);
        return bigConvert.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
    }



    /**
     * @return 获取骨量
     */
    public float getMSW(){
        float MSW= Wt - getFM()- getSLM_Raw();

        if(MSW<1.0f){
            MSW=1.0f;
        }else if(MSW>4.0f){
            MSW=4.0f;
        }
        return MSW;
    }



    /**
     * @return 获取基础代谢率
     */
    public float getBMR(){
        float BMR=0;

        if(isEightR) {
            if (Sex == 1) {
                BMR = (7.2819f * H + 12.3775f * Wt -4.4731f * Age -0.5458f * Z -84.7412f);
            } else {
                BMR = (6.3442f * H + 9.8875f * Wt -3.4612f * Age -0.3056f * Z -59.9762f);
            }
        }else {
            if (Sex == 1) {
                BMR = (7.5037f * H + 13.1523f * Wt - 4.3376f * (Age * 1.0f) - 0.3486f * Z - 311.7751f);
            } else {
                BMR = (7.5432f * H + 9.9474f * Wt - 3.4382f * (Age * 1.0f) - 0.3090f * Z - 288.2821f);
            }
        }

        BigDecimal bigConvert=new BigDecimal(BMR);
        return bigConvert.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * @return 获取身体年龄
     */
    public int getBodyAge(){

        int bodyAge=0;
        if(Age>17) {
            float fBodyAge=0;
            if(isEightR) {
                if (Sex == 1) {
                    fBodyAge = (-0.5378f * H + 0.7642f * Wt + 0.3667f * Age + 0.0390f * Z + 33.8131f);
                } else {
                    fBodyAge = (-0.9531f * H + 1.5246f * Wt + 0.4584f * Age + 0.0374f * Z + 58.5035f);
                }
            }else {
                if (Sex == 1) {
                    fBodyAge = (-0.7471f * H + 0.9161f * Wt + 0.4184f * (Age * 1.0f) + 0.0517f * Z + 54.2267f);
                } else {
                    fBodyAge = (-1.1165f * H + 1.5784f * Wt + 0.4615f * (Age * 1.0f) + 0.0415f * Z + 83.2548f);
                }
            }
            bodyAge = (int) fBodyAge;
            if((bodyAge-Age)>10){
                bodyAge=Age+10;
            }else if((Age-bodyAge)>10){
                bodyAge=Age-10;
            }
            if (bodyAge < 18) {
                bodyAge = 18;
            }
            if (bodyAge > 80) {
                bodyAge = 80;
            }
        }

        return bodyAge;
    }

    /**
     * @return 获取标准体重
     */
    public float getBW(){
        return getBW(Sex,H);
    }

    private static float getBW(byte Sex,float H){
        float bw=0.0f;
        if(Sex==1){
            bw=(H-80.0f) * 0.7f;
        }else{
            bw=(H-70.0f) * 0.6f;
        }
        return bw;
    }
    /**
     * @return 获取体重控制
     */
    public float getWC(){
        float bw=getBW();
        return bw-Wt;
    }

    /**
     * @return 获取标准肌肉
     */
    public float getBM(){
        if(Age>17) {
            return getBM(Wt,Sex);
        }else{
            return 0.0f;
        }
    }

    public static float getBM(float weight,byte sex){
        float bm=0.0f;
        if(sex==1){
            bm=weight * 0.77f;
        }else{
            bm=weight * 0.735f;
        }
        return bm;
    }

    /**
     * @return 获取肌肉控制
     */
    public float getMC(){
        if(Age>17) {
            float bm = getBM();
            float slm = getSLM_Raw();
            return bm - slm;
        }else{
            return 0.0f;
        }
    }

    /**
     * @return 获取脂肪控制
     */
    public float getFC(){
        if(Age>17) {
            float bw = getBW();
            float mc = getMC();
            float wc = getWC();
            float fc = 0.0f;
            if (Wt < bw) {
                fc = wc - mc;
            } else {
                if (Sex == 1) {
                    fc = (0.15f * (Wt + mc) - getFM()) / 0.85f;
                } else {
                    fc = (0.2f * (Wt + mc) - getFM()) / 0.8f;
                }
            }
            return fc;
        }else{
            return 0.0f;
        }

    }


    /**
     * @return 获取肥胖度
     */
    public float getOD(){
        float bw=getBW();
        return ((Wt-bw)/bw)*100.0f;
    }

    /**
     * @return 获取蛋白质
     */
    public float getPM(){
        if(Age>17) {
            float slm = getSLM_Raw();
            float tf = getTF();
            return slm-tf;
        }else{
            return 0;
        }
    }

    /**
     * @return 获取瘦体重
     */
    public float getLBM(){
        //if(Sex == 1){
        float fm=getFM();
        return Wt-fm;
        //} else{
        //   return 0 ;
        //}
    }


    private static float getShape(float bfr,float bmi,byte Sex,int Age){
        int BFRStandard=0;
        if(Sex==1){
            if(Age<=39){
                if(bfr<11.0f){
                    BFRStandard=0;
                }else if(bfr>=11.0f && bfr<17.0f){
                    BFRStandard=1;
                }else if(bfr>=17.0f && bfr<27.0f){
                    BFRStandard=2;
                }else{
                    BFRStandard=3;
                }
            }else if(Age<=59){
                if(bfr<12.0f){
                    BFRStandard=0;
                }else if(bfr>=12.0f && bfr<18.0f){
                    BFRStandard=1;
                }else if(bfr>=18.0f && bfr<28.0f){
                    BFRStandard=2;
                }else{
                    BFRStandard=3;
                }
            }else{
                if(bfr<14.0f){
                    BFRStandard=0;
                }else if(bfr>=14.0f && bfr<20.0f){
                    BFRStandard=1;
                }else if(bfr>=20.0f && bfr<30.0f){
                    BFRStandard=2;
                }else{
                    BFRStandard=3;
                }
            }

        }else{
            if(Age<=39){
                if(bfr<21.0f){
                    BFRStandard=0;
                }else if(bfr>=21.0f && bfr<28.0f){
                    BFRStandard=1;
                }else if(bfr>=28.0f && bfr<40.0f){
                    BFRStandard=2;
                }else{
                    BFRStandard=3;
                }
            }else if(Age<=59){
                if(bfr<22.0f){
                    BFRStandard=0;
                }else if(bfr>=22.0f && bfr<29.0f){
                    BFRStandard=1;
                }else if(bfr>=29.0f && bfr<41.0f){
                    BFRStandard=2;
                }else{
                    BFRStandard=3;
                }
            }else{
                if(bfr<23.0f){
                    BFRStandard=0;
                }else if(bfr>=23.0f && bfr<30.0f){
                    BFRStandard=1;
                }else if(bfr>=30.0f && bfr<42.0f){
                    BFRStandard=2;
                }else{
                    BFRStandard=3;
                }
            }
        }

        int BMIStandard=0;
        if(bmi<18.5f){
            BMIStandard=0;
        }else if(bmi>=18.5f && bmi<24.0f){
            BMIStandard=1;
        }else if(bmi>=24.0f && bmi<28.0f){
            BMIStandard=2;
        }else{
            BMIStandard=3;
        }

        int shape=0;
        switch (BFRStandard){
            case 0:
                if(BMIStandard==0){
                    shape=-1;
                }else if(BMIStandard==1){
                    shape=0;
                }else{
                    shape=2;
                }
                break;
            case 1:
                if(BMIStandard==0){
                    shape=-1;
                }else if(BMIStandard==1){
                    shape=0;
                }else{
                    shape=2;
                }
                break;
            case 2:
                if(BMIStandard==0){
                    shape=1;
                }else if(BMIStandard==1){
                    shape=1;
                }else{
                    shape=3;
                }
                break;
            case 3:
                if(BMIStandard==0){
                    shape=1;
                }else if(BMIStandard==1){
                    shape=1;
                }else{
                    shape=3;
                }
                break;
        }
        return shape;
    }


    /**
     * @return 获取体型 -1:消瘦 0:普通 1:隐形肥胖 2:肌肉型肥胖 3:肥胖
     */
    public float getShape(){
        float bfr=getBFR();
        float bmi=getBMI();

        return getShape(bfr,bmi,Sex,Age);
    }

    /**
     * @return 获取体成分评分
     */
    public float getScore(){
        return getScoreWithAge(H,Wt,Sex,Age,getBFR(),getSLM_Raw(),getVFR());

    }

    private float getScoreWithAge(float height,float weight,byte sex,int age,float bfr,float slm,float vfr){
        if(age>17) {
            return calScore(height,weight,sex,age,bfr,slm,vfr);
        }else{
            return 0;
        }
    }

    //*****************************************************以下方法为8电极相关计算方法******************************************

    /*
  * 获取右手脂肪率
  * */
    public float getRABFR(){
        float B_Z_RA= (Z12-Z14+Z24)/2.0f;
        float B_RA_BFR;

        if(Sex==1)
            B_RA_BFR =-0.3409f*H+0.4866f*Wt+0.0213f*Age+0.0264f*B_Z_RA+31.4827f;
        else
            B_RA_BFR =-0.5619f*H+0.8352f*Wt+0.0054f*Age+0.0110f*B_Z_RA+65.2287f;
//        if(Sex==1)
//            B_RA_BFR =-0.4153f*H+0.5072f*Wt-0.0076f*Age+0.0464f*B_Z_RA+35.5400f;
//        else
//            B_RA_BFR =-0.6787f*H+0.9261f*Wt+0.0255f*Age+0.0325f*B_Z_RA+68.8284f;
        if(B_RA_BFR<4)
            B_RA_BFR=4;
        if(B_RA_BFR>45)
            B_RA_BFR=45;
        return  B_RA_BFR;
    }

    /*
  * 左手脂肪率
  * */
    public float getLABFR(){
        float B_LA_BFR;
        float B_Z_LA= (Z14+Z12-Z24)/2.0f;
        if(Sex==1)
            B_LA_BFR = -0.3225f*H+0.4518f*Wt+0.0260f*Age+0.0166f*B_Z_LA+34.0857f;
        else
            B_LA_BFR = -0.5501f*H+0.8187f*Wt-0.0013f*Age+0.0115f*B_Z_LA+65.6346f;
        if(B_LA_BFR<4)
            B_LA_BFR=4;
        if(B_LA_BFR>45)
            B_LA_BFR=45;
        return  B_LA_BFR;
    }

    /*
 * 右脚脂肪率
 * */
    public float getRLBFR(){
        float B_RL_BFR;
        float B_Z_RL = (Z34-Z23+Z24)/2.0f;
        if(Sex==1)
            B_RL_BFR = -0.4359f*H+0.5865f*Wt+0.0213f*Age+0.0649f*B_Z_RL+36.8261f;
        else
            B_RL_BFR = -0.4227f*H+0.5993f*Wt-0.0041f*Age+0.0240f*B_Z_RL+59.3459f;
        if(B_RL_BFR<4)
            B_RL_BFR=4;
        if(B_RL_BFR>45)
            B_RL_BFR=45;
        return  B_RL_BFR;
    }

    /*
  * 左脚脂肪率
  * */
    public float getLLBFR(){
        float B_LL_BFR;
        float B_Z_LL = (Z23+Z34-Z24)/2.0f;
        if(Sex==1)
            B_LL_BFR =-0.3799f*H+0.5553f*Wt+0.0298f*Age+0.0588f*B_Z_LL+30.7532f;
        else
            B_LL_BFR =-0.4030f*H+0.5670f*Wt-0.0053f*Age+0.0193f*B_Z_LL+59.4150f;
        if(B_LL_BFR<4)
            B_LL_BFR=4;
        if(B_LL_BFR>45)
            B_LL_BFR=45;
        return  B_LL_BFR;
    }

    /*
  * 躯干脂肪率
  * */
    private float getTRBFR_Raw(){
        float B_TR_BFR;
        float B_Z_TR = (Z14+Z23)/50.0f;
        if(Sex==1)
            B_TR_BFR =-0.4665f*H+0.7362f*Wt+0.1955f*Age+0.5224f*B_Z_TR+31.5550f;
        else
            B_TR_BFR =-0.6681f*H+1.1076f*Wt-0.0182f*Age+1.0112f*B_Z_TR+45.2974f;
        if(B_TR_BFR<5.5)
            B_TR_BFR=5.5f;
        if(B_TR_BFR>55)
            B_TR_BFR=55;
        return  B_TR_BFR;
    }

    /*
  * 躯干脂肪率
  * */
    public float getTRBFR(){
        return getSectionBFR(2);
    }

    /*
 * 右手肌肉量
 * */
    public float getRASLM(){
        float B_RA_SLM_R;
        float B_Z_RA= (Z12-Z14+Z24)/2.0f;
        if(Sex==1)
            B_RA_SLM_R =0.0180f*H+0.0152f*Wt-0.0005f*Age-0.0028f*B_Z_RA-0.5839f;
        else
            B_RA_SLM_R =0.0087f*H+0.0149f*Wt+0.0010f*Age-0.0009f*B_Z_RA-0.2348f;
        if(B_RA_SLM_R<0.5)
            B_RA_SLM_R=0.5f;
        if(B_RA_SLM_R>5)
            B_RA_SLM_R=5;
        return B_RA_SLM_R;
    }

    /*
* 左手肌肉量
* */
    public float getLASLM(){
        float B_LA_SLM_R;
        float B_Z_LA= (Z14+Z12-Z24)/2.0f;
        if(Sex==1)
            B_LA_SLM_R =0.0166f*H+0.0150f*Wt-0.0018f*Age-0.0023f*B_Z_LA-0.6043f;
        else
            B_LA_SLM_R =0.0075f*H+0.0156f*Wt+0.0016f*Age-0.0009f*B_Z_LA-0.2108f;
        if(B_LA_SLM_R<0.5)
            B_LA_SLM_R=0.5f;
        if(B_LA_SLM_R>5)
            B_LA_SLM_R=5;
        return	B_LA_SLM_R;
    }

    /*
* 右脚肌肉量
* */
    public float getRLSLM(){
        float B_RL_SLM_R;
        float B_Z_RL = (Z34-Z23+Z24)/2.0f;
        if(Sex==1)
            B_RL_SLM_R =0.0689f*H+0.0714f*Wt+0.0008f*Age-0.0177f*B_Z_RL-2.7435f;
        else
            B_RL_SLM_R =0.0383f*H+0.0291f*Wt+0.0058f*Age-0.0072f*B_Z_RL+0.5081f;
        if(B_RL_SLM_R<4.0)
            B_RL_SLM_R=4.0f;
        if(B_RL_SLM_R>15)
            B_RL_SLM_R=15;
        return	B_RL_SLM_R;
    }

    /*
    * 左脚肌肉量
    * */
    public float getLLSLM(){
        float B_LL_SLM_R;
        float B_Z_LL = (Z23+Z34-Z24)/2.0f;
        if(Sex==1)
            B_LL_SLM_R =0.0544f*H+0.0742f*Wt-0.0019f*Age-0.0159f*B_Z_LL-0.9357f;
        else
            B_LL_SLM_R =0.0402f*H+0.0303f*Wt+0.0066f*Age-0.0073f*B_Z_LL+0.0978f;
        if(B_LL_SLM_R<4.0)
            B_LL_SLM_R=4.0f;
        if(B_LL_SLM_R>15)
            B_LL_SLM_R=15;
        return B_LL_SLM_R;
    }

    /*
    * 躯干肌肉量
    * */
    private float getTRSLM_Raw(){
        float B_TR_SLM;
        float B_Z_TR = (Z14+Z23)/50.0f;
        if(Sex==1)
            B_TR_SLM =0.1284f*H+0.1579f*Wt-0.0167f*Age+0.0001f*B_Z_TR-4.9226f;
        else
            B_TR_SLM =0.1845f*H+0.1022f*Wt-0.0092f*Age-0.0620f*B_Z_TR-12.3778f;
        if(B_TR_SLM<10.0)
            B_TR_SLM=10.0f;
        if(B_TR_SLM>40)
            B_TR_SLM=40;
        return B_TR_SLM;
    }

    /*
   * 躯干肌肉量
   * */
    public float getTRSLM(){
        float body= getTRSLM_Raw();
        float rightHand=getRASLM();
        float leftHand=getLASLM();
        float rightFoot=getRLSLM();
        float leftFoot=getLLSLM();
        float full=getSLM_Raw();

        float fRet=body;
        if((body+rightHand+leftHand+rightFoot+leftFoot)>=full){
            fRet=body-((body+rightHand+leftHand+rightFoot+leftFoot)-full+ full*0.1f);
        }
        if(fRet<10.0f)
            fRet=10.0f;
        if(fRet>40.0f)
            fRet=40.0f;
        return fRet;
    }


    //type 0--右手 1-左手 2躯干 3右脚  4左脚
    private float getSectionBFR(int type){
        float fRet=0.0f;
        //全身脂肪率
        float fullBody=getBFR_Raw();
        //躯干脂肪率
        float bodyBFR=getTRBFR_Raw();
        float rightHand=getRABFR();
        float leftHand=getLABFR();
        float rightFoot=getRLBFR();
        float leftFood=getLLBFR();
        float maxValue=bodyBFR;
        float minValue=bodyBFR;
        float wl=getWL();
        float hl=getHL();

        if(rightHand>maxValue){maxValue=rightHand;}
        if(leftHand>maxValue){maxValue=leftHand;}
        if(rightFoot>maxValue){maxValue=rightFoot;}
        if(leftFood>maxValue){maxValue=leftFood;}

        if(rightHand<minValue){minValue=rightHand;}
        if(leftHand<minValue){minValue=leftHand;}
        if(rightFoot<minValue){minValue=rightFoot;}
        if(leftFood<minValue){minValue=leftFood;}

        if(fullBody>maxValue ){
            switch(type){
                case 0:
                    fRet=rightHand;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
                case 1:
                    fRet=leftHand;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
                case 2:
                    fRet=fullBody + 2.0f * wl / hl;
                    if(fRet<5.5f)
                        fRet=5.5f;
                    if(fRet>55.0f)
                        fRet=55.0f;
                    break;
                case 3:
                    fRet=rightFoot;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
                case 4:
                    fRet=leftFood;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
            }
        }else if(fullBody<minValue){
            float k=maxValue-2*wl/hl-fullBody;
            switch(type){
                case 0:
                    fRet=rightHand-k;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
                case 1:
                    fRet=leftHand-k;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
                case 2:
                    fRet=bodyBFR-k;
                    if(fRet<5.5f)
                        fRet=5.5f;
                    if(fRet>55.0f)
                        fRet=55.0f;
                    break;
                case 3:
                    fRet=rightFoot-k;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
                case 4:
                    fRet=leftFood-k;
                    if(fRet<4.0f)
                        fRet=4.0f;
                    if(fRet>45.0f)
                        fRet=45.0f;
                    break;
            }
        }else{
            switch(type){
                case 0:
                    fRet=rightHand;
                    break;
                case 1:
                    fRet=leftHand;
                    break;
                case 2:
                    fRet=bodyBFR;
                    break;
                case 3:
                    fRet=rightFoot;
                    break;
                case 4:
                    fRet=leftFood;
                    break;
            }
        }

        return fRet;
    }

//    /*
//  * 获取颈围
//    * * */
//    public float getJW(){
//        float B_Z_TR = (Z14+Z23)/50.0f;
//        return 0.0125f*H+0.1565f*Wt+0.0510f*Age+1.7492f*Sex-0.0542f*B_Z_TR+21.0908f;
//
//    }

    /*
  * 获取腰围
    * * */
    public float getWL(){
        float B_Z_TR = (Z14+Z23)/50.0f;
        return -0.4233f*H+1.0327f*Wt+0.1152f*Age+2.9692f*Sex+1.0453f*B_Z_TR+53.2010f;
    }

    /*
  * 获取臀围
    * * */
    public float getHL(){
        float B_Z_TR = (Z14+Z23)/50.0f;
        return -0.2118f*H+0.5630f*Wt-0.0183f*Age+0.0677f*Sex-0.0031f*B_Z_TR+94.4542f;
    }

//    /*
//  * 获取胸围
//    * * */
//    public float getXW(){
//        float B_Z_TR = (Z14+Z23)/50;
//        return -0.1635f*H+0.5822f*Wt-0.0069f*Age+2.6195f*Sex-0.2180f*B_Z_TR+85.1497f;
//    }
//
//    /*
//  * 获取右上臂围
//    * * */
//    public float getRAW(){
//        float B_Z_RA= (Z12+Z24-Z14)/2;
//        return -0.1797f*H+0.3f*Wt+0.0083f*Age+0.9675f*Sex+0.0006f*B_Z_RA+39.4176f;
//    }
//
//    /*
//  * 获取左上臂围
//    * * */
//    public float getLAW(){
//        float B_Z_LA= (Z12+Z13-Z23)/2;
//        return -0.1760f*H+0.3057f*Wt-0.0090f*Age+0.6122f*Sex-0.0020f*B_Z_LA+40.0361f;
//    }
//
//    /*
//  * 获取右大腿围
//    * * */
//    public float getRLW(){
//        float B_Z_RL = (Z+Z14-Z13)/2;
//        return -0.1441f*H+0.3776f*Wt-0.0341f*Age-0.7584f*Sex-0.0191f*B_Z_RL+57.7680f;
//    }
//
//    /*
//  * 获取左大腿围
//    * * */
//    public float getLLW(){
//        float B_Z_LL = (Z+Z23-Z24)/2;
//        return -0.1275f*H+0.3663f*Wt-0.0374f*Age-0.9621f*Sex-0.0175f*B_Z_LL+55.3384f;
//    }


    //*****************************************************以下方法为直流秤的计算方法******************************************
    //适用于1.2和1.5协议的蓝牙秤，没有电阻值
    //

    /**
     * @return 计算肥胖度（适用于V1.5协议）
     * @param height 身高
     * @param weight 当前体重
     * @param sex 性别
     * @param age 年龄
     */
    public static float calOD(float height,float weight,byte sex,int age){
        float bw=getBW(sex,height);
        return ((weight-bw)/bw)*100.0f;
    }


    /**
     * @return 计算肌肉控制（适用于V1.5协议）
     * @param weight 当前体重
     * @param sex 性别
     * @param slmPercent 肌肉率
     */
    public static float calMC(float weight,byte sex,float slmPercent){
        float bm=getBM(weight,sex);
        float slm=calSLM(weight,slmPercent);
        return bm-slm;
    }

    /**
     * @return 计算脂肪控制（适用于V1.5协议）
     * @param weight 当前体重
     * @param sex 性别
     * @param height 身高
     * @param bfr 脂肪百分比
     * @param slmPercent 肌肉百分比
     */
    public static float calFC(float weight,byte sex,float height,float bfr,float slmPercent){
        float mc=calMC(weight,sex,slmPercent);
        float bw=getBW(sex,height);
        float fc=0.0f;
        if(weight<bw){
            fc=bw-weight-mc;
        }else{
            float fm=(bfr / 100.0f) * weight;
            if(sex==1){
                fc=(0.15f * (weight + mc)-fm)/0.85f;
            }else{
                fc=(0.2f * (weight + mc)-fm)/0.8f;
            }
        }
        return fc;

    }


    /**
     * @return 计算蛋白质（适用于V1.5协议）
     * @param weight 当前体重
     * @param sex 性别
     * @param bfr 脂肪率
     * @param tfr 水率
     */
    public static float calPM(float weight,byte sex,float bfr,float tfr){
        float yan=2.5f;
        if(sex==1){
            yan=3.0f;
        }

        return 100.0f-bfr-tfr-((yan/weight) * 100);
    }

    /**
     * @return 计算瘦体重（适用于V1.5协议）
     * @param weight 当前体重
     * @param bfr 脂肪百分比
     */
    public static float calLBM(float weight,float bfr) {
        float fm=calFM(weight,bfr);
        return weight-fm;
    }

    /**
     * @return 获取脂肪重（适用于V1.5协议）
     * @param weight 当前体重
     * @param bfr 脂肪百分比
     */
    public static float calFM(float weight,float bfr){
        return bfr * weight /100.0f;
    }

    /**
     * @return 获取肌肉重（适用于V1.5协议）
     * @param weight 当前体重
     * @param slmPercent 肌肉百分比
     */
    public static float calSLM(float weight,float slmPercent){
        return slmPercent /100.0f * weight ;
    }


    /**
     * @return 根据用户传入的参数计算身体年龄（适用于V1.5协议）
     * @param height 身高
     * @param weight 当前体重
     * @param sex 性别
     * @param age 年龄
     * @param bfr 脂肪百分比
     */
    public static int calBodyAge(float height,float weight,byte sex,int age,float bfr){
        int bodyAge= (int)Math.round(-0.2387f * height + 0.2258f * weight + 0.3452 * age + 1.2675 * bfr + 9.5081);
        if((bodyAge-age)>10){
            bodyAge=age+10;
        }else if((age-bodyAge)>10){
            bodyAge=age-10;
        }
        if(bodyAge<18){
            bodyAge=18;
        }else if(bodyAge>80){
            bodyAge=80;
        }
        return bodyAge;
    }



    /**
     * @return 根据用户传入的参数计算体型（适用于V1.5协议）
     * @param height 身高
     * @param weight 当前体重
     * @param sex 性别
     * @param age 年龄
     * @param bfr 脂肪百分比
     */
    public static float calShape(float height,float weight,byte sex,int age,float bfr){
        float bmi=(weight / (height * height)) * 100 * 100;
        return getShape(bfr,bmi,sex,age);
    }

    /**
     * @return 根据用户传入的参数计算人体成分得分
     * @param height 身高
     * @param weight 当前体重
     * @param sex 性别
     * @param age 年龄
     * @param bfr 脂肪百分比
     * @param slm 肌肉重
     * @param vfr 内脏脂肪等级（对于直流秤传0）
     */
    public static float calScore(float height,float weight,byte sex,int age,float bfr,float slm,float vfr){
        float BMI = (weight / (height * height)) * 100 * 100;
        float BFR = bfr;
        float S_SBMI = -5.686f * (BMI * BMI) + 241.7f * BMI - 2470f;
        if (S_SBMI < 55.0f)
            S_SBMI = 55.0f;
        if (S_SBMI > 96.0f)
            S_SBMI = 96.0f;
        float BFR1 = BFR + 0.03f * age;

        float S_BFR, BM;
        if (sex == 1) {//男
            S_BFR = 0.0001085f * (BFR1 * BFR1 * BFR1 * BFR1) - 0.003181f * (BFR1 * BFR1 * BFR1) - 0.2952f * (BFR1 * BFR1) + 10.85f * BFR1 + 0.4248f;
            BM = weight * 0.77f;
        } else {
            S_BFR = 0.0002469f * (BFR1 * BFR1 * BFR1 * BFR1) - 0.02788f * (BFR1 * BFR1 * BFR1) + 0.9597f * (BFR1 * BFR1) - 10.02f * BFR1 + 80.42f;
            BM = weight * 0.735f;
        }
        if (S_BFR < 55.0) {
            S_BFR = 55.0f;
        }

        float SLM = slm;
        float S_SLM = 90.0f + SLM - BM;
        if (S_SLM > 100.0f) S_SLM = 100.0f;

        float VFR = vfr;
        float S_VFR = 0;
        if (VFR >= 15.0)
            S_VFR = -50.0f;
        else
            S_VFR = 0.007212f * (VFR * VFR * VFR * VFR * VFR) - 0.2825f * (VFR * VFR * VFR * VFR) + 3.912f * (VFR * VFR * VFR) - 22.27f * (VFR * VFR) + 30.38f * VFR + 89.35f;
        if (S_VFR < -50.0f)
            S_VFR = -50.0f;

        float Coff_a = 0.4f;
        float Coff_b = 0.4f;
        float Coff_c = 0.1f;
        float Coff_d = 0.08f;

        if (vfr == 0) {
            Coff_a = 0.48f;
            Coff_b = 0.4f;
            Coff_c = 0.1f;
            Coff_d = 0.0f;
        }

        int iScore = (int) (Coff_a * S_SBMI + Coff_b * S_BFR + Coff_c * S_SLM + Coff_d * S_VFR);
        if (iScore < 45)
            iScore = 45;
        if (iScore > 100)
            iScore = 100;
        return iScore;
    }
}
