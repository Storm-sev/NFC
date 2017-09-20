package com.cheng315.nfc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheng315.nfc.R;
import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.utils.ImageLoaderUtils;
import com.efeiyi.Constant;
import com.efeiyi.base.BaseNfcActivity;
import com.efeiyi.utils.DateUtil;
import java.util.Date;

/**
 * Created by YangZhenjie on 2016/10/26.
 */
public class WorkInfoActivity extends BaseNfcActivity {

    private TextView title;
    private TextView goodsName;
    private TextView masterName;
    private TextView creatTime;
    private ImageView workImage;
    private ImageView back;

    @Override
    protected void initData() {

        Product mProduct = (Product) getIntent().getSerializableExtra("mProduct");

        ImageLoaderUtils.loadImage(this, workImage, Constant.netImageUrl + mProduct.getLogo());

        goodsName.setText(mProduct.getName());
        masterName.setText(mProduct.getMasterName());
        creatTime.setText(DateUtil.date2yyyyMM(new Date(mProduct.getMadeYear())));

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_work_info;
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
        workImage = (ImageView) findViewById(R.id.iv_work);
        goodsName = (TextView) findViewById(R.id.goods_name);
        masterName = (TextView) findViewById(R.id.master_name);
        creatTime = (TextView) findViewById(R.id.creat_time);
        back = (ImageView) findViewById(R.id.back);

        title.setText("作品信息");
        goodsName.setText("苏绣—颜伯龙花鸟小品");
        masterName.setText("姚惠芬");
        creatTime.setText("2015年6月");

    }


    /**
     * 启动自己
     */
    public static void startSelf(Context context, Product mProduct) {

        Intent intent = new Intent(context, WorkInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("mProduct", mProduct);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


}
