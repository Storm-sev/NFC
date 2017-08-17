package com.efeiyi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/8/16.
 */

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        initViews();
        initDatq();
        setUpListener();
        
    }

    protected abstract int attachLayoutRes();

    protected abstract void setUpListener();

    protected abstract void initDatq();

    protected abstract void initViews();



}
