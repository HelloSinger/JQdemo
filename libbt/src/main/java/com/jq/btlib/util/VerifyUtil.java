package com.jq.btlib.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyUtil {

	/** 
	 * 判断手机格式是否正确
	 **/
	public static boolean isMobileNO(String mobiles) {
		
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 判断email格式是否正确
	 * */
	public static boolean isEmail(String email) {
		
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判断是否全是数字
	 * */
	public static boolean isNumeric(String str) {
		
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
	
	/** 
	 * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0 
	 * @param version1 
	 * @param version2 
	 * @return 
	 */  
	public static int compareVersion(String version1, String version2) {  
	    String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；  
	    String[] versionArray2 = version2.split("\\.");  
	    int idx = 0;  
	    int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值  
	    int diff = 0;  
	    while (idx < minLength  
	            && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度  
	            && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符  
	        ++idx;  
	    }  
	    //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；  
	    diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;  
	    return diff;  
	}  

}
