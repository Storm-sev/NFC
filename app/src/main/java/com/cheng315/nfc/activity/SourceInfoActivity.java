package com.cheng315.nfc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cheng315.nfc.R;
import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.entity.TenantProductSeriesImg;
import com.cheng315.nfc.utils.ImageLoaderUtils;
import com.cheng315.nfc.view.ListViewForScrollView;
import com.cheng315.nfc.viewholder.ViewHolder;
import com.efeiyi.Constant;
import com.efeiyi.base.BaseNfcActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangZhenjie on 2016/10/26.
 */
public class SourceInfoActivity extends BaseNfcActivity {


    private static final String TAG = "SourceInfoActivity";

    private TextView title;
    private TextView workTitle;
    private ImageView workImage;
    private TextView creatRegion;
    private TextView creatCraft;
    private TextView participants;
    private ImageView back;
    private ListViewForScrollView list_view;
    private ScrollView scroll_view;
    private List<TenantProductSeriesImg> urlList;
    private MyAdapter adapter;

    @Override
    protected void initData() {
        adapter = new MyAdapter();
        list_view.setAdapter(adapter);

        Product mProduct = (Product) getIntent().getSerializableExtra("mProduct");
        urlList = new ArrayList<>();
        urlList.clear();
        urlList = mProduct.getTenant().getTenantProductSeriesList().get(0).getImgList();
        adapter.notifyDataSetChanged();
        if (mProduct.getName() != null) workTitle.setText(mProduct.getName());
        if (mProduct.getTenant() != null && mProduct.getTenant().getTenantProductSeriesList() != null && mProduct.getTenant().getTenantProductSeriesList().get(0).getRegion() != null)
            creatRegion.setText(mProduct.getTenant().getTenantProductSeriesList().get(0).getRegion());
        if (mProduct.getTenant() != null && mProduct.getTenant().getTenantProductSeriesList() != null && mProduct.getTenant().getTenantProductSeriesList().get(0).getRegion() != null)
            creatCraft.setText(mProduct.getTenant().getTenantProductSeriesList().get(0).getCraft());
        if (mProduct.getMasterName() != null)
            participants.setText(mProduct.getMasterName());
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_source_info;
    }

    @Override
    protected void setUpListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        title = (TextView) findViewById(R.id.tv_title);
        workTitle = (TextView) findViewById(R.id.tv_work_title);
        creatRegion = (TextView) findViewById(R.id.creat_region);
        creatCraft = (TextView) findViewById(R.id.creat_technology);
        participants = (TextView) findViewById(R.id.participants);
        back = (ImageView) findViewById(R.id.back);

        list_view = (ListViewForScrollView) findViewById(R.id.list_view);
        scroll_view = (ScrollView) findViewById(R.id.scrollview);
        title.setText("溯源信息");


    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return urlList == null ? 0 : urlList.size();

        }

        @Override
        public Object getItem(int position) {
            return urlList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(SourceInfoActivity.this, R.layout.item, null);
                ImageView imageView
                        = ViewHolder.getView(convertView, R.id.iv_work);
                ImageLoaderUtils.loadImage(SourceInfoActivity.this, imageView, Constant.netImageUrl + urlList.get(position).getImgUrl());
            }
            return convertView;
        }

    }


    /**
     * 启动自己
     */
    public static void startSelf(Context context, Product mProduct) {

        Intent intent = new Intent(context, SourceInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mProduct", mProduct);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
