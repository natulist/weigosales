package com.weigo.sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.app.framework.notification.NotificationCenter;
import com.app.framework.notification.Subscriber;
import com.app.framework.util.PrefsUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.weigo.base.http.JsonLoader;
import com.weigo.sales.Constants;
import com.weigo.sales.R;
import com.weigo.sales.SHContext;
import com.weigo.sales.SalesApplication;
import com.weigo.sales.activity.base.BaseActivity;
import com.weigo.sales.login.LoginProvider;
import com.weigo.sales.login.LoginState;

/**
 * @Description:
 * @author yingjie.lin
 */

public class LoginActivity extends BaseActivity implements OnClickListener {

	public static final int USER_ID = 1;
	public static final int SELL_ID = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById(R.id.wxlogin).setOnClickListener(this);
		NotificationCenter.defaultCenter().subscriber(LoginState.class, loginCallbackSubscriber);
	}

	private Subscriber<LoginState> loginCallbackSubscriber = new Subscriber<LoginState>() {

		@Override
		public void onEvent(LoginState event) {
			switch (event.status) {
			case LoginState.WX_GET_UNIONID:
				// TODO:登录处理
				Toast.makeText(SHContext.getInstance().getApplicationContext(), "登录中", Toast.LENGTH_SHORT).show();
				// 登录
//				LoginProvider mJsonProvider = new LoginProvider(event.data);
//				JsonLoader mJsonLoader = new JsonLoader(mJsonProvider);
//				mJsonLoader.load();
				localLogin(event.data, SELL_ID);
				break;
			case LoginState.WX_GET_UNIONID_CANCEL:
				break;
			case LoginState.WX_GET_UNIONID_FAIL:
				break;

			case LoginState.LOGIN_SUCCESS:
				PrefsUtils.savePrefString(SHContext.getInstance().getApplicationContext(), Constants.loginState, event.data);
				Intent intent = new Intent(Constants.intent.MAIN);
				startActivity(intent);
				break;
			case LoginState.LOGIN_CANCEL:
				break;
			case LoginState.LOGIN_FAIL:
				Toast.makeText(SHContext.getInstance().getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};

	protected void onDestroy() {
		NotificationCenter.defaultCenter().unsubscribe(LoginState.class, loginCallbackSubscriber);
		super.onDestroy();
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wxlogin:
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo";
			SalesApplication.WXapi.sendReq(req);
			break;
		default:
			break;
		}
	}
	
	private void localLogin(String id, int type){
		LoginProvider mJsonProvider = new LoginProvider(id, type);
		JsonLoader mJsonLoader = new JsonLoader(mJsonProvider);
		mJsonLoader.load();
	}
	
	

}
