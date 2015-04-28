package com.weigo.sales;


/** 
 * @Description: 
 * @author yingjie.lin 
 */

public class Constants {
	public static final String loginState = "LOGIN_STATE"; 
	public static final String user_type = "USER_TYPE"; 
	public static final String user_id = "USER_ID"; 
	public static interface intent {
		public final static String LOGIN = "com.weigo.sales.intent.login";
		public final static String MAIN = "com.weigo.sales.intent.main";
		public final static String SHARE = "com.weigo.sales.intent.share";
		public final static String EDIT = "com.weigo.sales.intent.edit";
		public final static String VIEW_PIC = "com.weigo.sales.intent.viewpic";
	}
	
//	public static final String NOTIFY_WX_LOGIN = "wx_login"; 
	
	public static interface net {
		public final static int SUCCESS = 0;
		public final static int ACCOUNT_NOT_EXIST = 1;
	}
}
