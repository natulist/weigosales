package com.weigo.sales.activity.base;

import com.weigo.sales.R;
import com.weigo.sales.R.id;
import com.weigo.sales.R.layout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseTitleActivity extends BaseActivity {

	private TextView mTitleText;
	private ImageView mBackButton, mMenuButton;
	private RelativeLayout mTitleBar;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title_base);
		ViewStub stub = (ViewStub) findViewById(R.id.title_stub);
		View view = stub.inflate();
		
		mTitleBar = (RelativeLayout)view.findViewById(R.id.title_bar);
		mTitleText = (TextView) view.findViewById(R.id.title_text);
		mBackButton = (ImageView) view.findViewById(R.id.title_back);		
		mMenuButton = (ImageView) view.findViewById(R.id.title_menu);	
		View contentView = loadContent();		
		initTitle();
		onContentCreate(savedInstanceState, contentView);
	}
	
	
	protected void initTitle() {
		
	}
	
	protected void hideTitle() {
		mTitleText.setVisibility(View.GONE);
	}
	
	private View loadContent() {
		int rid  = getContentLayout();
		if (rid <= 0)
			return null;
		
		FrameLayout parent = (FrameLayout) findViewById(R.id.view_content);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(rid, null);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.MATCH_PARENT);
		
		parent.addView(view, lp);
		return view;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void setTitleText(CharSequence text) {
		mTitleText.setText(text);
	}	
	
	protected void setTitleText(int textId) {
		mTitleText.setText(textId);
	}
	
	protected void setTitleText(CharSequence text, int color){
		setTitleText(text);
		mTitleText.setTextColor(color);
	}
	
	protected void setTitleText(int textId, int color){
		setTitleText(textId);
		mTitleText.setTextColor(color);
	}
	
	
	protected void enableBack(boolean back) {
		if (back) {
			mBackButton.setVisibility(View.VISIBLE);
			mBackButton.setOnClickListener(backClick);
			
		} else {
			mBackButton.setVisibility(View.GONE);
		}
	}
	
	protected void enableMenu(View.OnClickListener onClickListener) {
		if (onClickListener != null) {
			mMenuButton.setVisibility(View.VISIBLE);
			mMenuButton.setOnClickListener(onClickListener);
			//FIXME
//			mMenuButton.setImageResource(R.drawable.title_menu_anim);
			
		} else {
			mMenuButton.setVisibility(View.GONE);
		}
	}
	
	protected void enableMenu(int resid, View.OnClickListener onClickListener) {
		if (onClickListener != null) {
			mMenuButton.setVisibility(View.VISIBLE);
			mMenuButton.setOnClickListener(onClickListener);
			mMenuButton.setImageResource(resid);
			
		} else {
			mMenuButton.setVisibility(View.GONE);
		}
	}
	
	
	protected void setBgColor(int color){
		mTitleBar.setBackgroundColor(color);
	}
	
	
	private View.OnClickListener backClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			backFinish();
		}
	};
	
	protected void backFinish(){
		finish();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected abstract void onContentCreate(Bundle savedInstanceState, View content);
	protected abstract int getContentLayout();
}
