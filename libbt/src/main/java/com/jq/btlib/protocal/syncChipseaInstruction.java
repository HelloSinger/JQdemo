package com.jq.btlib.protocal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.jq.btlib.util.BytesUtil;

public class syncChipseaInstruction {
	
	private static int getAgeThroughBirthday(String birthday) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String strCurDate = formatter.format(curDate);
		
		int year = Integer.valueOf(birthday.substring(0, 4));
		int nowYear = Integer.valueOf(strCurDate.substring(0, 4));
		return nowYear - year;
	}
	
	/**
     * @Description 合并性别和年龄到一个字节
     * @param sex
     * 性别
     * @param age
     * 年龄
     * @return 性别和年龄，最高位为1代表男，0代表女
     */
	private static byte mergeSexAndAge(byte sex,byte age){
		byte bret=0;
		if(sex==1){
			bret=(byte)(128 | age);
		}else{
			bret=(byte)(127 & age);
		}
		
		return bret;
	}
	
	
	/**
     * @Description 生成分包信息
     * @param bCurPackage
     * 当前包序号
     * @param bPackageTotal
     * 总包数
     * @return 分包信息(高四位代表当前包数，低四位代表总包数)
     */
	private static byte mergePackageField(byte bCurPackage,byte bPackageTotal){
		byte bHigh=(byte) (bCurPackage<<4);
		byte bRet=(byte) (bHigh | bPackageTotal);
		return bRet;
	}
	
	
	/**
     * @Description 生成v1.1协议同步历史数据指令
     * @return 指令数组
     */
	public static byte[] sycnRetrieveHistoryData_v11(){
		byte[] bRet=new byte[20];
		bRet[0]=(byte) 0xca; //帧夈
		bRet[1]=(byte) 0x11; //版本号
		bRet[2]=(byte) 0x02; //数据域长度
		bRet[3]=(byte) 0x11; //命令字
		bRet[4]=(byte) 0x01; //设备类型		
		bRet[5]=BytesUtil.getDatasXor(bRet, 1, 4); //校验
		for(int i=6;i<20;i++){
			bRet[i]=0;
		}
		return bRet;
	}

	/**
	 * @Description 生成v1.10 x15 多用户匹配确认应答指令
	 * @param userId
	 * 所有角色列表
	 * @return 指令数组
	 */
	public static byte[] syncSelectedUserId(int userId){
		byte[] bRet=new byte[9];
		bRet[0]=(byte) 0xca; //帧夈
		bRet[1]=(byte) 0x11; //版本号
		bRet[2]=(byte) 0x05; //数据域长度
		bRet[3]=(byte) 0x15; //命令字
		BytesUtil.putInt(bRet,userId,4);
		bRet[8]=BytesUtil.getDatasXor(bRet, 1, 7); //校验
		return bRet;
	}

	
	/**
     * @Description 生成v1.1协议同步所有角色用户指令
     * @param lstRole 
     * 所有角色列表
     * @return 指令数组
     */
	public static ArrayList<byte[]> sycnAllRoleInfo_v11(ArrayList<RoleInfo> lstRole){
		ArrayList<byte[]> lstRet=new ArrayList<byte[]>();
		
//		SimpleDateFormat myFormatter = new SimpleDateFormat("yy:MM:dd:HH:mm:ss",
//				Locale.getDefault());
//		Date date = new Date(System.currentTimeMillis());		
//		String currentTime = myFormatter.format(date);
//		String[] dates = currentTime.split(":");
		int currentTime=(int)(System.currentTimeMillis() /1000);
		
		if(lstRole.size()==1){
			RoleInfo role=lstRole.get(0);			
			byte[] bRet=new byte[20];
			
			bRet[0]=(byte) 0xca; //帧夈
			bRet[1]=(byte) 0x11; //版本号
			bRet[2]=(byte) 0x10; //数据域长度
			bRet[3]=(byte) 0x10; //命令字
			bRet[4]=mergePackageField((byte)0x01,(byte)0x01); //分包信息 
			
//			bRet[5]=(byte) (Integer.parseInt(dates[0]) & 0xff);
//			bRet[6]=(byte) (Integer.parseInt(dates[1]) & 0xff);
//			bRet[7]=(byte) (Integer.parseInt(dates[2]) & 0xff);
//			bRet[8]=(byte) (Integer.parseInt(dates[3]) & 0xff);
//			bRet[9]=(byte) (Integer.parseInt(dates[4]) & 0xff);
//			bRet[10]=(byte) (Integer.parseInt(dates[5]) & 0xff);
			
			BytesUtil.putInt(bRet, currentTime, 5);
			bRet[9]=0;
			bRet[10]=0;
			
			BytesUtil.putInt(bRet, role.roleId,11);
			bRet[15]= mergeSexAndAge(role.sex,role.age);
			bRet[16]= role.height;
			BytesUtil.putShort(bRet, role.weight, 17);
			bRet[19]=BytesUtil.getDatasXor(bRet, 1, 18);
			lstRet.add(bRet);
		}else{
			//计算总字节数  总角色 * 8 + 同步时间  
			int total=lstRole.size() * 8+ 6;
			byte[] bTotal=new byte[total];
//			bTotal[0]=(byte) (Integer.parseInt(dates[0]) & 0xff);
//			bTotal[1]=(byte) (Integer.parseInt(dates[1]) & 0xff);
//			bTotal[2]=(byte) (Integer.parseInt(dates[2]) & 0xff);
//			bTotal[3]=(byte) (Integer.parseInt(dates[3]) & 0xff);
//			bTotal[4]=(byte) (Integer.parseInt(dates[4]) & 0xff);
//			bTotal[5]=(byte) (Integer.parseInt(dates[5]) & 0xff);
			BytesUtil.putInt(bTotal, currentTime, 0);
			bTotal[4]=0;
			bTotal[5]=0;
			int iIndex=6;
			for(RoleInfo rInfo:lstRole){				
				BytesUtil.putInt(bTotal, rInfo.roleId,iIndex);
				iIndex+=4;
				bTotal[iIndex]= mergeSexAndAge(rInfo.sex,rInfo.age);
				iIndex+=1;
				bTotal[iIndex]= rInfo.height;
				iIndex+=1;
				BytesUtil.putShort(bTotal, rInfo.weight, iIndex);
				iIndex+=2;
			}
			
			//**************************************************************
			int iLenPerPackage=14;
			
			int iPageCount=total/iLenPerPackage;
	        if (total % iLenPerPackage>0) {
	            iPageCount++;
	        }
	        for(int i=0;i<iPageCount;i++){
				int icurLen=iLenPerPackage;
	        	if(i==iPageCount-1){
					//最后一页
	        		icurLen=total % iLenPerPackage;
	        		if(icurLen==0){
	        			icurLen=iLenPerPackage;
	        		}
				}
	        	
	        	byte[] bRet=new byte[icurLen+6];
				bRet[0]=(byte) 0xca; //帧夈
				bRet[1]=(byte) 0x11; //版本号
				bRet[2]=(byte)(icurLen+2); //数据域长度
				bRet[3]=(byte) (0x10); //命令字
				bRet[4]= mergePackageField((byte)(i+1),(byte)(iPageCount)); //分包信息
				int srcPos=i*14;
				int istartPos=5;
				System.arraycopy(bTotal, srcPos, bRet, istartPos, icurLen);
				istartPos+=icurLen;
				bRet[istartPos]=BytesUtil.getDatasXor(bRet, 1, istartPos-1);
				lstRet.add(bRet);
	        }
		}
		return lstRet;
		
	}
	
	
	/**
     * @Description 生成v1.0协议同步当前色用户指令
     * @param roleId 
     * 角色Id
     * @param bsex 
     * 性别
     * @param bheight 
     * 身高
     * @param sbirthday 
     * 生日
     * @return 指令数组
     */
	public static byte[] syncRoleInfo_v10(int roleId,byte bsex,byte bheight,String sbirthday){
		
		byte[] bRet=new byte[20];
		
		SimpleDateFormat myFormatter = new SimpleDateFormat("yy:MM:dd:HH:mm:ss",
				Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());		
		String currentTime = myFormatter.format(date);
		String[] dates = currentTime.split(":");
		
		bRet[0]=(byte) 0xca; //帧夈
		bRet[1]=(byte) 0x10; //版本号
		bRet[2]=(byte) 0x0e; //数据域长度
		bRet[3]=(byte) 0x01; //命令字
		bRet[4]=(byte) (Integer.parseInt(dates[0]) & 0xff);
		bRet[5]=(byte) (Integer.parseInt(dates[1]) & 0xff);
		bRet[6]=(byte) (Integer.parseInt(dates[2]) & 0xff);
		bRet[7]=(byte) (Integer.parseInt(dates[3]) & 0xff);
		bRet[8]=(byte) (Integer.parseInt(dates[4]) & 0xff);
		bRet[9]=(byte) (Integer.parseInt(dates[5]) & 0xff);
		BytesUtil.putInt(bRet, roleId,10);
		bRet[14]=(byte) (bsex & 0xff);
		bRet[15]=(byte) (getAgeThroughBirthday(sbirthday) & 0xff);
		bRet[16]=(byte) (bheight & 0xff);
		bRet[17]=BytesUtil.getDatasXor(bRet, 1, 16);
		bRet[18]=0;
		bRet[19]=0;
		
		return bRet;
	} 
}
