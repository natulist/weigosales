package com.weigo.sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.app.framework.log.NLog;
import com.app.framework.util.PrefsUtils;
import com.weigo.sales.Constants;
import com.weigo.sales.R;
import com.weigo.sales.activity.base.BaseActivity;

/**
 * @Description: 启动activity
 */

public class LaunchActivity extends BaseActivity {

	private final static String TAG = "LaunchActivity";
	Intent mLaunchIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		findViewById(R.id.wxlogin).setVisibility(View.GONE);
		
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                gotoEntry();
            }
        }, 1000);
	}


	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mLaunchIntent = intent;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
	}

	private void gotoEntry() {
		
		if (PrefsUtils.loadPrefString(this, com.weigo.sales.Constants.loginState, null) != null) {
			gotoMain();
		} else {
			gotoLogin();
		}
		finish();
	}

	private void gotoMain() {
		Intent intent = new Intent(Constants.intent.MAIN);
		startActivity(intent);
	}

	private void gotoLogin() {
		Intent intent = new Intent(Constants.intent.LOGIN);
		startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
