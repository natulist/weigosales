package com.weigo.sales.activity.goods;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.app.framework.network.HostNameResolver;
import com.app.framework.util.DeviceManager;
import com.weigo.sales.R;
import com.weigo.sales.widget.AsyncRoundedImageView;


/**
 * @Description:
 * @author yingjie.lin
 * @date 2014年12月11日 上午10:12:56
 * @copyright TCL-MIE
 */

public class GridImageViewAdapter extends BaseAdapter {
    Context mContext;
    List<ImageInfo> mImageInfos;
    int imageNum;

    public GridImageViewAdapter(Context context, List<ImageInfo> imageInfos, int num) {
        mContext = context;
        mImageInfos = imageInfos;
        imageNum = num;
    }

    @Override
    public int getCount() {
        if (imageNum == 0) {
            return mImageInfos.size();
        }
        if (mImageInfos.size() > imageNum) {
            return imageNum;
        }
        return mImageInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AsyncRoundedImageView imageView;
        if (convertView == null) {
            imageView = new AsyncRoundedImageView(mContext);
            int px = DeviceManager.dip2px(mContext, 74);
            imageView.setLayoutParams(new GridView.LayoutParams(px, px));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (AsyncRoundedImageView) convertView;
        }

        if (mImageInfos.get(position).url != null) {
            imageView.setImageResource(R.drawable.ic_launcher);
            imageView.displayImage(HostNameResolver.resovleURL(mImageInfos.get(position).url));
        }else{
            imageView.setImageResource(R.drawable.add);
        }
        return imageView;
    }

}
