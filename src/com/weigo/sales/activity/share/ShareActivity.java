package com.weigo.sales.activity.share;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.app.framework.notification.NotificationCenter;
import com.app.framework.notification.TopicSubscriber;
import com.tcl.common.imageloader.core.ImageLoader;
import com.weigo.base.http.JsonLoader;
import com.weigo.sales.Constants;
import com.weigo.sales.R;
import com.weigo.sales.SHContext;
import com.weigo.sales.activity.base.BaseTitleActivity;
import com.weigo.sales.activity.goods.BaseShopInfo;
import com.weigo.sales.activity.goods.GridImageViewAdapter;
import com.weigo.sales.activity.goods.ImageInfo;
import com.weigo.sales.data.ShopSingleton;

/**
 * @Description: 分享页
 * @author yingjie.lin
 * @date 2014年12月11日 下午2:57:55
 * @copyright TCL-MIE
 */

public class ShareActivity extends BaseTitleActivity {
    private static final String TAG = ShareActivity.class.getSimpleName();
    BaseShopInfo mBaseShopInfo;
    EditText ed_msg;
    private ShareProvider mJsonProvider;
    private JsonLoader mJsonLoader;     
    GridImageViewAdapter adapter;
    GridView gridView;
    private CheckBox cb_link;
    private boolean addLink;
//    public GoodsInfo goodsInfo;
    private List<ImageInfo> localImgs = new ArrayList<ImageInfo>();

    @Override
    protected void onContentCreate(Bundle savedInstanceState, View content) {
        mBaseShopInfo = ShopSingleton.getInstance().mBaseShopInfo;
        TextView tv_title = (TextView) content.findViewById(R.id.share_title);
        tv_title.setText("分享到朋友圈");

        ed_msg = (EditText) content.findViewById(R.id.msg_input);
        ed_msg.setText(mBaseShopInfo.getGoodsTitle());
        cb_link = (CheckBox)content.findViewById(R.id.cb_link);
        gridView = (GridView) content.findViewById(R.id.share_grid);
        // TODO:显示图片


        // mJsonProvider = new ShareProvider(mBaseShopInfo.getGoodsID(), true);
        // mJsonLoader = new JsonLoader(mJsonProvider);
         NotificationCenter.defaultCenter().subscriber("update_img", mSubscriber);
        // mJsonLoader.load();
        localImgs.clear();
        for (int i = 0; i < mBaseShopInfo.imageInfos.size(); i++) {
            ImageInfo tmp = new ImageInfo();
            tmp.height = mBaseShopInfo.imageInfos.get(i).height;
            tmp.width = mBaseShopInfo.imageInfos.get(i).width;
            tmp.id = mBaseShopInfo.imageInfos.get(i).id;
            tmp.url = mBaseShopInfo.imageInfos.get(i).url;
            localImgs.add(tmp);
        }

        ImageInfo add = new ImageInfo();
        localImgs.add(add);
        adapter = new GridImageViewAdapter(SHContext.getInstance().getApplicationContext(), localImgs, 10);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= localImgs.size() - 1) {
                    // TODO:進入編輯
                    // localImgs.remove(localImgs.size()-1);
                    Intent intent1 = new Intent(Constants.intent.EDIT);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // ShopSingleton.getInstance().mBaseShopInfo = mAppInfo;
//                    ShopSingleton.getInstance().imageInfos = mBaseShopInfo.imageInfos;

                    view.getContext().startActivity(intent1);
                    return;
                }
                Intent intent = new Intent(Constants.intent.VIEW_PIC);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // intent.putExtra("postion", position);
                // intent.putExtra("data", mAppInfo);
                // ShopSingleton.getInstance().mBaseShopInfo = mAppInfo;
                // localImgs.remove(localImgs.size()-1);
//                ShopSingleton.getInstance().imageInfos = mBaseShopInfo.imageInfos;
                ShopSingleton.getInstance().index = position;
                view.getContext().startActivity(intent);
            }
        });
        
        
        cb_link.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addLink = isChecked;
            }
        });

    }

    public void updateImg(List<ImageInfo> imgs) {
        localImgs.clear();
        for (int i = 0; i < imgs.size(); i++) {
            ImageInfo tmp = new ImageInfo();
            tmp.height = imgs.get(i).height;
            tmp.width = imgs.get(i).width;
            tmp.id = imgs.get(i).id;
            tmp.url = imgs.get(i).url;
            localImgs.add(tmp);
        }
    }

    private TopicSubscriber<List<ImageInfo>> mSubscriber = new TopicSubscriber<List<ImageInfo>>() {

        @Override
        public void onEvent(String topic, List<ImageInfo> data) {
            localImgs.clear();
            ShopSingleton.getInstance().imageInfos.clear();
//            ShopSingleton.getInstance().imageInfos.clear();
            for (int i = 0; i < data.size(); i++){
                if (data.get(i).select){
                    ImageInfo tmp = new ImageInfo();
                    tmp.height = data.get(i).height;
                    tmp.width = data.get(i).width;
                    tmp.id = data.get(i).id;
                    tmp.url = data.get(i).url;
                    localImgs.add(tmp);
                    ShopSingleton.getInstance().imageInfos.add(tmp);
                    
                }
            }
            ImageInfo add = new ImageInfo();
            localImgs.add(add);
            adapter.notifyDataSetChanged();
        }};

    @Override
    protected int getContentLayout() {
        return R.layout.activity_share;
    }

    @Override
    protected void initTitle() {
        // TODO Auto-generated method stub
        super.initTitle();
        setTitleText(R.string.share_title);
        enableMenu("发送", new OnClickListener() {

            @Override
            public void onClick(View v) {
//                if (ShopSingleton.getInstance().imageInfos == null) {
//                    return;
//                }
                shareToWechatFriends();
            }
        });
    }

    private ArrayList<Uri> getUriListForImages() {

        ArrayList<Uri> myList = new ArrayList<Uri>();
        for (int i = 0; i < ShopSingleton.getInstance().imageInfos.size(); i++) {
            File file = ImageLoader.getInstance().getDiscCache().get(ShopSingleton.getInstance().imageInfos.get(i).url);
            myList.add(Uri.fromFile(file));
        }
        return myList;
    }

    private void shareToWechatFriends() {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");
        if (addLink){
            intent.putExtra("Kdescription", ed_msg.getText().toString() +" 购买请猛击："+"http://www.baidu.com");
        }else{
            intent.putExtra("Kdescription", ed_msg.getText().toString());
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, getUriListForImages());
        // intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().unsubscribe("update_img", mSubscriber);
    }
}
