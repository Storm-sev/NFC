package com.cheng315.nfc.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.cheng315.nfc.R;
import com.efeiyi.utils.MyUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by YangZhenjie on 2016/11/17.
 */
public class MiuiToast {
    private final WindowManager mWdm;
    private final View mToastView;
    private final Timer mTimer;
    private final boolean mShowTime;
    private boolean mIsShow;
    private WindowManager.LayoutParams mParams;

    public static MiuiToast MakeText(Context context, String text, int height, boolean showTime) {
        MiuiToast result = new MiuiToast(context, text, height, showTime);
        return result;
    }
    private MiuiToast(Context context, String text, int height, boolean showTime ){
         mShowTime = showTime;//记录Toast的显示长短类型
         mIsShow = false;//记录当前Toast的内容是否已经在显示
         mWdm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //通过Toast实例获取当前android系统的默认Toast的View布局
//         mToastView = Toast.makeText(context, text, Toast.LENGTH_SHORT).getView();
         mToastView = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
//        mToastView.setBackgroundResource(R.color.background);
         mTimer = new Timer();
        //设置布局参数
        setParams(context, height);
    }
    private void setParams(Context context, int height) {
        mParams = new WindowManager.LayoutParams();
        mParams.height = height + MyUtils.dip2px(context,30);
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.windowAnimations = R.style.anim_view;//设置进入退出动画效果
        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.TOP;
        mParams.y = 0;
    }
    public void show(){
        if(!mIsShow){//如果Toast没有显示，则开始加载显示
            mIsShow = true;
            mWdm.addView(mToastView, mParams);//将其加载到windowManager上
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    mToastView.clearFocus();
                    mWdm.removeView(mToastView);
                    mIsShow = false;
                }
            }, (long)(mShowTime ? 3500 : 2000));
        }
    }


   /* private static TextView mTextView;
    private static ImageView mImageView;

    public static void showToast(Context context, String message) {
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.my_toast, null);
        //初始化布局控件
 *//*       mTextView = (TextView) toastRoot.findViewById(R.id.message);
        //为控件设置属性
        mTextView.setText(message);
        mImageView.setImageResource(R.mipmap.ic_launcher);*//*
        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, 0);
        toastStart.setDuration(Toast.LENGTH_LONG);
        toastStart.setView(toastRoot);
        toastStart.show();
    }*/
}
