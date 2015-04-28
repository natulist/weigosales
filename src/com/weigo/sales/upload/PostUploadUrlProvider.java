package com.weigo.sales.upload;

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
import com.weigo.sales.gettoken.GetTokenState;


/** 
 * @Description: 
 * @author yingjie.lin 
 * @date 2014年12月23日 下午2:35:41 
 * @copyright TCL-MIE
 */

public class PostUploadUrlProvider implements JsonProvider {

	private static final String TAG = PostUploadUrlProvider.class.getSimpleName();
	String mJsonString;
	public PostUploadUrlProvider(String json){
		mJsonString = json;
	}
	
	@Override
	public String getURL() {
		return UrlConfig.getShareListURL();
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
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("version", AppConfig.PROTOCOL_VERSION);
		paramsMap.put("act", "supplier_update_goods");
		paramsMap.put("access_token", PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.loginState, null));
		paramsMap.put("user_type", PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.user_type, null));
		paramsMap.put("json", mJsonString);
		paramsMap.put("uid", PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.user_id, null));
		return paramsMap;
	}

	@Override
	public void onSuccess() {
		GetTokenState uploadState = new GetTokenState();
		uploadState.status = GetTokenState.UPLOAD_SUCCESS;
		uploadState.data = null;
		NotificationCenter.defaultCenter().publish(uploadState);
	}

	@Override
	public void onCancel() {
		GetTokenState uploadState = new GetTokenState();
		uploadState.status = GetTokenState.UPLOAD_CANCEL;
		uploadState.data = null;
		NotificationCenter.defaultCenter().publish(uploadState);
	}

	@Override
	public void onError(int err) {
		GetTokenState uploadState = new GetTokenState();
		uploadState.status = GetTokenState.UPLOAD_FAIL;
		uploadState.data = null;
		NotificationCenter.defaultCenter().publish(uploadState);
	}

	@Override
	public int parse(JSONObject obj) {
		try {
			NLog.e(TAG, "obj = %s", obj.toString());
			int err = obj.getInt("errcode");
			if (err != Constants.net.SUCCESS){
				return NetworkError.FAIL_UNKNOWN;
			}
			return NetworkError.SUCCESS;
		} catch (JSONException e) {
			NLog.printStackTrace(e);
			return NetworkError.FAIL_UNKNOWN;
		}
	}

}
