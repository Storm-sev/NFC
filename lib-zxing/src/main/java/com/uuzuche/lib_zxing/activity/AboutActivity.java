package com.uuzuche.lib_zxing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uuzuche.lib_zxing.R;

/**
 * Created by YangZhenjie on 2016/11/18.
 */
public class AboutActivity extends Activity {


    private TextView tvTitle;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        back = (ImageView) findViewById(R.id.back);
        tvTitle.setText("关于我们");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
