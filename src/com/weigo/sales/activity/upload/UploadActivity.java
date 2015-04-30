package com.weigo.sales.activity.upload;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.app.framework.notification.Subscriber;
import com.weigo.base.http.JsonLoader;
import com.weigo.sales.R;
import com.weigo.sales.SHContext;
import com.weigo.sales.activity.base.BaseTitleActivity;
import com.weigo.sales.activity.goods.BaseShopInfo;
import com.weigo.sales.activity.share.GoodsInfo;
import com.weigo.sales.activity.share.ShareEvent;
import com.weigo.sales.activity.share.ShareProvider;
import com.weigo.sales.data.ShopSingleton;
import com.weigo.sales.gettoken.GetTokenState;
import com.weigo.sales.gettoken.GetUploadTokenProvider;
import com.weigo.sales.upload.Upload;

/**
 * @Description: 分享页
 * @author yingjie.lin
 * @date 2014年12月11日 下午2:57:55
 * @copyright TCL-MIE
 */

public class UploadActivity extends BaseTitleActivity {
	private static final String TAG = UploadActivity.class.getSimpleName();
	BaseShopInfo mBaseShopInfo;
	EditText ed_msg;
	Upload mUpload;
	private static final int MAX_NUM_IMG = 9;

	private ShareProvider mJsonProvider;
	private JsonLoader mJsonLoader;
	UploadGridViewAdapter adapter;
	GridView gridView;
	public GoodsInfo goods;

	@Override
	protected void onContentCreate(Bundle savedInstanceState, View content) {
		mBaseShopInfo = ShopSingleton.getInstance().mBaseShopInfo;
		TextView tv_title = (TextView) content.findViewById(R.id.share_title);
		tv_title.setText("编辑分享内容");

		ed_msg = (EditText) content.findViewById(R.id.msg_input);
		ed_msg.setText(mBaseShopInfo.getGoodsTitle());

		mJsonProvider = new ShareProvider(mBaseShopInfo.getGoodsID(), false);
		mJsonLoader = new JsonLoader(mJsonProvider);
		NotificationCenter.defaultCenter().subscriber(ShareEvent.class, mSubscriber);
		mJsonLoader.load();

		gridView = (GridView) content.findViewById(R.id.share_grid);
		// gridView.setAdapter(new
		// UploadGridViewAdapter(SHContext.getInstance().getApplicationContext(),
		// mBaseShopInfo.imageInfos, MAX_NUM_IMG));

		NotificationCenter.defaultCenter().subscriber(GetTokenState.class, uploadCallbackSubscriber);
	}

	private Subscriber<ShareEvent> mSubscriber = new Subscriber<ShareEvent>() {

		@Override
		public void onEvent(ShareEvent event) {
			if (event.status == NetworkError.SUCCESS) {
				goods = event.apps;
				adapter = new UploadGridViewAdapter(SHContext.getInstance().getApplicationContext(), event.apps.imageInfos, MAX_NUM_IMG);
				gridView.setAdapter(adapter);
			}

			else if (event.status == NetworkError.CANCEL) {
			}

			else {
			}
		}
	};

	private Subscriber<GetTokenState> uploadCallbackSubscriber = new Subscriber<GetTokenState>() {

		@Override
		public void onEvent(GetTokenState event) {
			switch (event.status) {
			case GetTokenState.GET_SUCCESS:
				int num = 0;
				for (int i = 0; i < goods.imageInfos.size(); i++) {
					if (goods.imageInfos.get(i).select) {
						num++;
					}
				}
				goods.setGoodsTitle(ed_msg.getText().toString());
				mUpload = new Upload(goods, event.data, num);
				uploadImages();
				break;
			case GetTokenState.UPLOAD_SUCCESS:
				Toast.makeText(SHContext.getInstance().getApplicationContext(), "更新商品成功", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(SHContext.getInstance().getApplicationContext(), "更新商品失败", Toast.LENGTH_SHORT).show();
				break;
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
		setTitleText(R.string.edit_title);
		enableMenu("上传", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO:上传
				if (goods == null){
					return;
				}
				uploadExec();
			}
		});
	}

	private void uploadExec() {
		GetUploadTokenProvider mJsonProvider = new GetUploadTokenProvider();
		JsonLoader mJsonLoader = new JsonLoader(mJsonProvider);
		mJsonLoader.load();
	}

	private void uploadImages() {
		for (int i = 0; i < goods.imageInfos.size(); i++) {
			if (goods.imageInfos.get(i).select) {
				mUpload.UploadImg(i);
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		NotificationCenter.defaultCenter().unsubscribe(GetTokenState.class, uploadCallbackSubscriber);
		NotificationCenter.defaultCenter().unsubscribe(ShareEvent.class, mSubscriber);
	}
}
