package com.weigo.sales.activity.image;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.tcl.common.imageloader.core.ImageLoader;
import com.weigo.sales.activity.goods.ImageInfo;
import com.weigo.sales.data.ShopSingleton;

public class PictureLoader extends AsyncTaskLoader<List<String>> {
	private List<String> dataResult;
	private boolean dataIsReady;
	private static final String PICTURE = "pics";

	public PictureLoader(Context context) {
		super(context);
		if (dataIsReady) {
			deliverResult(dataResult);
		} else {
			forceLoad();
		}
	}

	@Override
	public List<String> loadInBackground() {
		List<String> list = new ArrayList<String>();
		List<ImageInfo> info = ShopSingleton.getInstance().imageInfos;
		list.add(ImageLoader.getInstance().getDiscCache().get(info.get(ShopSingleton.getInstance().index).url).getAbsolutePath());
		for (int i = 0; i < info.size(); i++) {
			if (i != ShopSingleton.getInstance().index) {
				list.add(ImageLoader.getInstance().getDiscCache().get(info.get(i).url).getAbsolutePath());
			}
		}
		// try {
		// ImageLoader.getInstance().getDiscCache().get("").getAbsolutePath()
		// String[] flLists = getContext().getAssets().list(PICTURE);
		// for (String file : flLists) {
		// if (file.endsWith(".jpg") || file.endsWith(".png")) {
		// list.add(ImageCacheManager.ASSETS_PATH_PREFIX + PICTURE
		// + "/" + file);
		// }
		// }
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return list;
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
	}

	@Override
	protected void onStartLoading() {
		// 显示加载条
		Logger.LOG(this, "onStartLoading");
		super.onStartLoading();
	}

	@Override
	protected void onStopLoading() {
		// 隐藏加载条
		Logger.LOG(this, "onStopLoading");
		super.onStopLoading();
	}

	@Override
	public boolean takeContentChanged() {

		return super.takeContentChanged();
	}

}
