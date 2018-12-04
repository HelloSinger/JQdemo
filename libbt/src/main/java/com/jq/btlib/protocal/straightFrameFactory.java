package com.jq.btlib.protocal;

import com.jq.btlib.util.CsBtUtil_v11.Protocal_Type;

public class straightFrameFactory {
	private static iStraightFrame instance=null;
	private straightFrameFactory (){}
	
	public static iStraightFrame getInstance(Protocal_Type pType) {
		switch(pType){
		case OKOK:
			if(instance==null){
				instance=new chipseaStraightFrame();
			}else{
				if(!(instance instanceof chipseaStraightFrame)){
					instance=new chipseaStraightFrame();
				}
			}
			break;
		case JD:
			if(instance==null){
				instance=new jdStraightFrame();
				
			}else{
				if(!(instance instanceof jdStraightFrame)){
					instance=new jdStraightFrame();
				}
			}
			break;
		case OKOKCloud:
			if(instance==null){
				instance=new okCloudStraightFrame();

			}else{
				if(!(instance instanceof okCloudStraightFrame)){
					instance=new okCloudStraightFrame();
				}
			}
			break;
		case OKOKCloudV3:
			if(instance==null){
				instance=new okCloudV3StraightFrame();

			}else{
				if(!(instance instanceof okCloudV3StraightFrame)){
					instance=new okCloudV3StraightFrame();
				}
			}
			break;
		case ALIBABA:
			if(instance==null){
				instance=new aliStraightFrame();

			}else{
				if(!(instance instanceof aliStraightFrame)){
					instance=new aliStraightFrame();
				}
			}
			break;
		case LEXIN:
				if(instance==null){
					instance=new lxStraightFrame();

				}else{
					if(!(instance instanceof lxStraightFrame)){
						instance=new lxStraightFrame();
					}
				}
				break;
		}
		
		return instance;  
	} 
}
