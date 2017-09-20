package com.cheng315.nfc.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.cheng315.nfc.R;
import com.cheng315.nfc.httpclient.HttpClientManager;
import com.cheng315.nfc.httpclient.OnCheckVersionListener;
import com.efeiyi.Constant;
import com.efeiyi.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by YangZhenjie on 2016/11/3.
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.splash_bg)
    ImageView splashBg;

    private Uri uri;
    private Bundle bundle;
    private Handler handler;
    private String apkUrl;
    private boolean isUpdate;


    @Override
    protected void initNfc() {

    }

    @Override
    protected void initViews() {

        loadSplashAnim();


    }

    /**
     *  splash animation
     */
    private void loadSplashAnim() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(1500);

        splashBg.startAnimation(alphaAnimation);

    }

    @Override
    protected void initData() {
        handler = new Handler();
        Intent intent = getIntent();
        uri = intent.getData();
        checkVersion();
        if (uri != null) {
            handler.postDelayed(new splashhandler(), 500);
        } else {
            handler.postDelayed(new splashhandler(), 2000);
        }

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void setUpListener() {

    }



    class splashhandler implements Runnable {
        public void run() {
            Intent intent = new Intent();
            if (uri != null) {
                intent = new Intent(getApplication(), MainFromNetActivity.class);
            } else {
                intent = new Intent(getApplication(), MainActivity.class);
            }
            intent.putExtra("isUpdate", isUpdate);
            intent.putExtra("apkUrl", apkUrl);
            startActivity(intent);
            SplashActivity.this.finish();

        }
    }

//    private String getHex(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (int i = bytes.length - 1; i >= 0; --i) {
//            int b = bytes[i] & 0xff;
//            if (b < 0x10)
//                sb.append('0');
//            sb.append(Integer.toHexString(b));
//        }
//        return sb.toString();
//    }


    /**
     * 检查版本
     */
    private void checkVersion() {


        HttpClientManager.getInstance().checkVersion(Constant.CHECK_VERSION, new OnCheckVersionListener() {
            @Override
            public void onResponse(String url, boolean update) {
                apkUrl = url;
                isUpdate = update;
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onError(boolean update, int code) {
                isUpdate = update;

            }
        });

    }

}
