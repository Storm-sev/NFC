package com.cheng315.nfc.httpclient;

import com.cheng315.nfc.entity.AppVersion;
import com.cheng315.nfc.entity.Banner;
import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.utils.AppUtil;
import com.cheng315.nfc.utils.LogUtils;
import com.efeiyi.Constant;
import com.efeiyi.net.HttpRequest;
import com.efeiyi.net.NetRequestUtil;
import com.efeiyi.net.RequestCallback;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/17.
 * 网络请求管理
 */

public class HttpClientManager {

    private static final String TAG = "HttpClientManager";

    private static volatile HttpClientManager INSTANCE;


    private HttpClientManager() {

    }


    public static HttpClientManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpClientManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpClientManager();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 联网进行TagId验证
     */

    public void checkTagFormNet(String tagId, final OnCommonCallBack<Product> onCommonCallBack){


        HttpRequest.get(Constant.CHECK_TAGID + tagId, new RequestCallback<Product>() {
            @Override
            public void onResponse(List<Product> entityList) {

                if (onCommonCallBack != null) {
                    onCommonCallBack.onComplete(entityList);
                }

            }

            @Override
            public void onError(Call call, int code) {


                if (onCommonCallBack != null) {

                    onCommonCallBack.onError(call,code);
                }
                
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onEmpty() {
                
                if(onCommonCallBack != null) {

                    onCommonCallBack.onEmpty();


                }
            }
        });



    }

    /**
     * 首页 banner 数据请求
     *
     */
    public void getMainBannerForNet(String url, final OnCommonCallBack<Banner> onCommonCallBack) {


        LogUtils.d(TAG, "请求首页banner请求的数据 : " + url);

        HttpRequest.get(url, new RequestCallback<Banner>() {
            @Override
            public void onResponse(List<Banner> entityList) {

                if (onCommonCallBack != null) {
                    onCommonCallBack.onComplete(entityList);

                }
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onEmpty() {

            }
        });


    }


    /**
     * 联网下载新版本的apk
     */
    public void downLoadingNewVersionApk(String apkUrl, FileCallBack fileCallBack) {

        Map<String, String> paramsMap = new HashMap<>();

        NetRequestUtil.download(apkUrl, paramsMap, fileCallBack);
    }


    /**
     * 联网检查版本状态
     */
    public void checkVersion(String checkVUrl, final OnCheckVersionListener onCheckVersionListener) {

        String version_id = "V" + AppUtil.getVersionCode();
        String version_mini = AppUtil.getVersionName();
        String version_code = version_id + "_" + version_mini;

        LogUtils.d(TAG, " 获取的版本请求参数 : " + version_code + "检查版本请求的地址 : " + Constant.CHECK_VERSION + version_code);

        HttpRequest.get(checkVUrl + version_code, new RequestCallback<AppVersion>() {

            @Override
            public void onResponse(List<AppVersion> entityList) {

                LogUtils.d(TAG, "CheckVersion 请求成功 : " + entityList.get(0).getApkUrl());
                String apkUrl = entityList.get(0).getApkUrl();

                if (onCheckVersionListener != null) {
                    onCheckVersionListener.onResponse(apkUrl, true);
                }

            }

            @Override
            public void onSuccess() {

                LogUtils.d(TAG, "checkVersion on success : ");

            }

            @Override
            public void onEmpty() {

                LogUtils.d(TAG, "checkVersion on empty : ");


            }

            @Override
            public void onError(Call call, int code) {
                super.onError(call, code);

                LogUtils.d(TAG, "checkVersion on error : ");

                if (code == 7 || code == 3) {
                    if (onCheckVersionListener != null) {
                        onCheckVersionListener.onError(false, code);
                    }
                }


            }
        });


    }


}
