package com.weigo.sales.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.framework.log.NLog;
import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.app.framework.util.PrefsUtils;
import com.weigo.base.http.JsonProvider;
import com.weigo.sales.AppConfig;
import com.weigo.sales.Constants;
import com.weigo.sales.SHContext;
import com.weigo.sales.UrlConfig;

/**
 * @Description:
 * @author yingjie.lin
 * @date 2014年12月10日 下午1:38:27
 * @copyright TCL-MIE
 */

public class LoginProvider implements JsonProvider {
	private static final String TAG = LoginProvider.class.getSimpleName();

	private String userID;
	
	private String access_token;
	private int type;

	public LoginProvider(String id, int type) {
		userID = id;
		this.type = type;
	}

	@Override
	public String getURL() {
		return UrlConfig.getLoginUrl();
	}

	@Override
	public boolean supportPost() {
		return true;
	}

	@Override
	public String getHttpsCer() {
		return null;
	}

	@Override
	public Map<String, String> getParams() {
		// TODO:
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("version", AppConfig.PROTOCOL_VERSION);
		paramsMap.put("act", "dis_login");
		paramsMap.put("uid", userID);
		paramsMap.put("user_type", type+"");
		return paramsMap;
	}

	@Override
	public void onSuccess() {
		PrefsUtils.savePrefString(SHContext.getInstance().getApplicationContext(), Constants.user_type, type+"");
		PrefsUtils.savePrefString(SHContext.getInstance().getApplicationContext(), Constants.user_id, userID);
		
		LoginState loginState = new LoginState();
		loginState.status = LoginState.LOGIN_SUCCESS;
		loginState.data = access_token;
		NotificationCenter.defaultCenter().publish(loginState);
	}

	@Override
	public void onCancel() {
		LoginState loginState = new LoginState();
		loginState.status = LoginState.LOGIN_CANCEL;
		loginState.data = null;
		NotificationCenter.defaultCenter().publish(loginState);
	}

	@Override
	public void onError(int err) {
		LoginState loginState = new LoginState();
		loginState.status = LoginState.LOGIN_FAIL;
		loginState.data = null;
		NotificationCenter.defaultCenter().publish(loginState);
	}

	@Override
	public int parse(JSONObject obj) {
		try {
			NLog.e(TAG, "obj = %s", obj.toString());
			int err = obj.getInt("errcode");
			if (err != Constants.net.SUCCESS){
				return NetworkError.FAIL_UNKNOWN;
			}
			String errmsg = obj.getString("errmsg");
			String access_token = obj.getString("uid");
			this.access_token = access_token;
			// String unionid = obj.getString("errcode");
			// if (unionid == null)
			// return NetworkError.FAIL_UNKNOWN;
			return NetworkError.SUCCESS;
		} catch (JSONException e) {
			NLog.printStackTrace(e);
			return NetworkError.FAIL_UNKNOWN;
		}
	}

}
