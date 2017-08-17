package com.efeiyi;

import android.app.Activity;
import android.content.Context;

import com.cheng315.nfc.utils.LogUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by Administrator on 2016/10/17.
 */
public class App extends android.app.Application {
    private Activity currentActivity;
    private static Context mContext;

    // 初始化log工具
    public static LogUtils.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ZXingLibrary.initDisplayOpinion(this);

        // 初始化log
        mBuilder = new LogUtils.Builder()
                .setLogSwitch(true)
                .setGlobalTag("LJY")// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLog2FileSwitch(false)// 打印log时是否存到文件的开关，默认关
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
                .setLogFilter(LogUtils.V);// log过滤器，和logcat过滤器同理，默认Verbose

    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    //获取全局Context
    public static Context getContext() {
        return mContext;
    }
}
