package com.cheng315.nfc.httpclient;

import com.efeiyi.Entity;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/8/18.
 */

public interface OnCommonCallBack<T extends Entity> {

    /**
     * 请求成功的回调
     */
    void onComplete(List<T> entityList);

    /**
     * 请求发生错误
     * @param call
     * @param code
     */
    void onError(Call call, int code);


    /**
     * 数据为空
     */
    void onEmpty();


}
