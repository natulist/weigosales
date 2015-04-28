package com.weigo.sales.activity.share;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.weigo.sales.activity.goods.ImageInfo;

public class GoodsInfo implements Parcelable{

	protected int shop_id;
	protected int goods_id = 0;
	protected String goods_title;
	protected int can_edit;

	protected int main_img_num;
	public List<ImageInfo> imageInfos;

	public int getGoodsID() {
		return goods_id;
	}

	public void setGoodsID(int goods_id) {
		this.goods_id = goods_id;
	}



	public String getGoodsTitle() {
		return goods_title;
	}

	public void setGoodsTitle(String goods_title) {
		this.goods_title = goods_title;
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
		if (!(obj instanceof GoodsInfo))
			return false;
		GoodsInfo other = (GoodsInfo) obj;
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
		dest.writeString(goods_title);
		dest.writeInt(shop_id);
		dest.writeTypedList(imageInfos);
	}
	
	public static final Parcelable.Creator<GoodsInfo> CREATOR = new Parcelable.Creator<GoodsInfo>() {

		@Override
		public GoodsInfo createFromParcel(Parcel source) {
			GoodsInfo info = new GoodsInfo();
			info.goods_id = source.readInt();
			info.goods_title = source.readString();
			info.shop_id = source.readInt();
			info.imageInfos = new ArrayList<ImageInfo>();
			source.readTypedList(info.imageInfos, ImageInfo.CREATOR);
			return info;
		}

		@Override
		public GoodsInfo[] newArray(int size) {
			return new GoodsInfo[size];
		}
	};
}