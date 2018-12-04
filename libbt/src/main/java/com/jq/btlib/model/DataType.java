package com.jq.btlib.model;

/**
 * 设备类型
 * */
public class DataType {
	
	/**蓝牙秤**/
	public static final byte WEIGHER = 0x0; 
	/**脂肪秤**/
	public static final byte FAT_SCALE = 0x1; 
	//后续需要扩展，可以在下面中定义 ...
	
	/**扩展**/
	public static final byte EXTEND = (byte) 0xff;
}
