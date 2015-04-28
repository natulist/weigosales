package com.weigo.sales.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.framework.log.NLog;
import com.app.framework.util.PrefsUtils;
import com.weigo.sales.Constants;
import com.weigo.sales.R;
import com.weigo.sales.SHContext;
import com.weigo.sales.activity.LoginActivity;
import com.weigo.sales.activity.base.BaseTitleActivity;

public class MainTabActivity extends BaseTitleActivity {
	private final static String TAG = "MainTabActivity";
	static final int TAB_RECOMMEND_INDEX = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		NLog.v(TAG, "onCreate this = %s", this.toString());
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		if (intent != null && intent.getBooleanExtra("goto_update", false)) {
			should_turn_to_update = true;
		}
	}

	int currentTabIndex = -1;
	ViewPager mViewPager;
	private MainTabPagerAdapter mAdapter;

	@Override
	protected void onContentCreate(Bundle savedInstanceState, View content) {
		initViewPager(content);

	}

	private boolean should_turn_to_update = false;

	@Override
	protected void onNewIntent(Intent intent) {
		NLog.v(TAG, "onNewIntent this = %s", this.toString());
		super.onNewIntent(intent);

		// 通知进入more
		if (intent != null && intent.getBooleanExtra("goto_update", false)) {
			should_turn_to_update = true;
		}
	}

	@Override
	protected void onResume() {
		NLog.v(TAG, "onResume this = %s", this.toString());
		super.onResume();
		mAdapter.onResume(mViewPager.getCurrentItem());

//		if (should_turn_to_update) {
//			NLog.i(TAG, "should_turn_to_update");
//			should_turn_to_update = false;
//
//			mHandler.postDelayed(new Runnable() {
//
//				@Override
//				public void run() {
//					if (mViewPager.getCurrentItem() != TAB_MORE_INDEX) {
//						mAdapter.onPause(mViewPager.getCurrentItem());
//						mViewPager.setCurrentItem(TAB_MORE_INDEX, false);
//					}
//
//					startActivity(new Intent(Constant.intent.APPS_UPDATE));
//				}
//			}, 200);
//
//		}

	}


	@Override
	protected void onPause() {
		NLog.v("MainTabActivity", "onPause this = %s", this.toString());
		super.onPause();
		mAdapter.onPause(mViewPager.getCurrentItem());
	}

	View.OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.isSelected()) {
				return;
			}
			int position = (Integer) v.getTag();
			mViewPager.setCurrentItem(position);
		}
	};


	private void initViewPager(View parent) {
		mViewPager = (ViewPager) parent.findViewById(R.id.main_pager);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		mAdapter = new MainTabPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);

		mViewPager.setCurrentItem(TAB_RECOMMEND_INDEX);
	}



	@Override
	protected int getContentLayout() {
		return R.layout.activity_main;
	}

	// protected int getRightLayout() {
	// return R.layout.btn_search;
	// }

	@Override
	protected void initTitle() {
		setTitleText(R.string.app_name);
		enableBack(true);
		enableMenu(R.drawable.logout,new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO:logout
				PrefsUtils.savePrefString(SHContext.getInstance().getApplicationContext(), Constants.loginState, null);
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setClass(MainTabActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	int nPressBack = 0;

	private boolean exit() {
		return true;
//		nPressBack++;
//		if (nPressBack >= 2) {
//			UIUtil.hideToast();
//			mHandler.removeMessages(MSG_PREPARE_EXIT);
//			ContextUtils.exit_to_desktop = true;
//			return true;
//		} else {
//			UIUtil.showToast(this, R.string.press_again_exit, false);
//			mHandler.sendEmptyMessageDelayed(MSG_PREPARE_EXIT, 3000);
//			return false;
//		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (exit()) {
				return super.onKeyDown(keyCode, event);
			}
		}
		return false;
	}

	

//	@Override
//	protected void initTitle() {
//		setTitleText(R.string.onekey_help);
//		enableBack(true);
//		enableMenu(null);
//		setBgColor(Color.rgb(255, 128, 26));
//	}

}
