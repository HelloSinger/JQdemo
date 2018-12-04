package com.jq.btlib.protocal;

import com.jq.btlib.model.BtGattAttr;
import com.jq.btlib.model.WeightParserReturn;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.util.BytesUtil;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.btlib.util.WeightUnitUtil;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/5.
 */

public class okCloudV3StraightFrame implements iStraightFrame {
    private CsFatScale fatScale = null;

    @Override
    public enumProcessResult process(byte[] bBuffer,String uuid) throws FrameFormatIllegalException {
        enumProcessResult processResult=enumProcessResult.Wait_Scale_Data;

        int iScaleType=0; //秤类型  1--体重秤 0--脂肪秤

        fatScale=new CsFatScale();
        if(bBuffer[0]==(byte)0xC0){
            iScaleType=1;
        }else if(bBuffer[0]==(byte)0xC4){
            iScaleType=0;
        }else if(bBuffer[0]==(byte)0xC8){
            iScaleType=0;
        }else{
            throw new FrameFormatIllegalException("帧格式错误 -- 类型不对");
        }

        fatScale.setDeviceType(iScaleType);
        if(uuid.equalsIgnoreCase(BtGattAttr.CHIPSEA_CHAR_RX_UUID)){
            fatScale.setLockFlag((byte)0);
            fatScale.setWeighingDate(new Date());
        }else{
            fatScale.setLockFlag((byte)1);
            byte[] bTmp=BytesUtil.subBytes(bBuffer, 2, 4);
            long scaleUTC=(long)BytesUtil.bytesToInt(bTmp)*1000;
            Date dScale= new Date(scaleUTC);
            fatScale.setWeighingDate(dScale);
        }
        fatScale.setHistoryData(false);
        if(uuid.compareToIgnoreCase(BtGattAttr.BODY_COMPOSITION_HISTORY)==0){
            fatScale.setHistoryData(true);
        }

        byte scaleProperty=bBuffer[1];
        WeightParserReturn parserReturn= WeightUnitUtil.Parser(bBuffer[7],bBuffer[6],scaleProperty,true);
        fatScale.setWeight(parserReturn.kgWeight * 10);
        fatScale.setScaleWeight(parserReturn.scaleWeight);
        fatScale.setScaleProperty(changeCloudProperty2Standard(scaleProperty));

        if(bBuffer[0]==(byte)0xC4){
            float Resistance=BytesUtil.bytesToInt(new byte[]{bBuffer[9],bBuffer[8]});
            Resistance=(Resistance * 0.1f);
            fatScale.setImpedance(Resistance);
        }else if(bBuffer[0]==(byte)0xC8){
            float Resistance=BytesUtil.bytesToInt(new byte[]{bBuffer[9],bBuffer[8]});
            Resistance=(Resistance * 0.1f);
            fatScale.setImpedance(Resistance);
            if(Resistance>0) {
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator('.');
                DecimalFormat df1 = new DecimalFormat("#0.0", symbols);

                float Z12 = BytesUtil.bytesToInt(new byte[]{bBuffer[11], bBuffer[10]});
                Z12 = (Z12 * 0.1f);

                float Z13 = BytesUtil.bytesToInt(new byte[]{bBuffer[13], bBuffer[12]});
                Z13 = (Z13 * 0.1f);

                float Z14 = BytesUtil.bytesToInt(new byte[]{bBuffer[15], bBuffer[14]});
                Z14 = (Z14 * 0.1f);

                float Z23 = BytesUtil.bytesToInt(new byte[]{bBuffer[17], bBuffer[16]});
                Z23 = (Z23 * 0.1f);

                float Z24 = BytesUtil.bytesToInt(new byte[]{bBuffer[19], bBuffer[18]});
                Z24 = (Z24 * 0.1f);

                //格式定义 type1:value | type2:value
                String remark = "1:" + df1.format(Z12) + "," + df1.format(Z13) + "," + df1.format(Z14) + "," + df1.format(Z23) + "," + df1.format(Z24);
                fatScale.setRemark(remark);
            }
        }

        processResult=enumProcessResult.Received_Scale_Data;
        return processResult;
    }

    private byte changeCloudProperty2Standard(byte scaleProperty){
        CsBtUtil_v11.Weight_Unit scaleUnit = BytesUtil.getUnit(scaleProperty);
        CsBtUtil_v11.Weight_Digit scaleDigit=BytesUtil.getCloudDigit(scaleProperty);
        String s1="000";
        if (scaleUnit == CsBtUtil_v11.Weight_Unit.JIN) {
            s1+="01";
        } else if (scaleUnit == CsBtUtil_v11.Weight_Unit.LB) {
            s1+="10";
        } else if (scaleUnit == CsBtUtil_v11.Weight_Unit.ST) {
            s1+="11";
        } else {
            s1+="00";
        }

        if (scaleDigit == CsBtUtil_v11.Weight_Digit.ONE) {
            s1+="001";
        } else if (scaleDigit == CsBtUtil_v11.Weight_Digit.TWO) {
            s1+="101";
        } else {
            s1+="011";
        }

        return (byte)(Integer.parseInt(s1, 2));
    }

    @Override
    public CsFatScale getFatScale() {
        return fatScale;
    }
}
