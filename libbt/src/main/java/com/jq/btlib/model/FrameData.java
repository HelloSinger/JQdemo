package com.jq.btlib.model;

/**
 * 帧数据域
 * */
public class FrameData {

	private byte deviceType;
	private byte insCmd;
	private byte[] data;
	
	/**完整的一个数据域字节组**/
	private byte[] full;

	/**
	 * 析构函数
	 * */
	public FrameData(byte deviceType, byte insCmd,byte[] data) {

		setDeviceType(deviceType);
		setInsCmd(insCmd);
		setDatas(data);
		
		//获得总数据域正文byte
		int count = 0;
		this.full = new byte[length()];
		
		
		full[count] = deviceType;
		count++;
		
		full[count] = insCmd;
		count++;
		
		for(int i=0; i< data.length;i++,count++){
			full[count] = data[i];
		}
	}

	// ===================================set函数===================================
	private void setDeviceType(byte deviceType) {
		this.deviceType = deviceType;
	}

	private void setInsCmd(byte insCmd) {
		this.insCmd = insCmd;
	}

	private void setDatas(byte[] data) {
		this.data = data;
	}
	// ===================================get函数===================================
	public byte getDeviceType() {
		return this.deviceType;
	}

	public byte getInsCmd() {
		return this.insCmd;
	}

	public byte[] getDatas() {
		return this.data ;
	}
	
	public byte[] getFull(){
		return this.full;
	}
	
	// ===================================other函数===================================
	/**
	 * 数据大小
	 * */
	public int length(){
		return  2 + data.length;
	}
	
	
	
	/**
	 * 当前数据域正文所有数据的异或值
	 * */
	public byte getDatasXor(){
		
		byte dataCheckByte = this.deviceType;
		dataCheckByte ^= this.insCmd;
		for(int i = 0; i < data.length; i++){
			dataCheckByte ^= data[i];
		}
		return dataCheckByte;		 				
	}
	
	

}
