package com.cheng315.nfc.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng315.nfc.R;
import com.cheng315.nfc.httpclient.HttpClientManager;
import com.cheng315.nfc.httpclient.OnCheckVersionListener;
import com.cheng315.nfc.httpclient.UpdateService;
import com.cheng315.nfc.utils.DialogHelper;
import com.efeiyi.Constant;
import com.efeiyi.base.BaseActivity;

/**
 * Created by YangZhenjie on 2016/11/18.
 */
public class AboutActivity extends BaseActivity {


    private static final String TAG = AboutActivity.class.getSimpleName();


    private static final int UPDATEAPP = 0;
    private static final int NOUPDATEAPP = 1;
    private TextView tvTitle;
    private ImageView back;
    private LinearLayout llCheckVersion;
    private boolean isUpdata;
    private String apkUrl;

    private DialogHelper mDialogHelper;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATEAPP:
                    showVersionUpdataDialog();
                    break;
                case NOUPDATEAPP:
                    showVersionNoUpdataDialog();
                    break;
            }
        }
    };


    @Override
    protected void initNfc() {

    }

    @Override
    protected void initViews() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        back = (ImageView) findViewById(R.id.back);
        llCheckVersion = (LinearLayout) findViewById(R.id.ll_version);
        tvTitle.setText("关于我们");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void setUpListener() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llCheckVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkVersion();//检测最新版本
            }
        });

    }

    // 版本检测
    private void checkVersion() {

        HttpClientManager.getInstance().checkVersion(Constant.CHECK_VERSION, new OnCheckVersionListener() {
            @Override
            public void onResponse(String url, boolean update) {
                apkUrl = url;
                Message msg = Message.obtain();
                msg.what = UPDATEAPP;
                handler.sendMessage(msg);

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onError(boolean update, int code) {

                if (code == 7) {
                    Message msg = Message.obtain();
                    msg.what = NOUPDATEAPP;
                    handler.sendMessage(msg);
                }

            }
        });


    }


    //
    private void showVersionNoUpdataDialog() {

        DialogHelper.showNoListenerDialog(this, "版本提示", "已经是最新版本");
    }

    private void showVersionUpdataDialog() {

        if (UpdateService.DOWNLOAD_CODE) {
            Toast.makeText(this, "正在下载新版本...", Toast.LENGTH_SHORT).show();
            return;
        }

        DialogHelper.showDialog(this, R.string.nfc_version_updata, new DialogHelper.DialogPositiveButtonListener() {
            @Override
            public void onDialogPositiveButtonListener() {

                if (apkUrl != null) {
                    UpdateService.startOrDownLoadApp(AboutActivity.this, apkUrl);
                    UpdateService.DOWNLOAD_CODE = true;
                }
            }

            @Override
            public void onDialogNegativeButtonListener() {


            }
        });

        return;
    }


//    private void downloadNewApp(String apkUrl) {
//
//        UpdateService.startOrDownLoadApp(this, apkUrl);
//
//
////        Intent intent = new Intent(this, UpdateService.class);
////        intent.putExtra("APK_URL", apkUrl);
////        startService(intent);
//    }


}
