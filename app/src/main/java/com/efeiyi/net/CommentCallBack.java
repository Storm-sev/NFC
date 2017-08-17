package com.efeiyi.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.Callback;

import java.util.Map;

import okhttp3.Response;

/**
 * Created by Administrator on 2016/4/9.
 */
public abstract class CommentCallBack extends Callback<Map<String,Object>> {
    @Override
    public Map<String,Object> parseNetworkResponse(Response response) throws Exception {
        String string = response.body().string();
        Gson gson = new Gson();
        Map<String, Object> resultMap = gson.fromJson(string, new TypeToken<Map<String, Object>>() {
        }.getType());
        return resultMap;
    }
}
