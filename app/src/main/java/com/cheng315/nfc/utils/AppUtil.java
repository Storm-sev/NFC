package com.cheng315.nfc.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.efeiyi.App;

import java.io.File;

/**
 * Created by Administrator on 2017/8/16.
 * app
 */

public class AppUtil {


    /**
     * 当前应用的版本名称
     *
     * @return
     */
    public static String getVersionName() {
        PackageManager packageManager = App.getContext().getPackageManager();

        try {
            PackageInfo packageInfo =
                    packageManager.getPackageInfo(App.getContext().getPackageName(), 0);
            LogUtils.d(packageInfo.versionName);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取当前应用的版本号
     */
    public static String getVersionCode() {
        PackageManager packageManager = App.getContext().getPackageManager();
        try {
            PackageInfo packageInfo =
                    packageManager.getPackageInfo(App.getContext().getPackageName(), 0);

            LogUtils.d(packageInfo.versionCode);
            return packageInfo.versionCode + "";

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";

    }


    /**
     * install app
     * 更新app
     */
    public static void installApp(Context context, File file) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        context.startActivity(intent);

    }


    /**
     * 获取下载文件链接中获取文件名
     */
    public static String getNameFromUrl(String url) {

        return url.substring(url.lastIndexOf("/") + 1);


    }

}
