package com.efeiyi.base;

import android.app.PendingIntent;
import android.content.Intent;

import com.cheng315.nfc.nfc.NFCManager;

/**
 * Created by Administrator on 2017/8/31.
 * 带有nfc 功能的activity
 */

public abstract class BaseNfcActivity extends BaseActivity {


    protected Intent mIntent;
    protected PendingIntent mPendingIntent;


    @Override
    protected void initNfc() {
        mIntent = new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        NFCManager.getInstance();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NFCManager.getInstance().onNewIntent(intent);


    }

    @Override
    protected void onResume() {
        super.onResume();

        NFCManager.getInstance().onResume(this, mPendingIntent);

    }


    @Override
    protected void onPause() {
        super.onPause();

        NFCManager.getInstance().onPause(this);

    }
}
