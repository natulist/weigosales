package com.weigo.sales.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.framework.log.NLog;
import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.weigo.base.http.JsonProvider;

/**
 * @Description:
 * @author yingjie.lin
 * @date 2014年12月9日 下午4:55:55
 * @copyright TCL-MIE
 */

public class WXGetInfoProvider implements JsonProvider {
	private static final String TAG = WXGetInfoProvider.class.getSimpleName();
	private String mCode;
	private String access_token;
	private String openid;
	private String unionid;

	public WXGetInfoProvider(String access_token, String openid) {
		this.access_token = access_token;
		this.openid = openid;
	}

	@Override
	public String getURL() {
		return "https://api.weixin.qq.com/sns/userinfo";
	}

	@Override
	public boolean supportPost() {
		return false;
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("access_token", access_token);
		paramsMap.put("openid", openid);
		return paramsMap;
	}

	@Override
	public void onSuccess() {
		LoginState loginState = new LoginState();
		loginState.status = LoginState.WX_GET_UNIONID;
		loginState.data = unionid;
		NotificationCenter.defaultCenter().publish(loginState);
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
			NLog.e(TAG, "obj = %s", obj.toString());
			String unionid = obj.getString("unionid");
			if (unionid == null)
				return NetworkError.FAIL_UNKNOWN;
			NLog.e(TAG, "unionid = %s", unionid);
			String nickname = obj.getString("nickname");
			if (nickname == null)
				return NetworkError.FAIL_UNKNOWN;
			NLog.e(TAG, "nickname = %s", nickname);
			this.unionid = unionid;
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
