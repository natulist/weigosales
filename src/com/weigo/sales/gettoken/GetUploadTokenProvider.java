package com.weigo.sales.gettoken;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.framework.log.NLog;
import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.weigo.base.http.JsonProvider;
import com.weigo.sales.Constants;


/** 
 * @Description: 
 * @author yingjie.lin 
 * @date 2014年12月22日 下午1:52:57 
 * @copyright TCL-MIE
 */

public class GetUploadTokenProvider implements JsonProvider {
	private static final String TAG = GetUploadTokenProvider.class.getSimpleName();
	String upload_token;
	@Override
	public String getURL() {
		return "http://120.24.55.91:9080/weigou/t.do?requestid=uptoken";
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
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("requestid", "uptoken");
		return paramsMap;
	}

	@Override
	public void onSuccess() {
		GetTokenState getTokenState = new GetTokenState();
		getTokenState.status = GetTokenState.GET_SUCCESS;
		getTokenState.data = upload_token;
		NotificationCenter.defaultCenter().publish(getTokenState);
	}

	@Override
	public void onCancel() {
		GetTokenState getTokenState = new GetTokenState();
		getTokenState.status = GetTokenState.GET_CANCEL;
		getTokenState.data = upload_token;
		NotificationCenter.defaultCenter().publish(getTokenState);
	}

	@Override
	public void onError(int err) {
		GetTokenState getTokenState = new GetTokenState();
		getTokenState.status = GetTokenState.GET_FAIL;
		getTokenState.data = upload_token;
		NotificationCenter.defaultCenter().publish(getTokenState);
	}

	@Override
	public int parse(JSONObject obj) {
		try {
			NLog.e(TAG, "obj = %s", obj.toString());
			int err = obj.getInt("errcode");
			if (err != Constants.net.SUCCESS){
				return NetworkError.FAIL_UNKNOWN;
			}
			String uptoken = obj.getString("uptoken");
			this.upload_token = uptoken;
			return NetworkError.SUCCESS;
		} catch (JSONException e) {
			NLog.printStackTrace(e);
			return NetworkError.FAIL_UNKNOWN;
		}
	}

}
