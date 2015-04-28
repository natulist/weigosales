package com.weigo.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/** 
 * @Description: 字符串辅助类
 * @author wenbiao.xie 
 * @date 2014年9月30日 下午5:54:03 
 * @copyright TCL-MIE
 */

public class StringUtil {

	/**
	 * 判断是否有效的邮箱地址
	 * @param s 邮箱地址
	 * @return 返回判断结果
	 */
	public static boolean isValidEmail(String s) {
		
		if (TextUtils.isEmpty(s)) {
			return false;
		}

		String strPattern = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	/**
	 * 判断是否有效的手机号
	 * @param s 手机号码
	 * @return 返回判断结果
	 */
	public static boolean isValidMobilePhone(String s) {
		if (TextUtils.isEmpty(s)) {
			return false;
		}

		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(s);
		return matcher.matches();
	}
	/**
	 * 判断字符串是否为数字
	 * @param str 字符串
	 * @return 返回判断结果
	 */
	public static boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   return isNum.matches() ; 
	}
	
	/**
	 * 字符串串联
	 * @param sb
	 * @param format
	 * @param args
	 * @return
	 */
	public static StringBuffer appendFormat(StringBuffer sb, String format, Object...args) {
		if (TextUtils.isEmpty(format)) 
			throw new IllegalArgumentException("format params invalid");
		
		if (sb == null)
			sb = new StringBuffer();
		
		String s = String.format(format, args);
		sb.append(s);
		
		return sb;
	}

	
}
