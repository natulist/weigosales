package com.weigo.widget.listview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weigo.sales.R;

public class XListViewFooter extends LinearLayout
{
	public final static int	STATE_NORMAL	= 0;
	public final static int	STATE_READY		= 1;
	public final static int	STATE_LOADING	= 2;

	private Context			mContext;

	private View			mContentView;
	private ImageView		mLoadingView;
	private TextView		mHintView;
	private CharSequence mHintNormal;
	private CharSequence mHintLoading;
	private CharSequence mHintReady;
	private int mState = STATE_NORMAL;
	private AnimationDrawable mAnimationDrawable;
	private Animation mAnimation;
	
	public XListViewFooter(Context context)
	{
		super(context);
		initView(context);
	}

	public XListViewFooter(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}
	
	public void setHint(CharSequence normal, CharSequence ready)
	{
		if (normal != null) {
			mHintNormal = normal;
		}
		
		if (ready != null) {
			mHintReady = ready;
		}

		updateText();
	}
	
	void updateText()
	{
		if (mState == STATE_NORMAL) {
			mHintView.setText(mHintNormal);
		}
		else if (mState == STATE_READY) {
			mHintView.setText(mHintReady);
		}		
	}

	public void setState(int state)
	{
		
		if (state == STATE_READY)
		{
			normal();
			mHintView.setText(mHintReady);
		}
		else if (state == STATE_LOADING)
		{
			loading();
			mHintView.setText(mHintLoading);
		}
		else
		{
			normal();
			mHintView.setText(mHintNormal);
		}
	}

	public void setBottomMargin(int height)
	{
		if (height < 0)
			return;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.bottomMargin = height;
		mContentView.setLayoutParams(lp);
	}

	public int getBottomMargin()
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		return lp.bottomMargin;
	}

	/**
	 * normal status
	 */
	public void normal()
	{
		mHintView.setVisibility(View.VISIBLE);
		mLoadingView.setVisibility(View.GONE);
		if (mAnimationDrawable != null) {
			mAnimationDrawable.stop();
		}
		else if (mAnimation != null) {
			mLoadingView.clearAnimation();
		}		
	}

	/**
	 * loading status
	 */
	public void loading()
	{
	
		mLoadingView.setVisibility(View.VISIBLE);
		if (mAnimationDrawable != null) {
			mAnimationDrawable.start();
		}
		else if (mAnimation != null) {
			mLoadingView.startAnimation(mAnimation);
		}	
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide()
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = 0;
		mContentView.setLayoutParams(lp);
	}

	/**
	 * show footer
	 */
	public void show()
	{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
		lp.height = LayoutParams.WRAP_CONTENT;
		mContentView.setLayoutParams(lp);
	}
 
	private void initView(Context context)
	{
		mContext = context;
		LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(moreView);
		moreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

		mContentView = moreView.findViewById(R.id.xlistview_footer_content);
		mLoadingView = (ImageView)moreView.findViewById(R.id.xlistview_footer_progressbar);
		Drawable drawable = mLoadingView.getDrawable();
		if (drawable != null && drawable instanceof AnimationDrawable) {
			mAnimationDrawable = (AnimationDrawable) drawable;
		}
		else {
			mAnimation = AnimationUtils.loadAnimation(context, R.anim.list_refreshing_ani);
		}
		
		mHintView = (TextView) moreView.findViewById(R.id.xlistview_footer_hint_textview);
		
		mHintLoading = getResources().getText(R.string.xlistview_footer_hint_loading);
		mHintNormal = getResources().getText(R.string.xlistview_footer_hint_normal);
		mHintReady = getResources().getText(R.string.xlistview_footer_hint_ready);
	}

}
