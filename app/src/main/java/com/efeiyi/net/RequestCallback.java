package com.efeiyi.net;


import com.efeiyi.Entity;
import com.efeiyi.UserException;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/17.
 */
public abstract class RequestCallback<T extends Entity> implements okhttp3.Callback {


    @Override
    public void onFailure(Call call, IOException e) {
        onError(call, UserException.IO_ERROR);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        onResponse(response.body().string());
    }

    public void onError(Call call, int code) {
        UserException ex = new UserException(code);
        if (code > 0) {
//            Toast.makeText(App.getContext(), ex.getDescription(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onResponse(String content) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(content);
        } catch (java.lang.Exception e) {
            onError(null, UserException.PARAM_ERROR);
        }
        if (jsonObject.has("code")) {
            int code = 0;
            try {
                code = jsonObject.getInt("code");
            } catch (java.lang.Exception e) {
                onError(null, UserException.INNER_ERROR);
            }
            if (code != 0) {
                onError(null, code);
            } else {
                onSuccess();
            }
        }

        if (jsonObject.has("sum")) {
            int sum = 0;
            try {
                sum = jsonObject.getInt("sum");
                if (sum == 0) {
                    onEmpty();
                }
            } catch (java.lang.Exception e) {
                onError(null, UserException.INNER_ERROR);
                return;
            }
        }

        if (!jsonObject.has("data")) {
            onSuccess();
        } else {

            Object data = null;

            try {
                data = jsonObject.get("data");
            } catch (JSONException e) {
                onError(null, UserException.INNER_ERROR);
                return;
            }

            Type genType = getClass().getGenericSuperclass();
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            Class<?> entityClass = (Class) params[0];

            List<T> entityList = new ArrayList<T>();
            Gson gson = new Gson();
            if (data instanceof JSONObject) {
                T entity = (T) gson.fromJson(data.toString(), entityClass);
                entityList.add(entity);
                onResponse(entityList);
                return;
            } else if (data instanceof JSONArray) {

                JSONArray jsonArray = (JSONArray) data;
                for (int i = 0; i < jsonArray.length(); i++) {
                    T entity = null;
                    try {
                        entity = (T) gson.fromJson(jsonArray.get(i).toString(), entityClass);
                    } catch (Exception e) {
                        onError(null, UserException.INNER_ERROR);
                        return;
                    }
                    entityList.add(entity);
                }

                onResponse(entityList);
            }
        }
    }

    public abstract void onResponse(List<T> entityList);

    public abstract void onSuccess();

    public abstract void onEmpty();

}
