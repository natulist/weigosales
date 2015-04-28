package com.weigo.sales.activity.goods;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseShopInfo implements Parcelable{

	protected String shop_name;
	protected int shop_id;
	protected String shop_icon;
	protected int goods_id = 0;
	protected String goods_title;
	protected String goods_price;
	protected String goods_add_time;
	protected String goods_sku_url;
	public List<ImageInfo> imageInfos;

	public int getGoodsID() {
		return goods_id;
	}

	public void setGoodsID(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getShopName() {
		return shop_name;
	}

	public void setShopName(String shop_name) {
		this.shop_name = shop_name;
	}

	public String getShopIcon() {
		return shop_icon;
	}
	
	public void setShopIcon(String shop_icon){
		this.shop_icon = shop_icon;
	}


	public String getGoodsTitle() {
		return goods_title;
	}

	public void setGoodsTitle(String goods_title) {
		this.goods_title = goods_title;
	}

	public String getGoodsPrice() {
		return goods_price;
	}

	public void setGoodsPrice(String goods_price) {
		this.goods_price = goods_price;
	}

	public String getGoodsSkuUrl() {
		return goods_sku_url;
	}

	public void setGoodsSkuUrl(String goods_sku_url) {
		this.goods_sku_url = goods_sku_url;
	}

	public String getGoodsAddTime() {
		return goods_add_time;
	}

	public void setGoodsAddTime(String goods_add_time) {
		this.goods_add_time = goods_add_time;
	}


	public int getShopID() {
		return shop_id;
	}

	public void setShopID(int shop_id) {
		this.shop_id = shop_id;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BaseShopInfo))
			return false;
		BaseShopInfo other = (BaseShopInfo) obj;
		if (goods_id != other.goods_id)
			return false;
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(goods_id);
		dest.writeString(shop_name);
		dest.writeString(shop_icon);
		dest.writeString(goods_title);
		dest.writeString(goods_price);
		dest.writeString(goods_add_time);
		dest.writeString(goods_sku_url);
		dest.writeInt(shop_id);
		dest.writeTypedList(imageInfos);
	}
	
	public static final Parcelable.Creator<BaseShopInfo> CREATOR = new Parcelable.Creator<BaseShopInfo>() {

		@Override
		public BaseShopInfo createFromParcel(Parcel source) {
			BaseShopInfo info = new BaseShopInfo();
			info.goods_id = source.readInt();
			info.shop_name = source.readString();
			info.shop_icon = source.readString();
			info.goods_title = source.readString();
			info.goods_price = source.readString();
			info.goods_add_time = source.readString();
			info.goods_sku_url = source.readString();
			info.shop_id = source.readInt();
			info.imageInfos = new ArrayList<ImageInfo>();
			source.readTypedList(info.imageInfos, ImageInfo.CREATOR);
			return info;
		}

		@Override
		public BaseShopInfo[] newArray(int size) {
			return new BaseShopInfo[size];
		}
	};
}