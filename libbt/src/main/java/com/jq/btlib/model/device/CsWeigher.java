package com.jq.btlib.model.device;


/**
 * 蓝牙秤 -- 传输给ui命令为 0x1x 在这里规定 0x1x系列为人体秤专用
 * 		  UI获得与脂肪秤有关的信息,只需要通过CsWeigher即可
 * */
public class CsWeigher extends CsDevice {
	
	//=======================================变量===========================================
	/**重量**/
	private double weight;
	/**当前重量单位**/
	private double weightUnit;
	
	
	//=======================================函数===========================================
	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight/10;
	}

	@Override
	public String toString() {
		return "CsWeigher [weight=" + weight + ", weightUnit=" + weightUnit
				+ "]";
	}

	public double getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(double weightUnit) {
		this.weightUnit = weightUnit;
	}
	
	
}
