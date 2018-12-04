package com.jq.btlib.protocal;

import com.jq.btlib.util.BytesUtil;

public class syncJdInstruction {
	public static byte[] syncUserInformation(byte sex,byte age,short height){
		byte[] bRet=new  byte[20];
		bRet[0]=0x00; //包序号
		bRet[1]=(byte)0xF1; //命讼字
		bRet[2]=0x05;
		bRet[3]=0x01; //用户Id,固定1
		bRet[4]=sex;
		bRet[5]=age;
		
		byte[] bHeight=BytesUtil.shortToByteArray(height);
		bRet[6]=bHeight[0];
		bRet[7]=bHeight[1];
		
		for(int i=8;i<20;i++){
			bRet[i]=0x00;
		}			
		return bRet;
	}
	
	//设备基本信息同步
	public static byte[] syncDeviceInformation(){
		byte[] bRet=new  byte[20];
		bRet[0]=0x00; //包序号
		bRet[1]=(byte)0xF0; //命讼字
		
		for(int i=2;i<20;i++){
			bRet[i]=0x00;
		}			
		return bRet;
	}
}
