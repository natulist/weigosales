package com.weigo.sales.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {

	protected View mContentView;
	private boolean mFirstVisible = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		if (mContentView != null) {
			ViewGroup parent =(ViewGroup) mContentView.getParent();
			if (parent != null)
				parent.removeView(mContentView);
			
			return mContentView;
		}
		
		if (getLayoutTheme() > 0) {
			final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), getLayoutTheme());
			LayoutInflater inflater2 = inflater.cloneInContext(contextThemeWrapper);
			inflater = inflater2;
		}
		
		View layout =inflater.inflate(this.getLayoutResId(), container, false);
		mContentView = layout;
		findViewByIds(layout);
		initViews();
		
		return layout;
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	protected int getLayoutTheme() {
		return 0;
	}
	
	protected int getLayoutResId() {
		return 0;
	}
	
	
	protected void findViewByIds(View parent) {
		
	}
	
	protected void initViews() {
		
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if (isVisibleToUser) {
			if (!mFirstVisible)
				mFirstVisible = true;
			onPageResume();
		} else if (mFirstVisible){
			onPagePause();
		}
		
	}
	
	protected int getPageId() {
		return 0;
	}
	
	protected String getPageName() {
		return getClass().getSimpleName();
	}
	
	public void onPageResume() {
	}
	
	public void onPagePause() {
	}
	
	
//	protected void onDownloadStatusChanged(int event, BaseAppInfo app, int extra)
//	{
//		
//	}
//	
//	protected void onPackageStatusChanged(String pkgName, int status) {
//		
//	}
}
