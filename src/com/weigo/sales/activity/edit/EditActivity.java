package com.weigo.sales.activity.edit;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.app.framework.notification.Subscriber;
import com.weigo.base.http.JsonLoader;
import com.weigo.sales.R;
import com.weigo.sales.SHContext;
import com.weigo.sales.activity.base.BaseTitleActivity;
import com.weigo.sales.activity.goods.BaseShopInfo;
import com.weigo.sales.activity.goods.ImageInfo;
import com.weigo.sales.activity.share.GoodsInfo;
import com.weigo.sales.activity.share.ShareEvent;
import com.weigo.sales.activity.share.ShareProvider;
import com.weigo.sales.data.ShopSingleton;

/**
 * @Description: 分享页
 * @author yingjie.lin
 * @date 2014年12月11日 下午2:57:55
 * @copyright TCL-MIE
 */

public class EditActivity extends BaseTitleActivity {
	private static final String TAG = EditActivity.class.getSimpleName();
	BaseShopInfo mBaseShopInfo;
	EditText ed_msg;
	private static final int MAX_NUM_IMG = 9;

	private ShareProvider mJsonProvider;
	private JsonLoader mJsonLoader;
	EditGridViewAdapter adapter;
	GridView gridView;
	public GoodsInfo goods;
	private List<ImageInfo> editImgs;

	@Override
	protected void onContentCreate(Bundle savedInstanceState, View content) {
		mBaseShopInfo = ShopSingleton.getInstance().mBaseShopInfo;

        mJsonProvider = new ShareProvider(mBaseShopInfo.getGoodsID(), false);
		mJsonLoader = new JsonLoader(mJsonProvider);
		NotificationCenter.defaultCenter().subscriber(ShareEvent.class, mSubscriber);
		mJsonLoader.load();

		gridView = (GridView) content.findViewById(R.id.edit_grid);
		// gridView.setAdapter(new
		// UploadGridViewAdapter(SHContext.getInstance().getApplicationContext(),
		// mBaseShopInfo.imageInfos, MAX_NUM_IMG));

	}

	private Subscriber<ShareEvent> mSubscriber = new Subscriber<ShareEvent>() {

		@Override
		public void onEvent(ShareEvent event) {
			if (event.status == NetworkError.SUCCESS) {
			    editImgs = event.apps.imageInfos;
				goods = event.apps;
				adapter = new EditGridViewAdapter(SHContext.getInstance().getApplicationContext(), event.apps.imageInfos, MAX_NUM_IMG);
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
		return R.layout.activity_edit;
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		super.initTitle();
		setTitleText(R.string.edit_title);
	      enableMenu("保存", new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                //TODO:保存選擇的圖片,訪問服務器告之選擇了哪幾個圖片ID
	                NotificationCenter.defaultCenter().publish("update_img", editImgs);
	                //通知主頁加載數據
	                NotificationCenter.defaultCenter().publish("reload_goods", null);
	                finish();
	            }
	        });
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		NotificationCenter.defaultCenter().unsubscribe(ShareEvent.class, mSubscriber);
	}
}
