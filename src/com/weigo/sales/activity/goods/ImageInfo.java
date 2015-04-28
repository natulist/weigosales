package com.weigo.sales.activity.goods;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description:
 * @author yingjie.lin
 * @date 2014年12月10日 下午8:26:23
 * @copyright TCL-MIE
 */

public class ImageInfo implements Parcelable {
	public int id = 0;
	public int width = 0;
	public int height = 0;
	public String url;
	
	public boolean select;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(width);
		dest.writeInt(height);
		dest.writeString(url);
	}

	public static final Parcelable.Creator<ImageInfo> CREATOR = new Parcelable.Creator<ImageInfo>() {

		@Override
		public ImageInfo createFromParcel(Parcel source) {
			ImageInfo imageInfo = new ImageInfo();
			imageInfo.id = source.readInt();
			imageInfo.width = source.readInt();
			imageInfo.height = source.readInt();
			imageInfo.url = source.readString();
			return null;
		}

		@Override
		public ImageInfo[] newArray(int size) {
			return new ImageInfo[size];
		}

	};
}
