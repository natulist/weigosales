package com.weigo.base.utils;

import android.os.Looper;

/**
 * 
 * @author wenbiao.xie
 * 线程辅助类
 */
public class ThreadUtil {
	
	public static boolean isMainThread() {
		long id = Thread.currentThread().getId();
		return id == Looper.getMainLooper().getThread().getId();
	}

}
