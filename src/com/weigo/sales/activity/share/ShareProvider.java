package com.weigo.sales.activity.share;

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
import com.weigo.sales.activity.goods.ImageInfo;

public class ShareProvider implements JsonProvider {
	private static final String TAG = ShareProvider.class.getSimpleName();
	GoodsInfo mGoodsInfos;
	Context mContext;
	int goodsID;
	boolean mShare;

	public ShareProvider(int goodsid, boolean share) {
		mContext = ServiceContext.getInstance().getApplicationContext();
		goodsID = goodsid;
		mShare = share;
	}

	@Override
	public String getURL() {
		return UrlConfig.getShareListURL();
	}

	@Override
	public Map<String, String> getParams() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("version", AppConfig.PROTOCOL_VERSION);
		paramsMap.put("act", "get_supplier_goods_info");
		paramsMap.put("access_token", PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.loginState, null));
		paramsMap.put("user_type", PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.user_type, null));
		paramsMap.put("goods_id", goodsID + "");

		paramsMap.put("uid", PrefsUtils.loadPrefString(SHContext.getInstance().getApplicationContext(), Constants.user_id, null));
		paramsMap.put("json_type", mShare ? "share" : "edit");
		return paramsMap;
	}

	@Override
	public int parse(JSONObject obj) {
		try {
			int err = obj.getInt(JsonConstants.STATUS);
			if (err != Constants.net.SUCCESS)
				return NetworkError.FAIL_UNKNOWN;
			GoodsInfo item = parseAppInfo(obj);
			mGoodsInfos = item;
			return NetworkError.SUCCESS;
		} catch (JSONException e) {
			NLog.printStackTrace(e);
			return NetworkError.FAIL_UNKNOWN;
		}

	}

	private GoodsInfo parseAppInfo(JSONObject obj) throws JSONException {

		int can_edit = obj.getInt("can_edit");
		int shop_id = obj.getInt("shop_id");
		int goods_id = obj.getInt("goods_id");

		String goodsTitle = obj.getString("goods_title");
		//FIXME
		int main_img_num = 0;// obj.getInt("main_img_num");

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

		GoodsInfo goodsInfo = new GoodsInfo();
		goodsInfo.can_edit = can_edit;
		goodsInfo.main_img_num = main_img_num;
		goodsInfo.setGoodsID(goods_id);
		goodsInfo.setGoodsTitle(goodsTitle);
		goodsInfo.shop_id = shop_id;
		goodsInfo.imageInfos = imageInfos;

		return goodsInfo;
	}

	@Override
	public void onSuccess() {
		ShareEvent event = new ShareEvent();
		event.status = NetworkError.SUCCESS;
		event.apps = mGoodsInfos;

		mGoodsInfos = null;
		NotificationCenter.defaultCenter().publish(event);

	}

	@Override
	public void onCancel() {
		ShareEvent event = new ShareEvent();
		event.status = NetworkError.CANCEL;
		NotificationCenter.defaultCenter().publish(event);
	}

	@Override
	public void onError(int err) {
		ShareEvent event = new ShareEvent();
		event.status = err;
		NotificationCenter.defaultCenter().publish(event);
	}

	@Override
	public boolean supportPost() {
		return true;
	}

	@Override
	public String getHttpsCer() {
		// TODO Auto-generated method stub
		return null;
	}
}
