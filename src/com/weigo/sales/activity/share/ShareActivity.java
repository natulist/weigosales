package com.weigo.sales.activity.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.app.framework.notification.Subscriber;
import com.tcl.common.imageloader.core.ImageLoader;
import com.weigo.base.http.JsonLoader;
import com.weigo.sales.R;
import com.weigo.sales.SHContext;
import com.weigo.sales.activity.base.BaseTitleActivity;
import com.weigo.sales.activity.goods.BaseShopInfo;
import com.weigo.sales.activity.goods.GridImageViewAdapter;
import com.weigo.sales.activity.goods.ImageInfo;
import com.weigo.sales.data.ShopSingleton;

/**
 * @Description: 分享页
 * @author yingjie.lin
 * @date 2014年12月11日 下午2:57:55
 * @copyright TCL-MIE
 */

public class ShareActivity extends BaseTitleActivity {
	private static final String TAG = ShareActivity.class.getSimpleName();
	BaseShopInfo mBaseShopInfo;
	EditText ed_msg;
	private ShareProvider mJsonProvider;
	private JsonLoader mJsonLoader;
	GridImageViewAdapter adapter;
	GridView gridView;
	public GoodsInfo goodsInfo;

	@Override
	protected void onContentCreate(Bundle savedInstanceState, View content) {
		mBaseShopInfo = ShopSingleton.getInstance().mBaseShopInfo;
		TextView tv_title = (TextView) content.findViewById(R.id.share_title);
		tv_title.setText("分享到朋友圈");

		ed_msg = (EditText) content.findViewById(R.id.msg_input);
		ed_msg.setText(mBaseShopInfo.getGoodsTitle());

		gridView = (GridView) content.findViewById(R.id.share_grid);
		// TODO:显示图片
		

		mJsonProvider = new ShareProvider(mBaseShopInfo.getGoodsID(), true);
		mJsonLoader = new JsonLoader(mJsonProvider);
		NotificationCenter.defaultCenter().subscriber(ShareEvent.class, mSubscriber);
		mJsonLoader.load();

	}

	private Subscriber<ShareEvent> mSubscriber = new Subscriber<ShareEvent>() {

		@Override
		public void onEvent(ShareEvent event) {
			if (event.status == NetworkError.SUCCESS) {
				goodsInfo = event.apps;
				List<ImageInfo> tmp = event.apps.imageInfos;
				ImageInfo add = new ImageInfo();
				tmp.add(add);
				adapter = new GridImageViewAdapter(SHContext.getInstance().getApplicationContext(), tmp, 10);
				gridView.setAdapter(adapter);
			}

			else if (event.status == NetworkError.CANCEL) {
			}

			else {
			}
		}
	};

	@Override
	protected int getContentLayout() {
		return R.layout.activity_share;
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		super.initTitle();
		setTitleText(R.string.share_title);
		enableMenu("发送", new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (goodsInfo == null){
					return;
				}
				shareToWechatFriends();
			}
		});
	}

	private ArrayList getUriListForImages() {

		ArrayList myList = new ArrayList();
		for (int i = 0; i < goodsInfo.imageInfos.size(); i++) {
			File file = ImageLoader.getInstance().getDiscCache().get(goodsInfo.imageInfos.get(i).url);
			myList.add(Uri.fromFile(file));
		}
		return myList;
	}

	private void shareToWechatFriends() {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		intent.setType("image/*");
		intent.putExtra("Kdescription", ed_msg.getText().toString());

		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, getUriListForImages());
		// intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		NotificationCenter.defaultCenter().unsubscribe(ShareEvent.class, mSubscriber);
	}
}
