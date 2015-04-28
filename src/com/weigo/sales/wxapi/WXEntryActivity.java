package com.weigo.sales.wxapi;

import android.os.Bundle;
import android.widget.Toast;

import com.app.framework.log.NLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.weigo.base.http.JsonLoader;
import com.weigo.sales.AppConfig;
import com.weigo.sales.activity.base.BaseActivity;
import com.weigo.sales.login.WXGetTokenProvider;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final String TAG = WXEntryActivity.class.getSimpleName();
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	private WXGetTokenProvider mJsonProvider;
	private JsonLoader mJsonLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		NLog.e(TAG, "onCreate");
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, false);

		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq arg0) {
		NLog.e(TAG, "onReq");
	}

	@Override
	public void onResp(BaseResp resp) {
		Bundle bundle = new Bundle();
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			resp.toBundle(bundle);
			Resp sp = new Resp(bundle);
			String code = sp.code;
			NLog.e(TAG, "code:%s", code);
			mJsonProvider = new WXGetTokenProvider(code);
			mJsonLoader = new JsonLoader(mJsonProvider);
			mJsonLoader.load();
			// 上面的code就是接入指南里要拿到的code
			// https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
			// 分享成功
			// Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();

			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			// 分享取消
			Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			// 分享拒绝
			Toast.makeText(this, "拒绝分享" + "Error Message: " + resp.errStr, Toast.LENGTH_LONG).show();
			break;
		}
		finish();
	}
}