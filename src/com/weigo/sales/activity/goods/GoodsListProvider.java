package com.weigo.sales.activity.goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.app.framework.log.NLog;
import com.app.framework.network.http.NetworkError;
import com.app.framework.notification.NotificationCenter;
import com.app.framework.util.PrefsUtils;
import com.weigo.base.http.JsonConstants;
import com.weigo.base.http.JsonProvider;
import com.weigo.base.utils.ServiceContext;
import com.weigo.sales.AppConfig;
import com.weigo.sales.Constants;
import com.weigo.sales.SHContext;
import com.weigo.sales.UrlConfig;

public class GoodsListProvider implements JsonProvider {
	private static final String TAG = GoodsListProvider.class.getSimpleName();
	int mNext;
	int mMarkPos;
	boolean mMarkFlag;
	List<BaseShopInfo> mGoodsInfos;
	Context mContext;
	String keyWord = "";

	public GoodsListProvider() {
		mContext = ServiceContext.getInstance().getApplicationContext();
	}
	
	public  void setKey(String key){
		keyWord = key;
	}

	public void mark() {
		mMarkFlag = true;
		mMarkPos = mNext;
		mNext = 0;
	}

	public void unMark() {
		if (mMarkFlag) {
			mNext = mMarkPos;
			mMarkFlag = false;
		}
	}

	@Override
	public String getURL() {
		return UrlConfig.getShareListURL();
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("version", AppConfig.PROTOCOL_VERSION);
		paramsMap.put("act", "get_supplier_goods");
		paramsMap.put("page_index", String.valueOf(mNext));
//		paramsMap.put("goods_state", "1");
//		paramsMap.put("orderby", "time");
		paramsMap.put("keyword", keyWord);
		
		paramsMap.put("uid", PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.user_id, null));

		return paramsMap;
	}

	@Override
	public int parse(JSONObject obj) {
		List<BaseShopInfo> goods = null;
		JSONObject data = null;
		NLog.e(TAG, obj.toString());
		try {
			int err = obj.getInt(JsonConstants.STATUS);
			if (err != Constants.net.SUCCESS)
				return NetworkError.FAIL_UNKNOWN;

			int next = obj.optInt("next_page", -1);
			mNext = next;

			JSONArray jsonArray = obj.optJSONArray("result");
			if (jsonArray != null && jsonArray.length() > 0) {
				goods = new ArrayList<BaseShopInfo>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					BaseShopInfo item = parseAppInfo(jsonObject);
					goods.add(item);
				}
			}

			mGoodsInfos = goods;
			return NetworkError.SUCCESS;
		} catch (JSONException e) {
			NLog.printStackTrace(e);
			return NetworkError.FAIL_UNKNOWN;
		}

	}

	private BaseShopInfo parseAppInfo(JSONObject obj) throws JSONException {

		String shopName = obj.getString("shop_name");
		int shopID = obj.getInt("shop_id");
		String shopIcon = obj.getString("shop_icon");
		int goodsID = obj.getInt("goods_id");
		String goodsTitle = obj.getString("goods_title");
//		String goodsPrice = obj.getString("goods_price");
		String goodsAddTime = obj.getString("goods_add_time");
		String goodsSkuUrl = obj.getString("goods_sku_url");

		JSONArray goodImgs = obj.getJSONArray("goods_images");
		List<ImageInfo> imageInfos = new ArrayList<ImageInfo>();
		for (int i = 0; i < goodImgs.length(); i++) {
			JSONObject obj1 = goodImgs.getJSONObject(i);
			int imgID = obj1.getInt("id");
			int imgW = obj1.getInt("width");
			int imgH = obj1.getInt("height");
			String imgUrl = obj1.getString("url");
			ImageInfo imageInfo = new ImageInfo();
			imageInfo.id = imgID;
			imageInfo.width = imgW;
			imageInfo.height = imgH;
			imageInfo.url = imgUrl;
			imageInfos.add(imageInfo);
		}

		BaseShopInfo goodsInfo = new BaseShopInfo();

		goodsInfo.setGoodsID(goodsID);
		goodsInfo.setGoodsAddTime(goodsAddTime);
		goodsInfo.setShopName(shopName);
//		goodsInfo.setGoodsPrice(goodsPrice);
		goodsInfo.setGoodsTitle(goodsTitle);
		goodsInfo.setGoodsSkuUrl(goodsSkuUrl);
		goodsInfo.setShopID(shopID);
		goodsInfo.setShopIcon(shopIcon);
		goodsInfo.imageInfos = imageInfos;

		return goodsInfo;
	}

	@Override
	public void onSuccess() {
		GoodsListEvent event = new GoodsListEvent();
		event.status = NetworkError.SUCCESS;
		event.apps = mGoodsInfos;

		mGoodsInfos = null;
		NotificationCenter.defaultCenter().publish(event);

	}

	@Override
	public void onCancel() {
		GoodsListEvent event = new GoodsListEvent();
		event.status = NetworkError.CANCEL;
		NotificationCenter.defaultCenter().publish(event);
	}

	@Override
	public void onError(int err) {
		GoodsListEvent event = new GoodsListEvent();
		event.status = err;
		NotificationCenter.defaultCenter().publish(event);
	}

	@Override
	public boolean supportPost() {
		return true;
	}

	public boolean hasMore() {
		return mNext >= 0;
	}

	@Override
	public String getHttpsCer() {
		// TODO Auto-generated method stub
		return null;
	}
}
