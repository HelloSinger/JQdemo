package com.jq.btlib.protocal;

import com.jq.btlib.model.device.CsFatConfirm;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.util.BytesUtil;
import com.jq.btlib.model.WeightParserReturn;
import com.jq.btlib.util.WeightUnitUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class chipseaStraightFrame implements iStraightFrame {
	private CsFatScale fatScale = null;
	private CsFatConfirm fatConfirm=null;

	//在线数据缓存
	private HashMap<Byte,byte[]> lstBuffer=new HashMap<Byte,byte[]>();

	//历史数据缓存
	private HashMap<Byte,byte[]> lstHistoryBuffer=new HashMap<Byte,byte[]>();


	private ArrayList<Byte> lstRoleBuffer=new ArrayList<Byte>();

	
	/**
     * @Description 将包信息拆分为总包数及当前包数
     * @param bPackInfo
     * 包信息
     * @return byte[0] 总包数  byte[1]当前包数
     */
	private byte[] splitPackInfo(byte bPackInfo){
		byte[] bRet=new byte[2];
		byte bTotal=(byte) (bPackInfo <<4);
		bRet[0]=(byte) (bTotal >>4);
		
		bRet[1]=(byte) (bPackInfo >>4);			
		return bRet;
	}
	
	private byte[] ConverList2ByteArray(ArrayList<Byte> lstData){
		byte[] bRet=new byte[lstData.size()];
		
		for(int i=0;i<bRet.length;i++){
			bRet[i]=lstData.get(i);
		}
		
		return bRet;
	}

	private byte[] concat(byte[] a, byte[] b) {
		byte[] c= new byte[a.length+b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	private byte[] ConvertMap2ByteArray(boolean isHistory){
		byte[] bRet=new byte[0];
		if(isHistory){
			for (int i = 0; i < lstHistoryBuffer.size(); i++) {
				bRet = concat(bRet, lstHistoryBuffer.get((byte) (i + 1)));
			}
		}else {
			for (int i = 0; i < lstBuffer.size(); i++) {
				bRet = concat(bRet, lstBuffer.get((byte) (i + 1)));
			}
		}
		return bRet;
	}


	@Override
	public enumProcessResult process(byte[] bBuffer,String uuid) throws FrameFormatIllegalException {
		enumProcessResult processResult=enumProcessResult.Wait_Scale_Data;
		
		if(bBuffer == null){
			throw new FrameFormatIllegalException("帧格式错误 -- 帧为空");			
		}
		if(bBuffer[0] != (byte)0xCA){			
			throw new FrameFormatIllegalException("帧格式错误 -- 不是正确的帧头");			
		}
		
		byte[] bTmp;
		int iIndex=0;
		byte scaleProperty;
		byte version=bBuffer[1];
		byte dataLength=bBuffer[2];
		byte cmdId =0;
		byte[] bPackInfos;
		int ipackLen;
		WeightParserReturn parserReturn;
		if(version==0x10){
			scaleProperty= bBuffer[4];
			byte[] weight = BytesUtil.subBytes(bBuffer,
					5, 2);
			byte[] axunge = BytesUtil.subBytes(bBuffer,
					7, 2);
			byte[] water = BytesUtil.subBytes(bBuffer,
					9, 2);
			byte[] muscle = BytesUtil.subBytes(bBuffer,
					11, 2);
			byte[] bmr = BytesUtil.subBytes(bBuffer,
					13, 2);
			byte[] visceral_fat = BytesUtil.subBytes(bBuffer,
					15, 2);
			byte[] bone = BytesUtil.subBytes(bBuffer,
					17, 1);
			fatScale = new CsFatScale();
			fatScale.setHistoryData(false);
			cmdId=BytesUtil.getCmdId(scaleProperty);
			fatScale.setLockFlag(cmdId);
			parserReturn= WeightUnitUtil.Parser(bBuffer[5], bBuffer[6], scaleProperty,false);
			fatScale.setWeight(parserReturn.kgWeight*10);
			fatScale.setScaleWeight(parserReturn.scaleWeight);
			//fatScale.setWeight(BytesUtil.bytesToInt(weight));
			fatScale.setScaleProperty(scaleProperty);
			if(cmdId>0){
				fatScale.setAxunge(BytesUtil.bytesToInt(axunge));
				fatScale.setWater(BytesUtil.bytesToInt(water));
				fatScale.setMuscle(BytesUtil.bytesToInt(muscle));
				fatScale.setBmr(BytesUtil.bytesToInt(bmr));
				fatScale.setVisceral_fat(BytesUtil.bytesToInt(visceral_fat));
				fatScale.setBone(BytesUtil.bytesToInt(bone));
			}
				
			processResult=enumProcessResult.Received_Scale_Data;
		}else if(version==0x11){
			cmdId= bBuffer[3];
			switch(cmdId){
			case 0x00:
			case 0x01:
			//透传过程数据
				byte[] weight = BytesUtil.subBytes(bBuffer,
						5, 2);

				fatScale = new CsFatScale();
				fatScale.setHistoryData(false);
				//fatScale.setWeight(BytesUtil.bytesToInt(weight));
				fatScale.setLockFlag(cmdId);
				fatScale.setScaleProperty(bBuffer[11]);
				parserReturn= WeightUnitUtil.Parser(weight[0], weight[1], fatScale.getScaleProperty(),false);
				fatScale.setWeight(parserReturn.kgWeight*10);
				fatScale.setScaleWeight(parserReturn.scaleWeight);

				processResult=enumProcessResult.Received_Scale_Data;
				break;
			case 0x12:
			case 0x13:
				if (packageProcess(bBuffer)){
					//包传输完毕
					byte[] myArray;
					if(cmdId==0x12) {
						myArray = ConvertMap2ByteArray(false);
					}else{
						myArray = ConvertMap2ByteArray(true);
					}

					fatScale = new CsFatScale();
					if(cmdId==0x12){
						fatScale.setHistoryData(false);
					}else{
						bTmp=BytesUtil.subBytes(myArray, 0, 4);
						long scaleUTC=(long)BytesUtil.bytesToInt(bTmp)*1000;
						Date dScale= new Date(scaleUTC);

						fatScale.setWeighingDate(dScale);
						fatScale.setHistoryData(true);
					}
					scaleProperty=myArray[4];
					iIndex=6;
					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 4);
					fatScale.setRoleId(BytesUtil.bytesToInt(bTmp));
					iIndex+=4;

					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 2);
					fatScale.setScaleProperty(scaleProperty);
					//fatScale.setWeight(BytesUtil.bytesToInt(bTmp));
					parserReturn= WeightUnitUtil.Parser(bTmp[0], bTmp[1], scaleProperty,false);
					fatScale.setWeight(parserReturn.kgWeight*10);
					fatScale.setScaleWeight(parserReturn.scaleWeight);
					iIndex+=2;

					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 2);
					fatScale.setAxunge(BytesUtil.bytesToInt(bTmp));
					iIndex+=2;

					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 2);
					fatScale.setWater(BytesUtil.bytesToInt(bTmp));
					iIndex+=2;

					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 2);
					fatScale.setMuscle(BytesUtil.bytesToInt(bTmp));
					iIndex+=2;

					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 2);
					fatScale.setBmr(BytesUtil.bytesToInt(bTmp));
					iIndex+=2;

					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 2);
					fatScale.setVisceral_fat(BytesUtil.bytesToInt(bTmp));
					iIndex+=2;

					bTmp=BytesUtil.subBytes(myArray,
							iIndex, 1);
					fatScale.setBone(BytesUtil.bytesToInt(bTmp));
					iIndex+=1;

					//1.3版肌肉含量单位为kg,需要转化为百份比
					//fatScale.setMuscle((int)((fatScale.getMuscle() / fatScale.getWeight())*1000));
					double musclePercent=(fatScale.getMuscle() / fatScale.getWeight())*100;
					if(musclePercent>=50.0f){
						//解决天晟问题
						musclePercent=fatScale.getMuscle();
						if(musclePercent>=50.0f){
							musclePercent=50.0f;
						}
					}
					fatScale.setMuscle((int)(musclePercent * 10));

					fatScale.setLockFlag((byte)1);
					if(cmdId==0x12) {
						lstBuffer.clear();
						lstBuffer=null;
					}else{
						lstHistoryBuffer.clear();
						lstHistoryBuffer=null;
					}
					processResult=enumProcessResult.Received_Scale_Data;
				}else{
					processResult=enumProcessResult.Wait_Scale_Data;
				}
				break;
			case 0x14:
				//多用户匹配确认指令
				bPackInfos=splitPackInfo(bBuffer[4]);
				ipackLen=dataLength-2; //去掉命令字 和分包信息
				if(lstRoleBuffer==null){
					lstRoleBuffer=new ArrayList<Byte>();
				}
				for(int i=0;i<ipackLen;i++){
					lstRoleBuffer.add(bBuffer[5+i]);
				}
				if (bPackInfos[0]==bPackInfos[1]){
					byte[] confirmArray = ConverList2ByteArray(lstRoleBuffer);
					fatConfirm=new CsFatConfirm();

					iIndex=0;
					bTmp=BytesUtil.subBytes(confirmArray,
							iIndex, 2);
					iIndex+=2;
					scaleProperty=confirmArray[iIndex];
					parserReturn= WeightUnitUtil.Parser(bTmp[0], bTmp[1], scaleProperty,false);
					fatConfirm.setScaleProperty(scaleProperty);
					fatConfirm.setWeight(parserReturn.kgWeight*10);
					fatConfirm.setScaleWeight(parserReturn.scaleWeight);
					//fatConfirm.setWeight(BytesUtil.bytesToInt(bTmp));

					iIndex+=1;
					ArrayList<Integer> lstRole=new ArrayList<Integer>();
					for(int i=iIndex;i<confirmArray.length;i+=4){
						bTmp=BytesUtil.subBytes(confirmArray,
								i, 4);
						lstRole.add(BytesUtil.bytesToInt(bTmp));
					}
					fatConfirm.setMatchedRoleList(lstRole);
					lstRoleBuffer.clear();
					lstRoleBuffer=null;
					processResult=enumProcessResult.Match_User_Msg;

				}else{
					processResult=enumProcessResult.Wait_Scale_Data;
				}

				break;
			}
		}
		
		return processResult;
	}


	public CsFatConfirm getFatConfirm(){
		return fatConfirm;
	}

	@Override
	public CsFatScale getFatScale() {
		// TODO Auto-generated method stub
		return fatScale;
	}

	private boolean packageProcess(byte[] bBuffer){
		boolean bFinish=false;
		//当前包长度
		byte dataLength=bBuffer[2];
		//命令类型
		int cmdId= bBuffer[3];
		int ipackLen=dataLength-2; //去掉数据域中的命令字和分包信息
        //byte[0] 总包数  byte[1]当前包数
		byte[] bPackInfos=splitPackInfo(bBuffer[4]);
		byte[] bData=new byte[ipackLen];

		for (int i = 0; i < ipackLen; i++) {
			bData[i]=bBuffer[5 + i];
		}

		if(cmdId==0x12) {
			if(bPackInfos[1]==1) {
				if(lstBuffer!=null){
                    lstBuffer.clear();
                }else{
                    lstBuffer = new HashMap<Byte, byte[]>();
                }
			}

			if(lstBuffer.containsKey(bPackInfos[1])){
				lstBuffer.remove(bPackInfos[1]);
			}
			lstBuffer.put(bPackInfos[1],bData);

			if(bPackInfos[0]==bPackInfos[1]){
				if(lstBuffer.size()!=(int)bPackInfos[0]){
					lstBuffer.clear();
				}else{
					bFinish=true;
				}
			}
		}else{
			if(bPackInfos[1]==1) {
				if(lstHistoryBuffer!=null){
					lstHistoryBuffer.clear();
				}else{
					lstHistoryBuffer = new HashMap<Byte, byte[]>();
				}
			}

			if(lstHistoryBuffer.containsKey(bPackInfos[1])){
				lstHistoryBuffer.remove(bPackInfos[1]);
			}
			lstHistoryBuffer.put(bPackInfos[1],bData);

			if(bPackInfos[0]==bPackInfos[1]){
				if(lstHistoryBuffer.size()!=bPackInfos[0]){
					lstHistoryBuffer.clear();
				}else{
					bFinish=true;
				}
			}
		}

		return bFinish;

	}


}
