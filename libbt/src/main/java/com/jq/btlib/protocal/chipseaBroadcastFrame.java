package com.jq.btlib.protocal;

import android.util.Log;

import com.jq.btlib.model.WeightParserReturn;
import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.util.BytesUtil;
import com.jq.btlib.util.WeightUnitUtil;

public class chipseaBroadcastFrame {
	//数据域长度
	public byte dataLength;

	public String name;
	//版本号
	public byte version;
	//设备类型 0:人体秤 1:脂肪秤 2:广播体脂秤
	public byte deviceType;
	//命令字节 0:未锁定数据 1:代表锁定数据
	public byte cmdId;
	//重量
	public double weight;
	//产品id
	public int productId;
	//mac
	public String mac;
	//秤协议类型 OKOK--   JD--
	public String procotalType;

	//蓝牙秤显示的体重数值
	public String scaleWeight;
	public byte scaleProperty;
	//测试流水号
	public int measureSeqNo;
	//电阻
	public float r1;

	//正负
	public int  type_fu;
	public String status;
	public int kitchens ;
	@Override
	public String toString() {
		return "chipseaBroadcastFrame{" +
				"version=" + version +
				", deviceType=" + deviceType +
				", cmdId=" + cmdId +
				", weight=" + weight +
				", productId=" + productId +
				", mac='" + mac + '\'' +
				", procotalType='" + procotalType + '\'' +
				", scaleWeight='" + scaleWeight + '\'' +
				", scaleProperty=" + scaleProperty +
				", measureSeqNo=" + measureSeqNo +
				", r1=" + r1 +
				'}';
	}

	public chipseaBroadcastFrame(String sMac){
		version=0;
		deviceType=1;
		cmdId=0;
		weight=0;
		productId=0;
		mac=sMac;
	}

	public chipseaBroadcastFrame(String sMac,String se){
		mac=sMac;
		okokBroadcastParser11(se);
	}

	private void okokParser(byte[] data,String sMac){
		this.mac=sMac;
		version=data[1];
		dataLength=data[2];
		switch(version){
			case 0x10:
				deviceType=data[3];
				cmdId = data[4];
				scaleProperty=cmdId;
				break;
			case 0x11:
				//cmdId = data[3];
				scaleProperty= data[3];
				deviceType=data[4];
				break;
		}

		if(deviceType==0x00){
			//人体秤
			switch(version){
				case 0x10:
					byte[] bweight = BytesUtil.subBytes(data,
							8, 2);
					weight=BytesUtil.bytesToInt(bweight) / 10f;
					break;
				case 0x11:
					cmdId=BytesUtil.getCmdId(scaleProperty);
					WeightParserReturn parserReturn=WeightUnitUtil.Parser(data[5],data[6],scaleProperty,false);
					weight=parserReturn.kgWeight;
					scaleWeight=parserReturn.scaleWeight;

					byte[] bProduct = BytesUtil.subBytes(data,
							7, 4);
					productId=BytesUtil.bytesToInt(bProduct);
					break;
			}

		}else{
			byte[] bweight = BytesUtil.subBytes(data,
					5, 2);
			//weight=BytesUtil.bytesToInt(bweight) / 10f;
			//scaleWeight="" + weight;
			cmdId=BytesUtil.getCmdId(scaleProperty);
			WeightParserReturn parserReturn=WeightUnitUtil.Parser(data[5],data[6],scaleProperty,false);
			weight=parserReturn.kgWeight;
			scaleWeight=parserReturn.scaleWeight;

			byte[] bProduct = BytesUtil.subBytes(data, 7, 4);
			productId=BytesUtil.bytesToInt(bProduct);

		}
	}

	private void okokBroadcastParser(byte[] data,String sMac){
		this.mac=sMac;
		version=data[1];
		dataLength=data[2];

		byte[] bProduct = BytesUtil.subBytes(data, 3, 4);
		productId=BytesUtil.bytesToInt(bProduct);
		if(data[7]==0){
			deviceType=0;
		}else{
			deviceType=2;
		}
		scaleProperty= data[8];
		cmdId=BytesUtil.getCmdId(scaleProperty);
		if(cmdId>0) {
			measureSeqNo=data[9] & 0xFF;

			WeightParserReturn parserReturn = WeightUnitUtil.Parser(data[10], data[11], scaleProperty, false);
			weight = parserReturn.kgWeight;
			scaleWeight = parserReturn.scaleWeight;

			byte[] bTmp = BytesUtil.subBytes(data, 12, 2);
			r1=(BytesUtil.bytesToInt(bTmp) / 10.0f);
		}else{
			measureSeqNo=0;
			weight=0;
			scaleWeight="";
			r1=0;
		}

	}

	private void okokBroadcastParser11(String sMac){
		Log.v("===jq", "" + sMac);
		kitchens = 1;
		String[] split = sMac.split("x");
		weight = Integer.valueOf(split[1]);
		String substring = split[0].substring(0, 1);
		if (substring.equals("1")) {
			type_fu= 1;
			weight = 0;
			return;
		}else {
			type_fu= 0;
			weight = Integer.valueOf(split[1]);
		}

		String substring2 = split[0].substring(1, 5);
		if(substring2.equals("0000")){
			weight = Integer.valueOf(split[1]);
		}else if(substring2.equals("0010")){
			weight = (int)(Integer.valueOf(split[1])*1.0288);
		}else if(substring2.equals("1000")){
			weight = (int)(Integer.valueOf(split[1])*2);
		}else if(substring2.equals("0011")){//oz
			weight = (int)(Integer.valueOf(split[1])*28.35);
		} else if(substring2.equals("0110")){//lb
			weight = (int)(Integer.valueOf(split[1])*453.59);
		}
//        else if(substring2.equals("011")){//lb/oz
//            weightnums = (int)(Integer.valueOf(split[1])*(453.59/28.35));
//        }

		status = split[0].substring(7, 8);

		Log.v("===chipsea11", "" + weight);
	}

	private void okokCloudParser(byte[] data,String sMac){
		this.mac=sMac;
		deviceType=1;
		version=data[2];
		scaleProperty= data[3];
		cmdId=0;

		WeightParserReturn parserReturn=WeightUnitUtil.Parser(data[5],data[4],scaleProperty,true);
		weight=parserReturn.kgWeight;
		scaleWeight=parserReturn.scaleWeight;

		byte[] bProduct = BytesUtil.subBytes(data,
				6, 4);
		productId=BytesUtil.bytesToInt(bProduct);

	}


	public chipseaBroadcastFrame(byte[] bBuffer,String sMac) throws FrameFormatIllegalException{

		if(bBuffer == null){
			throw new FrameFormatIllegalException("帧格式错误 -- 帧为空");
		}

		if(bBuffer[0] == (byte)0xCA){
			if(bBuffer[1]==(byte)0x20){
				//广播体脂秤
				okokBroadcastParser(bBuffer,sMac);
			}else{
				okokParser(bBuffer,sMac);
			}
		}else if(bBuffer[0]==(byte)0xFF){
			okokCloudParser(bBuffer,sMac);
		} else{
			throw new FrameFormatIllegalException("帧格式错误 -- 帧头错误");
		}

		//data=BytesUtil.subBytes(bBuffer, 5, 20);

	}
}
