package com.jq.btlib.model;

import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.util.BytesUtil;


/**
 * 帧
 * */
public class Frame {
	
	private static byte startByte = (byte) 0xCA;
	private byte frameVers;
	private byte dataAreaLen;
	private FrameData dataArea;
	private byte checkByte;
	
	private byte[] frameFull; 
	
	/**
	 * 析构函数
	 * */
	public Frame(byte frameVers,FrameData dataArea) {
		
		setFrameVers(frameVers);
		setDataAreaLen((byte)dataArea.length());
		setDatasArea(dataArea);
		
		//计算出异或值
		byte curCheckByte = frameVers ;
		curCheckByte ^= dataAreaLen;
		curCheckByte ^= dataArea.getDatasXor();
		setCheckByte(curCheckByte);
		
		
		//获得当前所有byte[]数据
		int count = 0;
		this.frameFull = new byte[length()];
		
		this.frameFull[count] = startByte;
		count++;

		this.frameFull[count] = frameVers;
		count++;
		
		this.frameFull[count] = dataAreaLen;
		count++;
		
		for(int i=0;i<dataArea.length();i++,count++){
			this.frameFull[count] = dataArea.getFull()[i];
		}
		
		this.frameFull[count] = checkByte;
		count++;
	}

	/**
	 * 析构函数 
	 * @param frameFull 名命令的字节数组
	 * */
	public Frame(byte[] frameFull) throws FrameFormatIllegalException{
		if(frameFull == null){
			throw new FrameFormatIllegalException("帧格式错误 -- 帧为空");
		}else if(frameFull[0] != (byte)0xCA){			
			throw new FrameFormatIllegalException("帧格式错误 -- 不是正确的帧头");
		}else if(frameFull[2] != (frameFull.length-4)){
			throw new FrameFormatIllegalException("帧格式错误 -- 数据长度不匹配");
		}
//		else if(frameFull.length > 20){
//			throw new FrameFormatIllegalException("帧格式错误 -- 一帧数据不能超过20byte");
//		}
		
		setFrameVers(frameFull[1]);
		setDataAreaLen(frameFull[2]);
		setCheckByte(frameFull[frameFull.length-1]);
	
	
		//设置数据域
		byte[] data = BytesUtil.subBytes(frameFull, 5, frameFull[2]-2);
		
		FrameData datas = new FrameData(frameFull[3], frameFull[4], data);
		
		setDatasArea(datas);
		
		//判断实际校验位是否正确
		byte tmp_check_byte = frameFull[1];
		tmp_check_byte 	   ^= frameFull[2];
		tmp_check_byte 	   ^= datas.getDatasXor();
		
		if(tmp_check_byte != getCheckByte()){
			throw new FrameFormatIllegalException("帧格式错误 -- 实际异或值与获得异或值不一致");
		}
		
		//获得当前所有byte[]数据		
		this.frameFull = frameFull;

	}
	
	
	// ===================================set函数===================================
	private void setFrameVers(byte frameVers) {
		this.frameVers = frameVers;
	}

	private void setDataAreaLen(byte dataAreaLen) {
		this.dataAreaLen = dataAreaLen;
	}

	private void setDatasArea(FrameData dataArea) {
		this.dataArea = dataArea;
	}

	private void setCheckByte(byte checkByte) {
		this.checkByte = checkByte;
	}

	// ===================================get函数===================================
	public byte getFrameVers() {
		return this.frameVers;
	}

	public byte getDataAreaLen() {
		return this.dataAreaLen;
	}

	public FrameData getDatasArea() {
		return this.dataArea;
	}

	public byte getCheckByte() {
		return this.checkByte;
	}
	
	public byte[] getFrameFull(){
		return this.frameFull;
	}
	// ===================================other函数===================================
	/**
	 * 数据帧总大小
	 * */
	public int length(){
		return this.frameFull.length;
	}

}
