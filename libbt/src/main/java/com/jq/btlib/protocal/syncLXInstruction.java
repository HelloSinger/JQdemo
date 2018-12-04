package com.jq.btlib.protocal;

import com.jq.btlib.util.BytesUtil;

/**
 * Created by Administrator on 2017/6/29.
 */

public class syncLXInstruction {
    public static byte[] Register(String mac){
        byte[] bRet=new byte[11];
        bRet[0]=0x10;
        bRet[1]=0x09;
        bRet[2]=0x00;
        bRet[3]=0x01;
        bRet[4]= BytesUtil.getMacBytes(mac)[0];
        bRet[5]= BytesUtil.getMacBytes(mac)[1];
        bRet[6]= BytesUtil.getMacBytes(mac)[2];
        bRet[7]= BytesUtil.getMacBytes(mac)[3];
        bRet[8]= BytesUtil.getMacBytes(mac)[4];
        bRet[9]= BytesUtil.getMacBytes(mac)[5];
        bRet[10]= 0x01;
        return bRet;
    }

    public static byte[] LoginReply(byte[] verifyCode){
        byte[] bRet=new byte[13];
        bRet[0]=0x10;
        bRet[1]=0x0B;
        bRet[2]=0x00;
        bRet[3]=0x08;
        //登陆结果
        bRet[4]=0x01;
        //验证码
        bRet[5]= verifyCode[0];
        bRet[6]= verifyCode[1];
        bRet[7]= verifyCode[2];
        bRet[8]= verifyCode[3];
        bRet[9]= verifyCode[4];
        bRet[10]= verifyCode[5];
        //工作类型
        bRet[11]= 0x00;
        //手机平台
        bRet[12]= 0x02;

        return bRet;
    }

    public static byte[] InitReply(){
        int currentTime=(int)(System.currentTimeMillis() /1000);
        byte[] bRet=new byte[10];
        bRet[0]=0x10;
        bRet[1]=0x08;
        bRet[2]=0x00;
        bRet[3]=0x0A;
        bRet[4]=0x18;
        BytesUtil.putInt(bRet, currentTime, 5);
        bRet[9]= 0x50;
        return bRet;
    }

    public static byte[] GetMeasureData(){
        byte[] bRet=new byte[6];
        bRet[0]=0x10;
        bRet[1]=0x04;
        bRet[2]=0x48;
        bRet[3]=0x01;
        bRet[4]=0x01;
        bRet[5]=0x01;

        return bRet;
    }

}
