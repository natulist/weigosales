package com.weigo.sales.activity.edit;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.framework.log.NLog;
import com.app.framework.network.HostNameResolver;
import com.weigo.sales.Constants;
import com.weigo.sales.R;
import com.weigo.sales.activity.goods.ImageInfo;
import com.weigo.sales.activity.upload.MyCheckBox;
import com.weigo.sales.data.ShopSingleton;
import com.weigo.sales.widget.AsyncRoundedImageView;

/**
 * @Description:
 * @author yingjie.lin
 * @date 2014年12月11日 上午10:12:56
 * @copyright TCL-MIE
 */

public class EditGridViewAdapter extends BaseAdapter {
    private static final String TAG = EditGridViewAdapter.class.getSimpleName();
    Context mContext;
    List<ImageInfo> mImageInfos;
    int imageNum;
    int imgSum;

    public EditGridViewAdapter(Context context, List<ImageInfo> imageInfos, int num) {
        mContext = context;
        mImageInfos = imageInfos;
        imageNum = num;
        imgSum = 0;
        NLog.e(TAG, "test:" + ShopSingleton.getInstance().imageInfos.size());
        for (int i = 0; i < mImageInfos.size(); i++) {
            mImageInfos.get(i).select = false;
            NLog.e(TAG, mImageInfos.get(i).id + "");
            for (int j = 0; j < ShopSingleton.getInstance().imageInfos.size(); j++) {
//                if (ShopSingleton.getInstance().imageInfos.get(j).id == mImageInfos.get(i).id) {
//                    mImageInfos.get(i).select = true;
//                    break;
//                }
                if (ShopSingleton.getInstance().imageInfos.get(j).url.equalsIgnoreCase(mImageInfos.get(i).url)){
                    imgSum++;
                    mImageInfos.get(i).select = true;
                    break;
                }
            }
            
            // if (i < ShopSingleton.getInstance().imageInfos.size()){
            // mImageInfos.get(i).height = ShopSingleton.getInstance().imageInfos.get(i).height;
            // mImageInfos.get(i).id = ShopSingleton.getInstance().imageInfos.get(i).id;
            // mImageInfos.get(i).url = ShopSingleton.getInstance().imageInfos.get(i).url;
            // mImageInfos.get(i).width = ShopSingleton.getInstance().imageInfos.get(i).width;
            // mImageInfos.get(i).select = true;
            // imgSum++;
            // }else{
            // mImageInfos.get(i).select = false;
            // }
        }
    }

    @Override
    public int getCount() {
        // if (imageNum == 0) {
        // return mImageInfos.size();
        // }
        // if (mImageInfos.size() > imageNum) {
        // return imageNum;
        // }
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
        final int localPosition = position;
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.edit_grid_item, null);
            holder.image = (AsyncRoundedImageView) convertView.findViewById(R.id.edit_img);
            holder.checkBox = (MyCheckBox) convertView.findViewById(R.id.edit_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == parent.getChildCount()) {
            holder.checkBox.position = position;
            NLog.e(TAG, "init onCheckedChanged:%s", position);
        }
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(mImageInfos.get(position).select);

        holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                MyCheckBox myCheckBox = (MyCheckBox) buttonView;
                NLog.e(TAG, "onCheckedChanged:%s", localPosition);
                if (imgSum >= imageNum && isChecked) {
                    buttonView.setChecked(false);
                    Toast.makeText(mContext, "最多设置" + imageNum + "张图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                mImageInfos.get(localPosition).select = isChecked;
                imgSum += isChecked ? 1 : -1;
                NLog.e(TAG, "imgSum:%d", imgSum);

            }
        });


        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.image.setPadding(8, 8, 8, 8);
        holder.image.setImageResource(R.drawable.ic_launcher);
        holder.image.displayImage(HostNameResolver.resovleURL(mImageInfos.get(position).url));

        holder.image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Constants.intent.VIEW_PIC);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // intent.putExtra("postion", position);
                // intent.putExtra("data", mAppInfo);
                ShopSingleton.getInstance().imageInfos = mImageInfos;
                ShopSingleton.getInstance().index = localPosition;
                NLog.e(TAG, "image:%d", localPosition);
                v.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        AsyncRoundedImageView image;
        MyCheckBox checkBox;
    }

}
