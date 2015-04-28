package com.weigo.sales.activity.image;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.weigo.sales.R;
import com.weigo.sales.activity.base.BaseActivity;


/** 
 * @Description: 
 * @author yingjie.lin 
 * @date 2014年12月11日 上午10:36:24 
 * @copyright TCL-MIE
 */

public class PictureViewActivity extends BaseActivity {
	// 屏幕宽度
	public static int screenWidth;
	// 屏幕高度
	public static int screenHeight;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.picture_view_activity);
		initViews();
	}

	private void initViews() {

		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight();

	}
	 
	
}
