package com.cheng315.nfc.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cheng315.nfc.R;
import com.efeiyi.utils.MyUtils;

/**
 * Created by Administrator on 2017/8/21.
 * image request utils
 */

public class ImageLoaderUtils {


    /**
     * context 根据context 类型绑定不同的生命周期
     */
    public static void loadImage2CenterCrop(Context context, ImageView imageView, String imageUrl) {

        Glide.with(context)
                .load(MyUtils.utf8Togb2312(imageUrl))
                .centerCrop() // 缩放图片到填充到imageview中
                .placeholder(R.mipmap.icon_empty)
                .error(R.mipmap.icon_empty)
                .into(imageView);

    }


    /**
     *
     * @param context 不同的glide
     * @param imageView
     * @param imageUrl
     */
    public static void loadImage(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.mipmap.icon_empty)
                .error(R.mipmap.icon_empty)
                .into(imageView);

    }



}
