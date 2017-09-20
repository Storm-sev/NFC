package com.cheng315.nfc.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.cheng315.nfc.R;
import com.efeiyi.base.BaseNfcActivity;

/**
 * Created by YangZhenjie on 2016/10/26.
 */
public class CertificateFailed extends BaseNfcActivity {

    private ImageView back;


    @Override
    protected void initViews() {
        back = (ImageView) findViewById(R.id.back);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_certificate_failed;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            // startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 启动自己
     */
    public static void startSelf(Context context) {

        Intent intent = new Intent(context, CertificateFailed.class);
        context.startActivity(intent);
    }
}
