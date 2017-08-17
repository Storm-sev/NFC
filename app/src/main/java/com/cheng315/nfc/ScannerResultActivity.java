package com.cheng315.nfc;

import android.os.Bundle;
import android.webkit.WebView;

import com.efeiyi.BaseActivity;

/**
 * Created by YangZhenjie on 2017/3/8.
 */
public class ScannerResultActivity extends BaseActivity {

    private WebView webView;
    private String scannerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_result);
        scannerResult = getIntent().getStringExtra("scannerResult");
        webView = (WebView) findViewById(R.id.scanner_result);
        webView.getSettings().setJavaScriptEnabled(true);
        if (scannerResult != null)
        webView.loadUrl(scannerResult);
    }
}
