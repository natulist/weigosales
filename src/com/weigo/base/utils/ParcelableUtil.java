package com.weigo.base.utils;

import android.os.Parcel;
import android.os.Parcelable;

/** 
 * @Description: Parcelable对象序列化操作
 * @author wenbiao.xie 
 * @date 2014年9月29日 下午2:18:04 
 * @copyright TCL-MIE
 */

public class ParcelableUtil {

	public static byte[] marshall(Parcelable parceable) {
        Parcel parcel = Parcel.obtain();
        parceable.writeToParcel(parcel, 0);
        byte[] bytes = parcel.marshall();
        parcel.recycle(); // not sure if needed or a good idea
        return bytes;
    }

    public static Parcel unmarshall(byte[] bytes) {
        Parcel parcel = Parcel.obtain();
        parcel.unmarshall(bytes, 0, bytes.length);
        parcel.setDataPosition(0); // this is extremely important!
        return parcel;
    }

    public static <T> T unmarshall(byte[] bytes, Parcelable.Creator<T> creator) {
        Parcel parcel = unmarshall(bytes);
        return creator.createFromParcel(parcel);
    }
}
