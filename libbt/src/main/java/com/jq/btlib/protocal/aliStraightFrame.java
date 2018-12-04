package com.jq.btlib.protocal;

import com.jq.btlib.model.BtGattAttr;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.util.BytesUtil;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/23.
 */

public class aliStraightFrame implements iStraightFrame {
    private CsFatScale fatScale = null;

    @Override
    public enumProcessResult process(byte[] bBuffer,String uuid) throws FrameFormatIllegalException {
        enumProcessResult processResult=enumProcessResult.Wait_Scale_Data;

        int iScaleType=0; //秤类型  1--体重秤 0--脂肪秤

        if(bBuffer == null){
            throw new FrameFormatIllegalException("帧格式错误 -- 帧为空");
        }

        if(bBuffer.length<14){
            throw new FrameFormatIllegalException("帧格式错误 -- 帧长度不对");
        }

        if(bBuffer[0]==(byte)0x02 && bBuffer[1]==(byte)0x00){
            iScaleType=1;
        }else if(bBuffer[0]==(byte)0x06 && bBuffer[1]==(byte)0x03) {
            iScaleType = 0;
        }else if(bBuffer[0]==(byte)0x06 && bBuffer[1]==(byte)0x23){
            iScaleType=0;
        }else{
            throw new FrameFormatIllegalException("帧格式错误 -- 类型不对");
        }

        fatScale=new CsFatScale();
        fatScale.setLockFlag((byte)1);
        fatScale.setDeviceType(iScaleType);
        int iYear= BytesUtil.bytesToInt(new byte[]{bBuffer[3],bBuffer[2]});
        //Calendar cWeight = Calendar.getInstance();
        //cWeight.set(iYear,bBuffer[4],bBuffer[5],bBuffer[6],bBuffer[7],bBuffer[8]);
        String dateStr=iYear + "-" + bBuffer[4] + "-" + bBuffer[5] + " " + bBuffer[6] + ":" + bBuffer[7] + ":" + bBuffer[8];
        //fatScale.setWeighingDate(cWeight.getTime());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime1 = new Date();
        try {
            dateTime1 = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fatScale.setWeighingDate(dateTime1);

        fatScale.setHistoryData(false);
        if(uuid.compareToIgnoreCase(BtGattAttr.BODY_COMPOSITION_HISTORY)==0){
            fatScale.setHistoryData(true);
        }

        if(iScaleType == 0){
            float Resistance=BytesUtil.bytesToInt(new byte[]{bBuffer[10],bBuffer[9]});
            Resistance=(Resistance * 0.1f);
            fatScale.setImpedance(Resistance);
        }
        int iWeight=BytesUtil.bytesToInt(new byte[]{bBuffer[12],bBuffer[11]});
        float scaleWeight=iWeight * 0.01f;
        BigDecimal bigConvert=new BigDecimal(scaleWeight);
        scaleWeight=bigConvert.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

        fatScale.setScaleWeight(String.valueOf(scaleWeight));
        fatScale.setWeight(scaleWeight * 10);
        fatScale.setScaleProperty((byte)5);
        processResult=enumProcessResult.Received_Scale_Data;

        return processResult;
    }

    @Override
    public CsFatScale getFatScale() {
        return fatScale;
    }

}
