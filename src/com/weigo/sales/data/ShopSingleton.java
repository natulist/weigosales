package com.weigo.sales.data;

import java.util.List;

import android.R.integer;

import com.weigo.sales.activity.goods.BaseShopInfo;
import com.weigo.sales.activity.goods.ImageInfo;


/** 
 * @Description: 
 * @author yingjie.lin 
 * @date 2014年12月16日 下午7:51:54 
 * @copyright TCL-MIE
 */

public class ShopSingleton {

	public int index;
	public BaseShopInfo mBaseShopInfo; //當前寶貝信息
	public List<ImageInfo> imageInfos; //当前显示的宝贝圖片
	
	private static class SingletonHolder{
		static final ShopSingleton INSTANCE = new ShopSingleton();
	}
	
	public static ShopSingleton getInstance(){
		return SingletonHolder.INSTANCE;
	}
	
}
