package com.jq.btc.bluettooth.report.haier.item;

import android.content.Context;

import com.jq.btc.app.R;
import com.jq.code.code.util.DecimalFormatUtils;
import com.jq.code.code.util.StandardUtil;

/**
 * Created by mihw on 2018/6/12.
 */

public class Rn8Item {

    private int standard ;
    private float value ;
    private boolean isAxunge ;

    public int getStandard() {
        return standard;
    }

    public void setStandard(int standard) {
        this.standard = standard;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isAxunge() {
        return isAxunge;
    }

    public void setAxunge(boolean axunge) {
        isAxunge = axunge;
    }
    public String getValueStr(Context context){
        return isAxunge ? DecimalFormatUtils.getOne(value)  : StandardUtil.getWeightExchangeValueforVer2(context, value, "", (byte) 1);
    }
    public int getStandardBg(){
        if(standard == 0){
            return R.mipmap.elec_standard_low ;
        }else if(standard == 1){
            return R.mipmap.elec_standard_normal ;
        }else if(standard == 2){
            return R.mipmap.elec_standard_high ;
        }else {
            return -1 ;
        }
    }
    public int getStandardText(){
        if(standard == 0){
            return R.string.elec_standard_low ;
        }else if(standard == 1){
            return R.string.elec_standard_normal ;
        }else if(standard == 2){
            return R.string.elec_standard_high ;
        }else {
            return -1 ;
        }
    }

}
