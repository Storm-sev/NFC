package com.cheng315.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheng315.nfc.entity.Product;
import com.efeiyi.BaseActivity;
import com.efeiyi.utils.DateUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

/**
 * Created by YangZhenjie on 2016/10/26.
 */
public class CertificateResultsActivity extends BaseActivity implements View.OnClickListener {

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
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_certification_results);
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

        mProduct = ((List<Product>) getIntent().getSerializableExtra("mProduct")).get(0);
        if (mProduct.getName() != null)
            workTitle.setText(mProduct.getName());
//        Glide.with(this).load("http://master3.efeiyi.com/" + mProduct.getLogo()).placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(ivWork);
        Picasso.with(this).load("http://master3.efeiyi.com/" + mProduct.getLogo()).placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(ivWork);

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

        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mAdapter = NfcAdapter.getDefaultAdapter(this);

        workInfo.setOnClickListener(this);
        source.setOnClickListener(this);
        back.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                //
                // startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.work_info:
                Intent intent1 = new Intent(this, WorkInfoActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("mProduct", mProduct);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.source:
                Intent intent2 = new Intent(this, SourceInfoActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("mProduct", mProduct);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
        }

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

  /* @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
       intent = getIntent();
    }
*/

}
