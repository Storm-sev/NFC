package com.cheng315.nfc.httpclient;

/**
 * Created by Administrator on 2017/8/18.
 * 版本检测监听
 */

public interface OnCheckVersionListener {


    /**
     * 版本有更新的回调
     */
    void onResponse(String url, boolean update);

    void onSuccess();

    void onEmpty();

    /**
     * 返回空
     *
     */
    void onError(boolean update, int code);



}
