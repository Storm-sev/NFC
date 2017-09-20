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
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.getApplicationContext().startActivity(intent);

    }





    /**
     * 获取下载文件链接中获取文件名
     */
    public static String getNameFromUrl(String url) {

        return url.substring(url.lastIndexOf("/") + 1);


    }

    /**
     *
     */
    public static String getHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        /*    if (i > 0) {
                sb.append(" ");
            }*/
        }
        return sb.toString();
    }


    /**
     *
     */
    public static long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;

    }


    /**
     *
     */
    public static long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    /**
     * 退出本应用
     */
    public static void exitApp() {

        //检查app 中是否还有activity
        System.exit(0);

    }
}
