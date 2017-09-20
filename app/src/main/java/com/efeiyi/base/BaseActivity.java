package com.efeiyi.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/16.
 * activity 的基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(attachLayoutRes());
        ButterKnife.bind(this);
        initNfc();
        initViews();
        initData();
        setUpListener();

    }

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    protected abstract void initNfc();


    protected abstract void initData();

    /**
     * 加载布局
     *
     * @return
     */
    protected abstract int attachLayoutRes();


    /**
     * 设置监听
     */
    protected abstract void setUpListener();


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();


    }
}
