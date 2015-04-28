package com.weigo.sales.gettoken;




/** 
 * @Description: 
 * @author yingjie.lin 
 * @date 2014年12月10日 下午5:44:07 
 * @copyright TCL-MIE
 */

public class GetTokenState {
	public static final int GET_SUCCESS = 1;
	public static final int GET_CANCEL = 2;
	public static final int GET_FAIL = 3;
	public static final int GET_JSON = 4;
	
	public static final int UPLOAD_SUCCESS = 5;
	public static final int UPLOAD_CANCEL = 6;
	public static final int UPLOAD_FAIL = 7;
	public int status;
	public String data;

}
