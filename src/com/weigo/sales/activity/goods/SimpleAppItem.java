package com.weigo.sales.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.framework.network.HostNameResolver;
import com.app.framework.util.PrefsUtils;
import com.weigo.sales.Constants;
import com.weigo.sales.R;
import com.weigo.sales.SHContext;
import com.weigo.sales.activity.LoginActivity;
import com.weigo.sales.activity.base.AbstractListItem;
import com.weigo.sales.data.ShopSingleton;
import com.weigo.sales.widget.AsyncRoundedImageView;

public class SimpleAppItem extends AbstractListItem {

	protected final BaseShopInfo mAppInfo;
	protected ViewHolder mViewHolder;
	protected String mSourceId = null;

	public SimpleAppItem(BaseShopInfo appInfo) {
		mAppInfo = appInfo;
	}

	public BaseShopInfo getAppInfo() {
		return mAppInfo;
	}

	public void setSource(String id) {
		this.mSourceId = id;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (o == this)
			return true;

		if (o instanceof SimpleAppItem) {
			SimpleAppItem sai = (SimpleAppItem) o;
			return sai.mAppInfo.equals(this.mAppInfo);
		}

		return false;
	}

	@Override
	public void refresh() {
		if (mViewHolder != null) {
			showBaseInfo(mViewHolder);
		}
	}

	@Override
	public View getView(Context context, View convertView, ViewGroup root) {
		View v = null;
		ViewHolder holder = null;
		if (convertView != null) {
			Integer t = (Integer) convertView.getTag(R.id.tag_item_type);
			if (t == null || t != ITEM_TYPE_APP) {
				convertView = null;
			} else {
				v = convertView;
				holder = (ViewHolder) convertView.getTag();
			}
		}

		if (v == null) {
			v = View.inflate(context, R.layout.list_item_app, root);
			v.setTag(R.id.tag_item_type, ITEM_TYPE_APP);
		}

		if (holder == null) {
			holder = new ViewHolder();
			holder.appIcon = (AsyncRoundedImageView) v.findViewById(R.id.user_item_icon);
			holder.appNameText = (TextView) v.findViewById(R.id.user_item_name);
			holder.appSizeText = (TextView) v.findViewById(R.id.user_item_info);
			holder.gridView = (MyGridView) v.findViewById(R.id.image_grid);
			holder.shareBtn = (Button) v.findViewById(R.id.share);
			holder.editBtn = (Button) v.findViewById(R.id.edit);
//			holder.buyBtn = (Button) v.findViewById(R.id.buy);
			v.setTag(holder);
		}

		mViewHolder = holder;
		showBaseInfo(holder);
		return v;
	}

	private void showBaseInfo(ViewHolder viewHolder) {
		// 显示姓名
		viewHolder.appNameText.setText(mAppInfo.getShopName());
		viewHolder.appSizeText.setText(mAppInfo.getGoodsTitle());
		viewHolder.appIcon.setImageResource(R.drawable.ic_launcher);
		viewHolder.shareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Constants.intent.SHARE);
				ShopSingleton.getInstance().mBaseShopInfo = mAppInfo;
				ShopSingleton.getInstance().imageInfos = mAppInfo.imageInfos;

				// intent.putExtra("data", mAppInfo);
				v.getContext().startActivity(intent);
			}
		});

		viewHolder.editBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO edit
				Intent intent = new Intent(Constants.intent.EDIT);
				ShopSingleton.getInstance().mBaseShopInfo = mAppInfo;
				ShopSingleton.getInstance().imageInfos = mAppInfo.imageInfos;

				v.getContext().startActivity(intent);
			}
		});
		
//		viewHolder.buyBtn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setAction("android.intent.action.VIEW");
//				Uri content_url = Uri.parse(mAppInfo.goods_sku_url);
////				NLog.e("test", mAppInfo.goods_sku_url);
//				intent.setData(content_url);
//				try {
//					v.getContext().startActivity(intent);
//				} catch (Exception e) {
//					e.printStackTrace();
//					Toast.makeText(v.getContext(), "请安装浏览器", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
		
		String userType = PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.user_type);
		if ((userType == null) || userType.equalsIgnoreCase(LoginActivity.USER_ID + "")) {
			viewHolder.editBtn.setVisibility(View.GONE);
		} else {
			viewHolder.editBtn.setVisibility(View.VISIBLE);
		}
		// 显示图片
		if (!TextUtils.isEmpty(mAppInfo.getShopIcon())) {
			viewHolder.appIcon.displayImage(HostNameResolver.resovleURL(mAppInfo.getShopIcon()));
		}
		viewHolder.gridView.setAdapter(new GridImageViewAdapter(SHContext.getInstance().getApplicationContext(), mAppInfo.imageInfos, 0));
		viewHolder.gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Constants.intent.VIEW_PIC);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// intent.putExtra("postion", position);
				// intent.putExtra("data", mAppInfo);
				ShopSingleton.getInstance().mBaseShopInfo = mAppInfo;
				ShopSingleton.getInstance().imageInfos = mAppInfo.imageInfos;
				ShopSingleton.getInstance().index = position;
				view.getContext().startActivity(intent);
			}
		});
	}

	protected static class ViewHolder {
		public AsyncRoundedImageView appIcon;
		public TextView appNameText;
		public TextView appSizeText;
		public MyGridView gridView;
		public Button shareBtn;
		public Button editBtn;
//		public Button buyBtn;
	}
}
