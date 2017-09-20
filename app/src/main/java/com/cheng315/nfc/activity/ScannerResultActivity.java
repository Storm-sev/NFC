package com.cheng315.nfc.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cheng315.nfc.R;
import com.cheng315.nfc.utils.LogUtils;
import com.cheng315.nfc.utils.StringUtils;
import com.efeiyi.base.BaseActivity;

/**
 * Created by YangZhenjie on 2017/3/8.
 */
public class ScannerResultActivity extends BaseActivity {


    private static final String TAG = "ScannerResultActivity";


    private WebView webView;
    private String scannerResult;

    @Override
    protected void initNfc() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

        scannerResult = getIntent().getStringExtra("scannerResult");

        LogUtils.d(TAG, "扫描获取的结果 是 + " + scannerResult + ": " + StringUtils.checkIsNet(scannerResult));

        if (scannerResult != null && StringUtils.checkIsNet(scannerResult)) {

            // 判断是否包含特定的字符串
            if (StringUtils.isContains(scannerResult)) {

            }

            webView.loadUrl(scannerResult);

        } else {


        }


    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_scanner_result;
    }

    @Override
    protected void setUpListener() {

    }

    private void initview() {

        webView = (WebView) findViewById(R.id.scanner_result);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        //不打开系统浏览器

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });


    }



    @Override
    protected void onDestroy() {

        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();


    }
}
