package com.weigo.widget.listview;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.weigo.sales.R;

public class XListViewHeader extends LinearLayout
{
	private LinearLayout	mContainer;
	private ImageView		mLoadingView;
	private TextView		mHintTextView;
	private int				mState = STATE_NORMAL;
	private LinearLayout	mTimeContainer;
	private TextView		mTime;
	
	private ProgressBar		mProgressBar;
	private LinearLayout    mllLoadingLayout;
	private LinearLayout	mllTextLayout;

	AnimationDrawable mAnimationDrawable;
	Animation mAnimation;
	
	public final static int	STATE_NORMAL			= 0;
	public final static int	STATE_READY				= 1;
	public final static int	STATE_REFRESHING		= 2;
	
	private CharSequence mHintNormal;
	private CharSequence mHintReady;
	private CharSequence mHintLoading;

	public XListViewHeader(Context context)
	{
		super(context);
		initView(context);
	}

	public XListViewHeader(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context)
	{
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.xlistview_header, null);
		addView(mContainer,LayoutParams.MATCH_PARENT,0);
		setGravity(Gravity.BOTTOM);
		
		mProgressBar = (ProgressBar) findViewById(R.id.pb_loading);
		mllLoadingLayout = (LinearLayout) findViewById(R.id.ll_blitzcrank_view);
		mllTextLayout = (LinearLayout) findViewById(R.id.xlistview_header_text);
		mLoadingView = (ImageView) findViewById(R.id.anne_listview_loading);
		
		Drawable drawable = mLoadingView.getDrawable();
		if (drawable instanceof AnimationDrawable) {
			mAnimationDrawable = (AnimationDrawable) drawable;
		}
		else {
			mAnimation = AnimationUtils.loadAnimation(context, R.anim.list_refreshing_ani);
		}
		
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mTimeContainer = (LinearLayout) findViewById(R.id.xlistview_header_time_container);
		mTime = (TextView)findViewById(R.id.xlistview_header_time);
		
		mHintNormal = getResources().getText(R.string.xlistview_header_hint_normal);
		mHintReady = getResources().getText(R.string.xlistview_header_hint_ready);
		mHintLoading = getResources().getText(R.string.xlistview_header_hint_loading);
	}
	
	public void enableLoadingView() {
		mProgressBar.setVisibility(View.GONE);
		mllLoadingLayout.setVisibility(View.VISIBLE);
		mllTextLayout.setVisibility(View.VISIBLE);
	}
	
	public void hideRefreshTipView() {
		mllTextLayout.setVisibility(View.GONE);
	}
	
	public void hideTimeView() {
		mTimeContainer.setVisibility(View.GONE);		
	}

	public void setTime(long timestamp)
	{
		/*
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		String timestr = sdf.format(new Date(timestamp));
		mTime.setText(timestr); */
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
		String timestr = sdf.format(new Date(timestamp));
		mTime.setText(timestr);
	}
	
	public void setContainerBackgroundColor(int color)
	{
		mContainer.setBackgroundColor(color);
	}
	
	public void setTimeContainerVisible(boolean visible) 
	{
		mTimeContainer.setVisibility(visible? View.VISIBLE : View.GONE);
	}
	
	public void setHint(int normalId, int readyId, int loadingId)
	{
		CharSequence normal = null;
		CharSequence ready = null;
		CharSequence loading = null;
		
		if (normalId > 0) {
			normal = getResources().getText(normalId);
		}
		
		if (readyId > 0) {
			ready = getResources().getText(readyId);
		}
		
		if (loadingId > 0) {
			loading = getResources().getText(loadingId);
		}
		
		setHint(normal, ready, loading);		
	}
	
	public void setHint(CharSequence normal, CharSequence ready, CharSequence loading)
	{
		if (normal != null)
		{
			mHintNormal = normal;
		}
		
		if (ready != null) 
		{
			mHintReady = ready;
		}
		
		if (loading != null) 
		{
			mHintLoading = loading;
		}
		
		updateText();
	}
	
	void updateText()
	{
		if (mState == STATE_NORMAL) 
		{
			mHintTextView.setText(mHintNormal);
		}
		else if (mState == STATE_READY) 
		{
			mHintTextView.setText(mHintReady);
		}
		else if (mState == STATE_REFRESHING) 
		{
			mHintTextView.setText(mHintLoading);
		}
	}

	public void setState(int state)
	{
		if (state == mState)
			return;

		if (state == STATE_REFRESHING)
		{ 
			mHintTextView.setText(mHintLoading);
			if (mAnimationDrawable != null)
				mAnimationDrawable.start();
			else if (mAnimation != null)
				mLoadingView.startAnimation(mAnimation);
		}
		else if (state == STATE_READY)
		{
			mHintTextView.setText(mHintNormal);
			if (mAnimationDrawable != null)
				mAnimationDrawable.stop();
			else if (mAnimation != null)
				mLoadingView.clearAnimation();
		}
		else
		{
			mHintTextView.setText(mHintReady);
		}
		mState = state;
	}

	public void setVisiableHeight(int height)
	{
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight()
	{
		return mContainer.getHeight();
	}

}
