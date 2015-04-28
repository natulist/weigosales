package com.weigo.sales.activity.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;

import com.app.framework.log.NLog;
import com.app.framework.network.NetworkHelper;
import com.weigo.sales.R;

public abstract class NetworkBaseFragment extends BaseFragment {

	private View mLoadProgressView;
	private View mLoadFailView;
	private ViewStub mProgressStub;
	private ViewStub mFailStub;
	private ViewStub mContentStub;
	private View mSubContentView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.container_network_base;
	}

	@Override
	protected void findViewByIds(View parent) {
		mContentStub = (ViewStub) parent.findViewById(R.id.stub_content);
		mProgressStub = (ViewStub) parent.findViewById(R.id.stub_progress);
		mFailStub = (ViewStub) parent.findViewById(R.id.stub_fail);
		
		int rid = getSubContentLayout();
		if (rid <= 0) {
			throw new RuntimeException("network base activity has none valid sub content layout");
		}
		
		mContentStub.setLayoutResource(rid);
		mSubContentView = mContentStub.inflate();
	}

	@Override
	protected void initViews() {
	}

	protected View getSubContentView() {
		return mSubContentView;
	}
	
	protected void  showContent() {
		NLog.v(getClass().getSimpleName(), "showContent");
		if (isRemoving())
			return;
		
		if (mLoadProgressView != null) {
			mLoadProgressView.setVisibility(View.GONE);
		}
		
		if (mLoadFailView != null) {
			mLoadFailView.setVisibility(View.GONE);
		}
		
		mSubContentView.setVisibility(View.VISIBLE);
	}

	protected void showProgress()
	{
		NLog.v(getClass().getSimpleName(), "showProgress");
		if (isRemoving())
			return;
		
		if (mLoadFailView != null) {
			mLoadFailView.setVisibility(View.GONE);
		}
		
		mSubContentView.setVisibility(View.GONE);
		
		if (mLoadProgressView == null) {
			View view = mProgressStub.inflate();
			mLoadProgressView = view;
		}
		
		mLoadProgressView.setVisibility(View.VISIBLE);
	}
	
	private View.OnClickListener mClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			loadRefresh();
		}
	};
	
	protected void showFail()
	{
		NLog.v(getClass().getSimpleName(), "showFail");
		if (isRemoving())
			return;
		
		if (mLoadProgressView != null) {
			mLoadProgressView.setVisibility(View.GONE);
		}
		
		mSubContentView.setVisibility(View.GONE);
		
		if (mLoadFailView == null) {
			View view = mFailStub.inflate();
			ImageView iv = (ImageView) view.findViewById(R.id.network_click);
			iv.setOnClickListener(mClickListener);
			mLoadFailView = view;
		}
		
		mLoadFailView.setVisibility(View.VISIBLE);
	}
	
	protected boolean loadRefresh()
	{
		boolean cached = hasCache();
		
		if (!NetworkHelper.sharedHelper().isNetworkAvailable()) {
			
			if (!cached)
				showFail();
			else 
				showContent();
			
			return false;
		}
		if (!cached)
			showProgress();
		
		doReload();
		return true;
	}
	
	protected void doReload() {
		
	}
	
	protected boolean hasCache() {
		return false;
	}
	
	protected abstract int getSubContentLayout();
}
