package com.weigo.sales.login;

import android.R.integer;


/** 
 * @Description: 
 * @author yingjie.lin 
 * @date 2014年12月10日 下午5:44:07 
 * @copyright TCL-MIE
 */

public class LoginState {
	public static final int WX_GET_UNIONID = 1;
	public static final int WX_GET_UNIONID_CANCEL = 2;
	public static final int WX_GET_UNIONID_FAIL = 3;
	public static final int LOGIN_SUCCESS = 4;
	public static final int LOGIN_CANCEL = 5;
	public static final int LOGIN_FAIL = 6;
	public int status;
	public String data;

}
