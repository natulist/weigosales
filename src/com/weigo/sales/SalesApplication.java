package com.weigo.sales;

import android.app.Application;

import com.app.framework.log.Logger;
import com.app.framework.log.NLog;
import com.app.framework.network.NetworkHelper;
import com.tcl.common.imageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.tcl.common.imageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.tcl.common.imageloader.core.ImageLoader;
import com.tcl.common.imageloader.core.ImageLoaderConfiguration;
import com.tcl.common.imageloader.core.assist.QueueProcessingType;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.weigo.base.utils.DirType;
import com.weigo.base.utils.TUncaughtExceptionHandler;

/**
 * @Description:
 * @author yingjie.lin
 */

public class SalesApplication extends Application {
	private static final String TAG = SalesApplication.class.getSimpleName();
	public static IWXAPI WXapi;
	@Override
	public void onCreate() {
		super.onCreate();

		// 抓取崩溃日志
		SHContext.initInstance(this);
		String path = SHContext.getDirectoryPath(DirType.crash);
		TUncaughtExceptionHandler.catchUncaughtException(getApplicationContext(), path);

		NLog.setDebug(true, Logger.VERBOSE);

//		 写Log文件
//		 String logpath = SHContext.getDirectoryPath(DirType.log);
//		 NLog.trace(Logger.TRACE_OFFLINE, logpath);
		
		
		WXapi = WXAPIFactory.createWXAPI(this, AppConfig.WX_APP_ID, true);
		WXapi.registerApp(AppConfig.WX_APP_ID);
		
		NetworkHelper.sharedHelper().registerNetworkSensor(getApplicationContext());
		
		initImageLoader();
		// crash处理
		// CrashExceptionHandler.getInstance().init(this);
		// weather:initData()需要访问网络状态
		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext

		// Utils.initDataFromRaw(this, R.raw.data, Constants.DB_ADDR);
	}

	void initImageLoader ()
	{
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
		builder.denyCacheImageMultipleSizesInMemory();
		builder.memoryCache(new LRULimitedMemoryCache(24 * 1024 * 1024));
		builder.discCache(new TotalSizeLimitedDiscCache(SHContext.getDirectory(DirType.image), 50 * 1024 * 1024));
		builder.tasksProcessingOrder(QueueProcessingType.LIFO);
		ImageLoader.getInstance().init(builder.build());
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
}
