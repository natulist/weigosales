package com.weigo.sales.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.tcl.common.imageloader.core.DisplayImageOptions;
import com.tcl.common.imageloader.core.DisplayImageOptions.Builder;
import com.tcl.common.imageloader.core.ImageLoader;
import com.tcl.common.imageloader.core.assist.ImageScaleType;
import com.tcl.common.imageloader.core.listener.ImageLoadingListener;
import com.weigo.sales.R;

public class AsyncRoundedImageView extends RoundedImageView {
	
	private Drawable mImageOnLoading;
	private Drawable mImageOnFail;
	private Drawable mImageForEmpty;

	private boolean hadLoaded;
	
	public AsyncRoundedImageView(Context context) {
		super(context);
	}
	
	public AsyncRoundedImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, 
				R.styleable.AsyncImageView); 
		
		mImageForEmpty = a.getDrawable(R.styleable.AsyncImageView_image_for_empty_url);
		mImageOnLoading = a.getDrawable(R.styleable.AsyncImageView_image_on_loading);
		mImageOnFail = a.getDrawable(R.styleable.AsyncImageView_image_on_fail);
		
		a.recycle();
	}
	
	public void setImageForEmptyUrl(Drawable drawable) {
		mImageForEmpty = drawable;
	}
	
	public void setImageOnLoading(Drawable drawable) {
		mImageOnLoading = drawable;
	}
	
	public void setImageOnFail(Drawable drawable) {
		mImageOnFail = drawable;
	}
	
	public void displayImage(String uri) {
		displayImage(uri, null, null);
	}
	
	public void displayImage(String uri, ImageLoadingListener listener) {
		displayImage(uri, null, listener);
	}
	
    //异步显示图片
	public void displayImage(String uri, DisplayImageOptions options, ImageLoadingListener listener) {
		if (uri == null) {
			return;
		}
		if (null == options) {
			options = getOptions();
		}
		hadLoaded = true;
		ImageLoader.getInstance().displayImage(uri, this, options, listener);
	}
	
	private DisplayImageOptions getOptions() {
		DisplayImageOptions.Builder builder = new Builder();
		builder.bitmapConfig(Bitmap.Config.RGB_565);
		builder.cacheInMemory(true);
		builder.cacheOnDisc(true);
		builder.imageScaleType(ImageScaleType.EXACTLY);
		if (mImageOnLoading != null && !hadLoaded) {
			builder.showImageOnLoading(mImageOnLoading);
		}
		if (mImageOnFail != null) {
			builder.showImageOnFail(mImageOnFail);
		}
		if (mImageForEmpty != null) {
			builder.showImageForEmptyUri(mImageForEmpty);
		}

		return builder.build();
	}

}
