package com.efeiyi;

import android.app.Activity;

/**
 * Created by Administrator on 2016/10/17.
 */
public class BaseActivity extends Activity {




    @Override
    protected void onResume() {
        super.onResume();
        App app = (App)getApplication();
        app.setCurrentActivity(this);
    }
}
