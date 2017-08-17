package com.cheng315.nfc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.cheng315.nfc.entity.AppVersion;
import com.cheng315.nfc.utils.AppUtil;
import com.cheng315.nfc.utils.LogUtils;
import com.efeiyi.BaseActivity;
import com.efeiyi.Constant;
import com.efeiyi.net.HttpRequest;
import com.efeiyi.net.RequestCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by YangZhenjie on 2016/11/3.
 */
public class SplashActivity extends BaseActivity {

    private Uri uri;
    private Bundle bundle;
    private Handler handler;
    private String apkUrl;
    private boolean isUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }


    private void checkVersion() {
        String version_id = "V" + AppUtil.getVersionCode();
        String version_mini = AppUtil.getVersionName();
        String version_code = version_id + "_" + version_mini;

        LogUtils.d("获取的版本请求参数" + version_code);

        HttpRequest.get(Constant.netUrl + "appVersion/upgrade?versionCode=" + version_code, new RequestCallback<AppVersion>() {

            @Override
            public void onResponse(List<AppVersion> entityList) {
                apkUrl = entityList.get(0).getApkUrl();
                isUpdate = true;
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onEmpty() {
            }

            @Override
            public void onError(Call call, int code) {
                super.onError(call, code);
                if (code == 7) {
                    isUpdate = false;
                }
            }
        });
    }

}
