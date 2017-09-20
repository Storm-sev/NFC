package com.efeiyi.net;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/31.
 */
public class NetRequestUtil<T> {
    public static <T> void post(final String url, final Map<String, String> t, final Callback<T> callback) {
        new Thread() {
            @Override
            public void run() {
                Gson gson = new Gson();
                OkHttpUtils
                        .postString()
                        .url(url)
                        .content(gson.toJson(t))
                        .build()
                        .connTimeOut(5000)
                        .readTimeOut(5000)
                        .writeTimeOut(5000)
                        .execute(callback);
            }
        }.start();

    }
    //*下载*//*
    public static <T> void download(final String url, final Map<String, String> paramsMap, final Callback<T> callback) {
        new Thread() {
            @Override
            public void run() {
                PostFormBuilder post = OkHttpUtils.post();
                post.url(url)
                        .params(paramsMap)
                        .build();
                RequestCall build = post.build();
                build.connTimeOut(500000);
                build.readTimeOut(500000);
                build.writeTimeOut(500000);
                build.execute(callback);

            }
        }.start();
    }

}