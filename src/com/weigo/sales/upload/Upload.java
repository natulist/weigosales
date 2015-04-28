package com.weigo.sales.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.app.framework.log.NLog;
import com.google.gson.Gson;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.tcl.common.imageloader.core.ImageLoader;
import com.weigo.base.http.JsonLoader;
import com.weigo.sales.activity.share.GoodsInfo;

/**
 * @Description:
 * @author yingjie.lin
 * @date 2014年12月22日 下午9:03:45
 * @copyright TCL-MIE
 */

public class Upload {
	private static final String TAG = Upload.class.getSimpleName();
	String mToken;
	int mImgUploadNum = 0;
	GoodsInfo mGoodsInfo;

	public Upload(GoodsInfo goodsInfo, String token, int num) {
		mToken = token;
		mGoodsInfo = goodsInfo;
		mImgUploadNum = num;
		NLog.e(TAG, "%d", num);
	}

	public void UploadImg(int id) {
//		if (id > mGoodsInfo.imageInfos.size()) {
//			NLog.e(TAG, "imageInfos error");
//			return;
//		}
		File data = ImageLoader.getInstance().getDiscCache().get(mGoodsInfo.imageInfos.get(id).url);
		final int imgID = mGoodsInfo.imageInfos.get(id).id;
		String key = ImageLoader.getInstance().getDiscCache().get(mGoodsInfo.imageInfos.get(id).url).getName();
		// NLog.e(TAG, "imgID:%d, %s:---%s",imgID, key, mToken);
		String picSuff = mGoodsInfo.imageInfos.get(id).url.substring(mGoodsInfo.imageInfos.get(id).url.lastIndexOf("."));
		key = key + picSuff;
		UploadManager uploadManager = new UploadManager();
		uploadManager.put(data, key, mToken, new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo info, JSONObject response) {

				int w = 0, h = 0;
				try {
					w = response.getInt("width");
					h = response.getInt("height");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.e("qiniu", info.toString());
				Log.e("qiniu1", response.toString());
				// Log.e("ID", key+"");
				addFile(key, imgID, w, h);
				Log.e("ID", imgUrlArr.size() + "");

				if (imgUrlArr.size() >= mImgUploadNum) {
					// TODO:上传图片业务逻辑
					NLog.e(TAG, "上传");
					PostUploadUrlProvider mJsonProvider = new PostUploadUrlProvider(getUrlJsonString());
					JsonLoader mJsonLoader = new JsonLoader(mJsonProvider);
					mJsonLoader.load();
				}
			}
		}, null);
	}

	List<ImageUrl> imgUrlArr = new ArrayList<ImageUrl>();

	private synchronized void addFile(String name, int id, int w, int h) {

		ImageUrl obj = new ImageUrl();
		obj.id = id;
		obj.url = name;
		obj.height = h;
		obj.width = w;
		imgUrlArr.add(obj);

	}

	class ImageUrl {
		int id;
		int height;
		int width;
		String url;
	}

	class GoodsUpdate {
		int goods_id;
		String title;
		List<ImageUrl> imgs;
	}

	public String getUrlJsonString() {
		GoodsUpdate goodsUpdate = new GoodsUpdate();
		Gson gson = new Gson();
		// String jsonImgUrl = gson.toJson(imgUrlArr);
		// NLog.e(TAG, "%s", jsonImgUrl);
		// JSONObject jsonObject = new JSONObject();
		// try {
		// jsonObject.put("goods_id", mBaseShopInfo.getGoodsID());
		// jsonObject.put("title", mBaseShopInfo.getGoodsTitle());
		// jsonObject.put("imgs", jsonImgUrl);
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		goodsUpdate.goods_id = mGoodsInfo.getGoodsID();
		goodsUpdate.title = mGoodsInfo.getGoodsTitle();
		goodsUpdate.imgs = imgUrlArr;
		String jsonObject = gson.toJson(goodsUpdate);
		NLog.e(TAG, "111%s", jsonObject.toString());
		return jsonObject.toString();
	}
}
