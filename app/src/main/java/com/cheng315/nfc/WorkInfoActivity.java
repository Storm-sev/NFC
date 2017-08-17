package com.cheng315.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng315.nfc.entity.Product;
import com.efeiyi.BaseActivity;
import com.efeiyi.Constant;
import com.efeiyi.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.Date;

/**
 * Created by YangZhenjie on 2016/10/26.
 */
public class WorkInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView title;
    private TextView goodsName;
    private TextView masterName;
    private TextView creatTime;
    private ImageView workImage;
    private ImageView back;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_info);
        title = (TextView) findViewById(R.id.tv_title);
        workImage = (ImageView) findViewById(R.id.iv_work);
        goodsName = (TextView) findViewById(R.id.goods_name);
        masterName = (TextView) findViewById(R.id.master_name);
        creatTime = (TextView) findViewById(R.id.creat_time);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);

        title.setText("作品信息");
        goodsName.setText("苏绣—颜伯龙花鸟小品");
        masterName.setText("姚惠芬");
        creatTime.setText("2015年6月");

        Product mProduct = (Product) getIntent().getSerializableExtra("mProduct");

        Picasso.with(this).load(Constant.netImageUrl + mProduct.getLogo()).placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(workImage);

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        goodsName.setText(mProduct.getName());
        masterName.setText(mProduct.getMasterName());
        creatTime.setText(DateUtil.date2yyyyMM(new Date(mProduct.getMadeYear())));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
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
}
