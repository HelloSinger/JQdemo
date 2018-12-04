package com.jq.btlib.model.device;

import java.util.Date;

/**
 * 蓝牙脂肪秤 -- 传输给ui命令为 0x2x 在这里规定  0x2x 系列为人体秤专用
 * 			 UI获得与脂肪秤有关的信息,只需要通过CsFatScales即可
 * */
public class CsFatScale extends CsDevice  {

	/**公司id**/
	//private byte[] firmId;
	/**重量**/
	private double weight;
	/**体脂率**/
	private double axunge;
	/**水分**/
	private double water;
	/**肌肉**/
	private double muscle;
	/**BMR**/
	private double bmr;
	/**内脏脂肪**/
	private double visceral_fat;
	/**骨头重量**/
	private double bone;
	/**角色Id(仅对历史数据有效)**/
	private int roleId;
	
	/**称重时间(仅对历史数据有效)**/
	private Date weighingDate;

	/**蓝牙秤显示的体重数值**/
	private String scaleweight;
	/**当前重量属性**/
	private byte scaleproperty;

	public String getScaleWeight() {
		return scaleweight;
	}

	public void setScaleWeight(String scaleWeight) {
		this.scaleweight = scaleWeight;
	}

	public byte getScaleProperty() {
		return scaleproperty;
	}

	public void setScaleProperty(byte scaleProperty) {
		this.scaleproperty = scaleProperty;
	}

	public double ori_visceral_fat;
	
	
	public Date getWeighingDate() {
		return weighingDate;
	}
	public void setWeighingDate(Date weighingDate) {
		this.weighingDate = weighingDate;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	/**是否是历史数据 **/
	private boolean isHistoryData;
	public boolean isHistoryData() {
		return isHistoryData;
	}
	public void setHistoryData(boolean isHistoryData) {
		this.isHistoryData = isHistoryData;
	}
	
	/**锁定标识**/
	private byte lockFlag;
	
	public byte getLockFlag() {
		return lockFlag;
	}
	public void setLockFlag(byte lockFlag) {
		this.lockFlag = lockFlag;
	}

	/**电阻值**/
	private float impedance;
	public float getImpedance() {
		return impedance;
	}

	public void setImpedance(float impedance) {
		this.impedance = impedance;
	}

	/**其它测量信息
	 * 格式定义 type1:value | type2:value
	 * **/
	private String remark;
	public String getRemark(){return remark;}
	public void setRemark(String remark){
		this.remark=remark;
	}


	/**设备类型 0--脂肪秤  1---人体秤**/
	private int deviceType;

	public int getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}

	/**身体年龄**/
	private double age;
	/****/
//=======================================函数===========================================
//	public byte[] getFirmId() {
//		return firmId;
//	}
//	public void setFirmId(byte[] firmId) {
//		this.firmId = firmId;
//	}
	public double getWeight() {
		return weight;
	}
	public double getAxunge() {
		return axunge;
	}

	public double getWater() {
		return water;
	}

	public double getMuscle() {
		return muscle;
	}

	public double getBmr() {
		return bmr;
	}

	public double getVisceral_fat() {
		return visceral_fat;
	}

	public double getBone() {
		return bone;
	}
	public double getAge() {
		return age;
	}
	
	public void setWeight(double weight) {
		this.weight = weight/10;
	}
	public void setAxunge(double axunge) {
		if(axunge>=65535){
			this.axunge=0;			
		}else{
			double tmp=axunge/10;
			if(tmp<5.0f){
				tmp=0;
			}else if(tmp>45.0f){
				tmp=0;
			}
			this.axunge = tmp;
		}

	}
	public void setWater(double water) {
		double percent=water/10;
		if(percent>=100.0f){
			percent=0;
		}else if(percent<20.0f){
			percent=0;
		}else if(percent>85.0f){
			percent=0;
		}

		this.water = percent;
	}
	public void setMuscle(double muscle) {
		double percent=muscle/10;
		if(percent>=100){
			percent=0;
		}
		this.muscle = percent;
	}
	
	public void setBmr(double bmr) {
		if(bmr>=65535){
			this.bmr=0;
		}else{
			double dTmp=bmr;			
			if(bmr>=5000){
				dTmp=bmr/10;
			}
			this.bmr = dTmp;
		}
		
	}
	public void setVisceral_fat(double visceral_fat) {
		ori_visceral_fat=visceral_fat;
		double percent=visceral_fat/10;
		if(percent>=100.0f){
			percent=0;
		}else if(percent>59.0f){
			percent=0;
		}
		this.visceral_fat = percent;
	}
	public void setBone(double bone) {
		if(bone>=65535){
			this.bone=0;
		}else{
			double tmp=bone /10;
			if(tmp<1.0f){
				tmp=0;
			}else if(tmp>4.0f){
				tmp=4;
			}
			this.bone = tmp;
		}
		
	}
	public void setAge(double age) {
		this.age = age;
	}
	
	public void cleanFatInfo(){
		this.axunge=0;
		this.water=0;
		this.muscle=0;
		this.bmr=0;
		this.visceral_fat=0;
		this.bone=0;
	}

	@Override
	public String toString() {
		return "CsFatScale{" +
				"weight=" + weight +
				", axunge=" + axunge +
				", water=" + water +
				", muscle=" + muscle +
				", bmr=" + bmr +
				", visceral_fat=" + visceral_fat +
				", bone=" + bone +
				", roleId=" + roleId +
				", weighingDate=" + weighingDate +
				", scaleweight='" + scaleweight + '\'' +
				", scaleproperty=" + scaleproperty +
				", ori_visceral_fat=" + ori_visceral_fat +
				", isHistoryData=" + isHistoryData +
				", lockFlag=" + lockFlag +
				", impedance=" + impedance +
				", age=" + age +
				"} " + super.toString();
	}

}
