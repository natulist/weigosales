package com.weigo.sales.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.framework.log.NLog;
import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.weigo.base.http.JsonLoader;
import com.weigo.base.http.JsonProvider;
import com.weigo.sales.AppConfig;


/** 
 * @Description: 
 * @author yingjie.lin 
 * @date 2014年12月9日 下午4:55:55 
 * @copyright TCL-MIE
 */

public class WXGetTokenProvider implements JsonProvider {
	private static final String TAG = WXGetTokenProvider.class.getSimpleName();
	private String mCode;
	private String access_token;
	private String openid;
	public WXGetTokenProvider(String code){
		mCode = code;
	}
	@Override
	public String getURL() {
		return "https://api.weixin.qq.com/sns/oauth2/access_token";
	}

	@Override
	public boolean supportPost() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("appid", AppConfig.WX_APP_ID);
		paramsMap.put("secret", AppConfig.WX_APP_SECRET);
		paramsMap.put("code", mCode);
		paramsMap.put("grant_type", "authorization_code");
		return paramsMap;
	}

	@Override
	public void onSuccess() {
		WXGetInfoProvider mJsonProvider = new WXGetInfoProvider(access_token, openid);
		JsonLoader mJsonLoader = new JsonLoader(mJsonProvider);
		mJsonLoader.load();
	}

	@Override
	public void onCancel() {
		LoginState loginState = new LoginState();
		loginState.status = LoginState.WX_GET_UNIONID_CANCEL;
		loginState.data = null;
		NotificationCenter.defaultCenter().publish(loginState);
	}

	@Override
	public void onError(int err) {
		LoginState loginState = new LoginState();
		loginState.status = LoginState.WX_GET_UNIONID_FAIL;
		loginState.data = null;
		NotificationCenter.defaultCenter().publish(loginState);
	}

	@Override
	public int parse(JSONObject obj) {
		try {
			String openid = obj.getString("openid");
			if (openid == null)
				return NetworkError.FAIL_UNKNOWN;
			NLog.e(TAG, "openid = %s", openid);
			String access_token = obj.getString("access_token");
			if (access_token == null)
				return NetworkError.FAIL_UNKNOWN;
			NLog.e(TAG, "access_token = %s", access_token);
			this.openid = openid;
			this.access_token = access_token;
			return NetworkError.SUCCESS;
		} catch (JSONException e) {
			NLog.printStackTrace(e);
			return NetworkError.FAIL_UNKNOWN;
		}
				
	}
	@Override
	public String getHttpsCer() {
		return null;
	}

}
