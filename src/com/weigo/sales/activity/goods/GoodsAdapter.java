package com.weigo.sales.activity.goods;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.app.framework.log.NLog;
import com.weigo.sales.R;
import com.weigo.sales.activity.base.AbstractListItem;

public class GoodsAdapter extends BaseAdapter implements OnItemClickListener, Runnable {

	private final static int POST_DELAYED_TIME = 5000;
	List<AbstractListItem> mItems;
	Context mContext;
	List<BaseShopInfo> mShopInfos;

	public GoodsAdapter(Context context) {
		mItems = new ArrayList<AbstractListItem>();
		mShopInfos = new ArrayList<BaseShopInfo>();
		this.mContext = context;
	}

	private Handler mHandler = new Handler();

	public void setRecommendData(Collection<BaseShopInfo> apps) {

		clear();
		addShopShare(apps, false);
	}

	protected void refreshItem(int index) {
		if (index < 0 || index >= getCount())
			return;

		AbstractListItem item = mItems.get(index);
		item.refresh();
	}


	public List<BaseShopInfo> getShopShareList() {
		return mShopInfos;
	}


	public void clear() {
		mShopInfos.clear();
		mItems.clear();
	}

	private boolean mScrollContinue;

	public void startScroll() {
		if (mScrollContinue)
			return;

		NLog.i("RecommendAdapter", "startScroll");
		mScrollContinue = true;
		mHandler.removeCallbacks(this);
		mHandler.postDelayed(this, POST_DELAYED_TIME);
	}

	@Override
	public void run() {
		if (!mScrollContinue)
			return;

		if (mScrollContinue) {
			mHandler.postDelayed(this, POST_DELAYED_TIME);
		}
	}

	int mCurrentGalleryBannerItem = 0;


	public void stopScroll() {
		NLog.i("RecommendAdapter", "stopScroll");
		mScrollContinue = false;
		mHandler.removeCallbacks(this);
	}


	public void addShopShare(Collection<BaseShopInfo> apps) {
		addShopShare(apps, true);
	}
	
	public void refreshShopShare(Collection<BaseShopInfo> apps){
		mShopInfos.clear();
		mItems.clear();
		addShopShare(apps, true);
	}

	void addShopShare(Collection<BaseShopInfo> shares, boolean notify) {
		if (shares == null || shares.size() == 0)
			return;

		mShopInfos.addAll(shares);
		for (BaseShopInfo appInfo : shares) {
			SimpleAppItem item = new SimpleAppItem(appInfo);
			item.setSource("ShareFragment");
			mItems.add(item);
		}

		if (notify)
			notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final AbstractListItem item = (AbstractListItem) getItem(position);
		final View v = item.getView(mContext, convertView, null);
		if (v != null) {
			v.setTag(R.id.tag_item, item);
			if (item.needAttachTo()) {
				v.post(new Runnable() {
					@Override
					public void run() {
						item.onViewAttached(v);
					}
				});
			}

		}

		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		
	}



}
