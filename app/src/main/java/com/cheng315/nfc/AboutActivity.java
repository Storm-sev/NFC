package com.cheng315.nfc;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng315.nfc.entity.AppVersion;
import com.cheng315.nfc.utils.AppUtil;
import com.cheng315.nfc.utils.DialogHelper;
import com.efeiyi.BaseActivity;
import com.efeiyi.Constant;
import com.efeiyi.net.HttpRequest;
import com.efeiyi.net.NetRequestUtil;
import com.efeiyi.net.RequestCallback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by YangZhenjie on 2016/11/18.
 */
public class AboutActivity extends BaseActivity {


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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        setUpListener();
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        back = (ImageView) findViewById(R.id.back);
        llCheckVersion = (LinearLayout) findViewById(R.id.ll_version);
        tvTitle.setText("关于我们");

        mDialogHelper = new DialogHelper(this);


    }

    private void setUpListener() {
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

    private void checkVersion() {
        String version_id = "V" + AppUtil.getVersionCode();
        String version_mini = AppUtil.getVersionName();

        String version_code = version_id + "_" + version_mini;
        HttpRequest.get(Constant.netUrl + "appVersion/upgrade?versionCode=" + version_code, new RequestCallback<AppVersion>() {

            @Override
            public void onResponse(List<AppVersion> entityList) {
                apkUrl = entityList.get(0).getApkUrl();
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
            public void onError(Call call, int code) {
                super.onError(call, code);
                if (code == 7) {
                    Message msg = Message.obtain();
                    msg.what = NOUPDATEAPP;
                    handler.sendMessage(msg);
                }
            }
        });
    }


    private void showVersionNoUpdataDialog() {

        new AlertDialog.Builder(this)
                .setTitle("版本提示")
                .setMessage("已经是最新版本")
                .setPositiveButton("确定", null)
                .show();


    }

    private void showVersionUpdataDialog() {
//
        mDialogHelper.showDialog(R.string.nfc_version_updata);
        mDialogHelper.setDialogPositiveButtonListener(new DialogHelper.DialogPositiveButtonListener() {
            @Override
            public void onDialogPositiveButtonListener() {
                downloadNewApp(apkUrl);

            }
        });

        return;
    }

    private void downloadNewApp(String apkUrl) {

        final ProgressDialog progressDialogH
                = mDialogHelper.createProgressDialogH(null);

        progressDialogH.show();


        Map<String, String> paramsMap = new HashMap<>();
        NetRequestUtil.download(apkUrl, paramsMap, new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), AppUtil.getNameFromUrl(apkUrl)) {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(AboutActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(File response) {
                progressDialogH.dismiss();
                AppUtil.installApp(AboutActivity.this, response);
            }

            @Override
            public void inProgress(float progress, long total) {
                int mProgress = (int) (progress * 100);
                progressDialogH.setMax(100);
                progressDialogH.setProgress(mProgress);

            }
        });
    }


}
