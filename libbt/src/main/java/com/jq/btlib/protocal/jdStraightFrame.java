package com.jq.btlib.protocal;

import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.util.BytesUtil;

import java.math.BigDecimal;

public class jdStraightFrame implements iStraightFrame {
	private byte[] bDataCache;
	private CsFatScale fatScale = null;

	@Override
	public enumProcessResult process(byte[] bBuffer,String uuid) throws FrameFormatIllegalException {
		enumProcessResult processResult=enumProcessResult.Wait_Scale_Data;
		
		if(bBuffer == null){
			throw new FrameFormatIllegalException("帧格式错误 -- 帧为空");			
		}

		if(bBuffer.length!=20){
			throw new FrameFormatIllegalException("帧格式错误 -- 包长度不对");	
		}
		
		byte packageIndex=bBuffer[0]; //包流水号		
		if(packageIndex==0x00){
			byte cmdId=bBuffer[1];
			//byte dataLength=bBuffer[2];
			
			if(cmdId==0x04){
				//透传数据
				bDataCache=new byte[28];
				System.arraycopy(bBuffer, 3, bDataCache, 0, 17);
				processResult=enumProcessResult.Wait_Scale_Data;
			}else if(cmdId==(byte)0xF1){
				processResult=enumProcessResult.Sync_JDRole_Resp;
			}
		}else if(packageIndex==0x01){
			//第二包
			if(bDataCache==null) {return enumProcessResult.Wait_Scale_Data;}
			
			System.arraycopy(bBuffer, 1, bDataCache, 17, 11);
			fatScale = new CsFatScale();
			fatScale.setHistoryData(false);
			
			int iIndex=6;
			byte[] bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			fatScale.setWeight(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//身体质量指数
			bTmp=BytesUtil.subBytes(bDataCache,
					iIndex, 2);
			iIndex+=2;
			
			//体脂率
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			fatScale.setAxunge(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//皮下脂肪率
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			//fatScale.setAxunge(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//内脏脂肪指数
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			fatScale.setVisceral_fat(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//肌肉率
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			fatScale.setMuscle(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//基础代谢率
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			fatScale.setBmr(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//骨骼重量
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			fatScale.setBone(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//水含量
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			fatScale.setWater(BytesUtil.bytesToInt(bTmp));
			iIndex+=2;
			
			//身体年龄
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 1);
			iIndex+=1;
			
			//（蛋白）率
			bTmp=BytesUtil.subBytes(bDataCache,					
					iIndex, 2);
			iIndex+=2;
			fatScale.setLockFlag((byte)1);
			fatScale.setScaleProperty((byte)1);
			BigDecimal bigConvert=new BigDecimal(fatScale.getWeight());
			fatScale.setScaleWeight("" + bigConvert.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
			processResult=enumProcessResult.Received_Scale_Data;
		}
		
		return processResult;
	}

	@Override
	public CsFatScale getFatScale() {
		return fatScale;
	}
}
