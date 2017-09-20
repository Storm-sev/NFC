package com.cheng315.nfc.httpclient;


import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.cheng315.nfc.R;
import com.cheng315.nfc.utils.AppUtil;
import com.cheng315.nfc.utils.LogUtils;
import com.efeiyi.App;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/23.
 */

public class UpdateService extends Service {


    private static final String TAG = UpdateService.class.getSimpleName();

    public static final int UPDATE_CODE = 1001;

    public static boolean DOWNLOAD_CODE = false;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private int preProgress;

    private RemoteViews mNotiCustomView; // 通知栏自定义view


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG, "开启下载服务");
        createNotification();
        downLoadApk(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 网络 获取新版本
     */
    private void downLoadApk(Intent intent) {
        if (intent != null) {

            final String apk_url = intent.getStringExtra("APK_URL");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    HttpClientManager.getInstance().downLoadingNewVersionApk(apk_url,
                            new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),
                                    AppUtil.getNameFromUrl(apk_url)) {

                                @Override
                                public void inProgress(final float progress, long total) {

//                                    LogUtils.d(TAG, "下载链接运行的线程");
                                    updateUI(progress);
                                }

                                @Override
                                public void onError(Call call, Exception e) {
                                    Toast.makeText(App.getContext(), "下载失败", Toast.LENGTH_SHORT).show();
                                    DOWNLOAD_CODE = false;
                                }

                                @Override
                                public void onResponse(File response) {

                                    AppUtil.installApp(App.getContext(), response);
                                    // 取消在状态栏的显示
                                    mNotificationManager.cancel(UPDATE_CODE);
                                    DOWNLOAD_CODE = false;
                                    stopSelf();

                                }
                            });
                }
            }).start();

        } else {
            DOWNLOAD_CODE = false;
            stopSelf();
        }
    }


    /**
     * 更新通知栏进度
     *
     * @param progress
     */
    private void updateUI(final float progress) {

        int curProgress = (int) (progress * 100);

        if (preProgress < curProgress) {

            mNotiCustomView.setProgressBar(R.id.pb_download, 100, curProgress, false);
            mNotiCustomView.setTextViewText(R.id.tv_tool__title, "诚品宝:" + curProgress + "%");
            mNotificationManager.notify(UPDATE_CODE, mBuilder.build());
        }

        preProgress = curProgress;
    }


    /**
     * 初始化通知栏
     */
    private void createNotification() {


        mBuilder = new NotificationCompat.Builder(this);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        mNotiCustomView = new RemoteViews(this.getPackageName(), R.layout.notification_update);
        mNotiCustomView.setProgressBar(R.id.pb_download, 100, 0, false);
        mNotiCustomView.setTextViewText(R.id.tv_tool__title, "诚品宝: 0%");

        mBuilder.setContent(mNotiCustomView);
        mBuilder.setSmallIcon(R.mipmap.icon_logo);
        mNotificationManager.notify(UPDATE_CODE, mBuilder.build());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * 系统启动下载版本的服务.
     */
    public static void startOrDownLoadApp(Context context, String apkUrl) {

        Intent intent = new Intent(context, UpdateService.class);
        intent.putExtra("APK_URL", apkUrl);
        context.startService(intent);

    }
}

