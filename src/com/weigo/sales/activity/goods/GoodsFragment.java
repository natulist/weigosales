package com.weigo.sales.activity.goods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.app.framework.log.NLog;
import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.app.framework.notification.Subscriber;
import com.app.framework.util.CollectionUtils;
import com.weigo.base.http.JsonLoader;
import com.weigo.base.utils.UIUtil;
import com.weigo.sales.R;
import com.weigo.sales.activity.base.NetworkBaseFragment;
import com.weigo.widget.listview.XListView;
import com.weigo.widget.listview.XListView.IXListViewListener;
import com.weigo.widget.listview.XListViewHeader;

public class GoodsFragment extends NetworkBaseFragment {
	private static final String TAG = GoodsFragment.class.getSimpleName();
	private XListView mListView;
	private XListViewHeader mHeadView;
	private GoodsAdapter mAdapter;
	private GoodsListProvider mJsonProvider;
	private JsonLoader mJsonLoader;
	private List<BaseShopInfo> mAppInfos;
	private String mMD5Hash = null;

	ImageButton searchButton, clearButton;
	EditText searchEditText;
	
	@Override
	protected void findViewByIds(View parent) {
		super.findViewByIds(parent);
		mListView = (XListView) parent.findViewById(R.id.recommend_list);
		searchButton = (ImageButton)parent.findViewById(R.id.searchBtn);
		clearButton = (ImageButton)parent.findViewById(R.id.searchDeleteBtn);
		searchEditText = (EditText)parent.findViewById(R.id.search_edit);
		
	}

	@Override
	protected void initViews() {
		mAdapter = new GoodsAdapter(getActivity());
		mListView.setAdapter(mAdapter);
		mHeadView = mListView.getRefreshHeader();
		mHeadView.setHint(R.string.xlistview_header_hint_normal, R.string.xlistview_header_hint_ready, R.string.xlistview_header_hint_loading);

		mHeadView.enableLoadingView();
		mHeadView.hideTimeView();
		mHeadView.setTime(System.currentTimeMillis());
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);

		// View gap = View.inflate(getActivity(), R.layout.xlistview_gap, null);
		// mListView.addHeaderView(gap);
		mListView.setXListViewListener(mLoadAction);
		if (!CollectionUtils.isEmpty(mAppInfos)) {
			NLog.i(TAG, "cache info not empty");
			mAdapter.setRecommendData(mAppInfos);
			mAppInfos = null;
			showContent();
		}
		mJsonProvider = new GoodsListProvider();
		mJsonLoader = new JsonLoader(mJsonProvider);
		NotificationCenter.defaultCenter().subscriber(GoodsListEvent.class, mSubscriber);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mJsonProvider.setKey(searchEditText.getText().toString());
				pullRefresh();
				//TODO:进度框
			}
		});
		
		clearButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchEditText.setText("");
				mJsonProvider.setKey("");
				pullRefresh();
			}
		});

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.setClassLoader(getClass().getClassLoader());
		if (mAdapter != null) {

			if (!CollectionUtils.isEmpty(mAdapter.getShopShareList())) {
				outState.putParcelableArrayList("apps", new ArrayList<BaseShopInfo>(mAdapter.getShopShareList()));
			}

			if (!TextUtils.isEmpty(mMD5Hash)) {
				outState.putString("hash", mMD5Hash);
			}
		}

		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
			mAppInfos = savedInstanceState.getParcelableArrayList("apps");
			mMD5Hash = savedInstanceState.getString("hash");
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private IXListViewListener mLoadAction = new IXListViewListener() {

		@Override
		public void onRefresh() {
			NLog.v(TAG, "onRefresh");
			pullRefresh();
		}

		@Override
		public void onLoadMore() {
			NLog.v(TAG, "onLoadMore");
			loadMore();
		}

	};

	@Override
	protected void doReload() {
		if (!mJsonLoader.load()) {
			showFail();
		}
	}

	@Override
	protected boolean hasCache() {
		return mAdapter.getCount() > 0;
	}

	void onOk(Collection<BaseShopInfo> shares, String md5Hash) {
		NLog.e(TAG, "onOk");
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(mJsonProvider.hasMore());

		if (mAdapter.getCount() == 0) {
			mLastRefreshTime = System.currentTimeMillis();
			mHeadView.setTime(mLastRefreshTime);

			// if (md5Hash.equals(mMD5Hash)) {
			// NLog.w(TAG, "hash same, none changed!");
			// mJsonProvider.unMark();
			// showContent();
			// return;
			// }

			NLog.i(TAG, "hash changed, has new content!");
			mAdapter.setRecommendData(shares);
			mMD5Hash = md5Hash;
			NLog.i(TAG, "after setRecommendData");

			if (isVisible()) {
				mAdapter.startScroll();
			}

		} else {
			if (mPullingRefresh) {
				NLog.e(TAG, "mPullingRefresh refreshShopShare");
				mAdapter.refreshShopShare(shares);
			} else {
				mAdapter.addShopShare(shares);
			}
		}

		showContent();
	}

	void showError(int err) {
		NLog.w(TAG, "showError err = %d, count = %d", err, mAdapter.getCount());
		if (mAdapter.getCount() > 0) {
			UIUtil.showToast(getActivity(), R.string.network_fail, true);
		} else {
			showFail();
		}
	}

	private Subscriber<GoodsListEvent> mSubscriber = new Subscriber<GoodsListEvent>() {

		@Override
		public void onEvent(GoodsListEvent event) {
			NLog.e(TAG, "onEvent");
			if (mPullingRefresh) {
				// mPullingRefresh = false;
				mListView.stopRefresh();
				if (event.status != NetworkError.CANCEL) {
					NLog.i(TAG, "refresh over!");
				}

			}

			if (mLoadingMore) {
				mLoadingMore = false;
				mListView.stopLoadMore();
			}

			mHandler.removeMessages(MSG_REFRESH_TIMEOUT);

			if (event.status == NetworkError.SUCCESS) {
				onOk(event.apps, event.md5Hash);
			} else if (event.status == NetworkError.CANCEL) {
				if (!hasCache())
					showFail();
			} else {
				showError(event.status);
			}

			if (mPullingRefresh) {
				mPullingRefresh = false;
			}
		}
	};

	private boolean mPullingRefresh = false;
	private boolean mLoadingMore = false;
	private long mLastRefreshTime;

	private boolean needRefresh() {
		long current = System.currentTimeMillis();
		return (current - mLastRefreshTime > 5 * 60 * 1000L);
	}

	private void pullRefresh() {
		if (mPullingRefresh)
			return;
		NLog.d(TAG, "pullRefresh");

		if (mLoadingMore) {
			mJsonLoader.cancel();
		}

		mJsonProvider.mark();
		if (mJsonLoader.load()) {
			mPullingRefresh = true;
			mHandler.removeMessages(MSG_REFRESH_TIMEOUT);
			mHandler.sendEmptyMessageDelayed(MSG_REFRESH_TIMEOUT, 5 * 1000);
		} else {
			mListView.stopRefresh();
			mJsonProvider.unMark();
		}
	}

	private void loadMore() {
		if (mLoadingMore)
			return;
		NLog.d(TAG, "loadMore");

		if (mPullingRefresh) {
			mJsonLoader.cancel();
			mJsonProvider.unMark();
		}

		if (mJsonLoader.load()) {
			mLoadingMore = true;
			mHandler.removeMessages(MSG_REFRESH_TIMEOUT);
			mHandler.sendEmptyMessageDelayed(MSG_REFRESH_TIMEOUT, 5 * 1000);
		} else {
			mListView.stopLoadMore();
		}
	}

	private void autoRefresh() {
		if (!needRefresh() || mPullingRefresh || mLoadingMore) {
			return;
		}

		NLog.d(TAG, "autoRefresh");
		mJsonProvider.mark();
		if (loadRefresh()) {
			mPullingRefresh = true;
			mHandler.removeMessages(MSG_REFRESH_TIMEOUT);
			mHandler.sendEmptyMessageDelayed(MSG_REFRESH_TIMEOUT, 5 * 1000);
		} else {
			mJsonProvider.unMark();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (!needRefresh() || mPullingRefresh || mLoadingMore) {
			return;
		}
		mHandler.sendEmptyMessageDelayed(MSG_AUTO_REFRESH, 100);
	}

	@Override
	public void onPageResume() {
		super.onPageResume();
		if (mAdapter != null)
			mAdapter.startScroll();
	}

	@Override
	public void onPagePause() {
		super.onPagePause();
		if (mAdapter != null)
			mAdapter.stopScroll();
	}

	@Override
	public void onDestroy() {
		NotificationCenter.defaultCenter().unsubscribe(GoodsListEvent.class, mSubscriber);
		super.onDestroy();
		mHandler.removeMessages(MSG_AUTO_REFRESH);
		mHandler.removeMessages(MSG_REFRESH_TIMEOUT);
	}

	private static final int MSG_REFRESH_TIMEOUT = 1;
	private static final int MSG_AUTO_REFRESH = 2;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_REFRESH_TIMEOUT) {
				mJsonLoader.cancel();
				UIUtil.showToast(getActivity(), R.string.network_timeout, false);
			}

			else if (msg.what == MSG_AUTO_REFRESH) {
				removeMessages(MSG_AUTO_REFRESH);

				autoRefresh();
			}
		}
	};

	@Override
	protected int getSubContentLayout() {
		return R.layout.fragment_share;
	}

	// FIXME
	// @Override
	// protected void onDownloadStatusChanged(int event, BaseAppInfo app, int
	// extra) {
	// int first = mListView.getFirstVisiblePosition();
	// int last = mListView.getLastVisiblePosition();
	//
	// int index = mAdapter.syncAppInfo(app);
	//
	// index += 1;
	// NLog.i(TAG, "first = %d, last = %d, index = %d", first, last, index);
	// if (index >= 0 && index >= first && index <= last && isVisible() ) {
	// mAdapter.refreshItem(index-1);
	//
	// //mAdapter.notifyDataSetChanged();
	// }
	// }
	//
	// @Override
	// protected void onPackageStatusChanged(String pkgName, int status) {
	// boolean ret = mAdapter.syncPackageStatus(pkgName, status);
	// if (ret && isVisible()) {
	// mAdapter.notifyDataSetChanged();
	// }
	// }
	

}
