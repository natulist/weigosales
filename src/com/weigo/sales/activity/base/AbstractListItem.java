package com.weigo.sales.activity.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractListItem {
	
	public final static int ITEM_TYPE_APP = 0;
	public final static int ITEM_TYPE_GALLERY = 1;
	public final static int ITEM_TYPE_BANNER = 2;
	public final static int ITEM_TYPE_SUBJECT = 3;

	public abstract View getView(Context context, View convertView, ViewGroup root);
	public void onViewAttached(View view) {}
	public boolean needAttachTo() {
		return false;
	}
	public void refresh() {}
}
