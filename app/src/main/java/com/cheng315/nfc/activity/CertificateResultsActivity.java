package com.cheng315.nfc.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheng315.nfc.R;
import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.utils.ImageLoaderUtils;
import com.cheng315.nfc.utils.LogUtils;
import com.efeiyi.Constant;
import com.efeiyi.base.BaseNfcActivity;
import com.efeiyi.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by YangZhenjie on 2016/10/26.
 * NFC扫描结果
 */
public class CertificateResultsActivity extends BaseNfcActivity {


    private static final String TAG = "CertificateResultsActivity";

    private ImageView ivWork;
    private ImageView back;
    private TextView workTitle;
    private TextView project;
    private TextView businessName;
    private TextView creatTime;
    private TextView workId;
    private TextView certificationLetter;
    private TextView certificationOrganization;
    private TextView certificationTime;
    private RelativeLayout workInfo;
    private RelativeLayout source;
    private Product mProduct;

    @Override
    protected void setUpListener() {

        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toSoureInfoActivity();

            }
        });

        workInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toWorkInfoActivity();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.d(TAG, "点击事件的监听");
                finish();
            }
        });


    }


    @Override
    protected void initViews() {
        ivWork = (ImageView) findViewById(R.id.iv_work);
        back = (ImageView) findViewById(R.id.back);
        workTitle = (TextView) findViewById(R.id.tv_work_title);
        project = (TextView) findViewById(R.id.project);
        businessName = (TextView) findViewById(R.id.business_name);
        creatTime = (TextView) findViewById(R.id.creat_time);
        workId = (TextView) findViewById(R.id.work_id);
        certificationLetter = (TextView) findViewById(R.id.certification_letter);
        certificationOrganization = (TextView) findViewById(R.id.certification_organization);
        certificationTime = (TextView) findViewById(R.id.certification_time);
        workInfo = (RelativeLayout) findViewById(R.id.work_info);
        source = (RelativeLayout) findViewById(R.id.source);
    }

    @Override
    protected void initData() {
        mProduct = ((List<Product>) getIntent().getSerializableExtra("mProduct")).get(0);
        if (mProduct.getName() != null)
            workTitle.setText(mProduct.getName());
//        Glide.with(this).load("http://master3.efeiyi.com/" + mProduct.getLogo()).placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(ivWork);
//        Picasso.with(this).load("http://master3.efeiyi.com/" + mProduct.getLogo()).placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(ivWork);

        // 图片请求框架 glide
        ImageLoaderUtils.loadImage(this, ivWork, Constant.netImageUrl + mProduct.getLogo());



        if (mProduct.getProductSeries() != null && mProduct.getProductSeries().getName() != null)
            project.setText(mProduct.getProductSeries().getName());
        if (mProduct.getTenant() != null && mProduct.getTenant().getName() != null)
            businessName.setText(mProduct.getTenant().getName());
        if (mProduct.getMadeYear() != 0)
            creatTime.setText(DateUtil.date2yyyyMM(new Date(mProduct.getMadeYear())));
        if (mProduct.getSerial() != null)
            workId.setText(mProduct.getSerial());
        if (mProduct.getTenant() != null && mProduct.getTenant().getTenantCertificationList() != null && mProduct.getTenant().getTenantCertificationList().get(0).getName() != null)
            certificationLetter.setText(mProduct.getTenant().getTenantCertificationList().get(0).getName());
        if (mProduct.getTenant() != null && mProduct.getTenant().getTenantCertificationList() != null && mProduct.getTenant().getTenantCertificationList().get(0).getOrg() != null)
            certificationOrganization.setText(mProduct.getTenant().getTenantCertificationList().get(0).getOrg());
        if (mProduct.getTenant() != null && mProduct.getTenant().getTenantCertificationList() != null && mProduct.getTenant().getTenantCertificationList().get(0).getTheDate() != 0)
            certificationTime.setText(DateUtil.date2yyyyMM(new Date(mProduct.getTenant().getTenantCertificationList().get(0).getTheDate())));



    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_certification_results;
    }


    /**
     * 跳转 sourceInfo
     */
    private void toSoureInfoActivity() {
        SourceInfoActivity.startSelf(CertificateResultsActivity.this, mProduct);
    }


    /**
     * 跳转workinfo
     */
    private void toWorkInfoActivity() {
        WorkInfoActivity.startSelf(CertificateResultsActivity.this, mProduct);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static void startSelf(Context context, List<Product> entityList) {


        Intent intent = new Intent(context, CertificateResultsActivity.class);
        intent.putExtra("mProduct", (Serializable) entityList);
        context.startActivity(intent);

    }

}
