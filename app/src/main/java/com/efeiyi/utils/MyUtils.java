package com.efeiyi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.efeiyi.App;

import java.io.UnsupportedEncodingException;

/**
 * Created by YangZhenjie on 2016/9/21.
 */
public class MyUtils {

    /**
     * 获取json并保存本地，使用json
     * @return
     */
    public static String getUserInfo() {
        SharedPreferences pref = App.getContext().getSharedPreferences(
                "user_login", 0);
        return pref.getString("user_info", "");
        }
    public static boolean saveUserInfo(String info) {
        SharedPreferences pref = App.getContext().getSharedPreferences(
                "user_login", 0);
        return pref.edit().putString("user_info", info).commit();
    }


//    在使用的时候，如果服务端返回一个User的JSON数据，那么我们可以这样：

  /*  Gson gson = new Gson();
    User user = gson.fromJson(response, User.class);
//对user的变量做一些修改，然后保存
    UserUtils.saveUserInfo(gson.toJson(user));
    //下次使用的时候就可以不用访问网络资源了
    User user = gson.fromJson(UserUtils.getUserInfo(), User.class);*/

    /**
     * 只对中文进行编码
     * @param str
     * @return
     */
  public  static String utf8Togb2312(String str){
      String data="";
      try {
          for(int i=0;i<str.length();i++){
              char c=str.charAt(i);
              if(c+"".getBytes().length>1&&c!=':'&&c!='/'){
                  data = data+java.net.URLEncoder.encode(c+"","utf-8");
              }else {
                  data=data+c;
              }
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }finally{
          System.out.println(data);
      }
      return  data;
  }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

