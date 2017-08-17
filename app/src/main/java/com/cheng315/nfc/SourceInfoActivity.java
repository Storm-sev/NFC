package com.cheng315.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.entity.TenantProductSeriesImg;
import com.cheng315.nfc.view.AutoScaleImageView;
import com.cheng315.nfc.view.ListViewForScrollView;
import com.efeiyi.BaseActivity;
import com.efeiyi.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YangZhenjie on 2016/10/26.
 */
public class SourceInfoActivity extends BaseActivity implements View.OnClickListener {

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
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source_info);
        title = (TextView) findViewById(R.id.tv_title);
        workTitle = (TextView) findViewById(R.id.tv_work_title);
        creatRegion = (TextView) findViewById(R.id.creat_region);
        creatCraft = (TextView) findViewById(R.id.creat_technology);
        participants = (TextView) findViewById(R.id.participants);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title.setText("溯源信息");
        list_view = (ListViewForScrollView) findViewById(R.id.list_view);
        scroll_view = (ScrollView) findViewById(R.id.scrollview);

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

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }



    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return  urlList == null ? 0 : urlList.size();

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

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(SourceInfoActivity.this, R.layout.item, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (AutoScaleImageView) convertView.findViewById(R.id.iv_work);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            Glide.with(SourceInfoActivity.this).load(Constant.netImageUrl +urlList.get(position).getImgUrl()).placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(viewHolder.imageView);
            Picasso.with(SourceInfoActivity.this).load(Constant.netImageUrl +urlList.get(position).getImgUrl()).placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(viewHolder.imageView);
            System.out.println("==================================================" + position + "===============================================");
            return convertView;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        if (mAdapter != null) {
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }
    static class ViewHolder {
        AutoScaleImageView imageView;
    }
}
