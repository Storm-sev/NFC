package com.cheng315.nfc.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheng315.nfc.R;
import com.cheng315.nfc.entity.Banner;
import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.httpclient.HttpClientManager;
import com.cheng315.nfc.httpclient.OnCommonCallBack;
import com.cheng315.nfc.httpclient.UpdateService;
import com.cheng315.nfc.nfc.NFCManager;
import com.cheng315.nfc.utils.AppUtil;
import com.cheng315.nfc.utils.DialogHelper;
import com.cheng315.nfc.utils.GlideImageLoader;
import com.cheng315.nfc.utils.LogUtils;
import com.efeiyi.Constant;
import com.efeiyi.base.BaseNfcActivity;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static com.cheng315.nfc.R.raw.ring;

public class MainActivity extends BaseNfcActivity {


    private static final String TAG = "MainActivity";


    private AnimationDrawable animationDrawable;
    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
//    private LinearLayout mTagContent;
    //  private SimpleDraweeView dvRecieve;
    private boolean isPress = false;
    private List<ImageView> data;
    //存放代表viewpager播到第几张的小圆点
//    private LinearLayout ll_tag;
//    private int currentItem = 0;
//    int p = 0;
    private com.youth.banner.Banner banner;
    //    private List<Banner> urlList;
    private RelativeLayout rl_banner;
    private TextView about;
    private String apkUrl;
    private AlertDialog mDialog;

    private TextView send;
    private TextView hint;
    private String uri;
    //    private Intent intent;
    private GifImageView dvRecieve;
    private static final int REQUEST_CODE = 101;
    private ImageView menu;
    private PopupWindow pw;
    private LinearLayout llQrCode;
    private String mTagId;
//    private String preTagId;

    private boolean isUpdate;


    private GestureDetector mDetector;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 01:
                    showMessage(R.string.error, R.string.no_net);
                    break;
            }

        }
    };

    @Override
    protected void initViews() {
        send = (TextView) findViewById(R.id.send);
        hint = (TextView) findViewById(R.id.hint);
        menu = (ImageView) findViewById(R.id.menu);
        llQrCode = (LinearLayout) findViewById(R.id.ll_qr);
        dvRecieve = (GifImageView) findViewById(R.id.iv_recieve);
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        banner = (com.youth.banner.Banner) findViewById(R.id.banner);
        dvRecieve.setImageResource(R.drawable.recieve_nfc);

        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer((GifDrawable) dvRecieve.getDrawable());

        setUpNfc();
        initBanner();


    }


    private void setUpNfc() {

        NfcAdapter nfcAdapter = NFCManager.getInstance().getNfcAdapter();

        if (nfcAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);

        }

    }

    @Override
    protected void initData() {

        uri = getIntent().getStringExtra("uri");
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        apkUrl = getIntent().getStringExtra("apkUrl");

        LogUtils.d("获取isUpdate 的值", isUpdate);

        if (isUpdate) {

            showVersionUpdataDialog();
        } else {
            showVersionNoUpdataDialog();
        }

        if (uri != null) {
            hint.setVisibility(View.GONE);
            send.setVisibility(View.GONE);
            dvRecieve.setVisibility(View.VISIBLE);
            isPress = true;
        }


        NFCManager.getInstance().resolveIntent(getIntent());

        getBannerUrl();
        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTagId = null;
        banner.startAutoPlay();
    }

    /**
     * 监听
     */
    @Override
    protected void setUpListener() {


        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return super.onDown(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                isPress = true;
                LogUtils.d(TAG, "手势识别 + onlongPress");
                if (mTagId != null) {
                    LogUtils.d(TAG, "先检测到nfc回调的时候, 然后发送检测 走长按事件的监听 ..........................................");
                    sendTag(mTagId);
                }

            }
        });

        NFCManager.getInstance().setOnTagIdCompleteListener(new NFCManager.OnTagIdCompleteListener() {
            @Override
            public void onTransmit(String tagId) {
                LogUtils.d("nfcmanager 回调到的数字" + tagId);

                mTagId = tagId;
                if (isPress) {
                    LogUtils.d(TAG, "先按住按钮靠近nfc卡片  然后 通过新的回调传递的tagid 读取并且发送  ---------------------");
                    sendTag(tagId);
                }
            }
        });


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        llQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到扫描页面
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    LogUtils.d(TAG, "手势检测 _action up ");
                    isPress = false;

                }
                if (mDetector != null) {
                    mDetector.onTouchEvent(event);
                }

                return true;
            }

        });


//        send.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    isPress = true;
////                    dvRecieve.setVisibility(View.VISIBLE);
//
//                    hint.setVisibility(View.GONE);
//                    send.setBackgroundResource(R.drawable.shape_button_press);
//                    send.setTextColor(getResources().getColor(R.color.white));
////                    NFCManager.getInstance().setOnTagIdCompleteListener(new NFCManager.OnTagIdCompleteListener() {
////                        @Override
////                        public void onTransmit(String tagId) {
////
////                            sendTag(tagId);
////                        }
////                    });
//
//                    return true;
//                }
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    isPress = false;
////                    dvRecieve.setVisibility(View.GONE);
//                    hint.setVisibility(View.VISIBLE);
//                    send.setBackgroundResource(R.drawable.shape_button);
//                    send.setTextColor(getResources().getColor(R.color.black));
////                    mTagId = null;
//                    return true;
//                }
//                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
//                    isPress = false;
//                    send.setBackgroundResource(R.drawable.shape_button);
//                    send.setTextColor(getResources().getColor(R.color.black));
////                    dvRecieve.setVisibility(View.GONE);
//                    hint.setVisibility(View.VISIBLE);
//                    return true;
//                }
//
//
//                if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
//
//////                    if(mTagId != null && !mTagId.equals(preTagId)) {
////
////                    if (mTagId != null) {
////
////                        sendTag(mTagId);
////                    }
////
//////
//////                        preTagId = mTagId;
//////
//////                    }
//
//                    if (mTagId != null && mTagId.equals(preTagId)) {
//                        sendTag(preTagId);
//                    }
//                    if (mTagId != null && !mTagId.equals(preTagId)) {
//                        preTagId = mTagId;
//                        sendTag(preTagId);
//
//                    }
//
//
//                }
////                NFCManager.getInstance().setOnTagIdCompleteListener(new NFCManager.OnTagIdCompleteListener() {
////                    @Override
////                    public void onTransmit(String tagId) {
////                        LogUtils.d("nfcmanager 回调到的数字" + tagId);
////
////                        mTagId = tagId;
////
//////                        sendTag(tagId);
////                    }
////                });
////                if (mTagId != null) {
////                    sendTag(mTagId);
////                }
//
//                return true;
//            }
//        });


    }


    private void showVersionNoUpdataDialog() {

        if (apkUrl == null) {
            return;
        }


        new AlertDialog.Builder(this)
                .setTitle("版本提示")
                .setMessage("已经是最新版本")
                .setPositiveButton("确定", null)
                .show();
    }

    private void showVersionUpdataDialog() {

        if (UpdateService.DOWNLOAD_CODE) {
            return;
        }
        DialogHelper.showDialog(this, R.string.nfc_version_updata, new DialogHelper.DialogPositiveButtonListener() {
            @Override
            public void onDialogPositiveButtonListener() {
                if (null != apkUrl) {
                    UpdateService.startOrDownLoadApp(MainActivity.this,apkUrl);
                    UpdateService.DOWNLOAD_CODE = true;

                }
            }

            @Override
            public void onDialogNegativeButtonListener() {

            }
        });

    }

    /**
     * 获取banner的数据
     */
    public void getBannerUrl() {


        LogUtils.d(TAG, " 获取的首页banner 地址" + Constant.MAIN_BANNER);

        HttpClientManager.getInstance().getMainBannerForNet(Constant.MAIN_BANNER, new OnCommonCallBack<Banner>() {
            @Override
            public void onComplete(final List<Banner> entityList) {

                LogUtils.d(TAG, "获取的主页banner 数据  + " + entityList.get(0).getImgUrl());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setBannerData(entityList);
                    }
                });

            }

            @Override
            public void onError(Call call, int code) {

            }

            @Override
            public void onEmpty() {

            }
        });

    }

    /**
     * 设置banner的数据
     */
    private void setBannerData(List<Banner> entityList) {

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        banner.setImageLoader(new GlideImageLoader());

        // 设置动画效果
        banner.setBannerAnimation(Transformer.Accordion);
        banner.isAutoPlay(true);
        banner.setDelayTime(2000);
        banner.setIndicatorGravity(BannerConfig.CENTER);

        List<String> bannerImages = new ArrayList<>();

        if (entityList != null) {
            for (Banner bannerBean : entityList) {
                bannerImages.add(bannerBean.getImgUrl());
            }
        }

        banner.setImages(bannerImages);
        banner.start();

    }

    private void initBanner() {

//        ll_tag = (LinearLayout) findViewById(R.id.ll_tag);
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素） 屏幕高度的1/3
        int screenHeight = metric.heightPixels; // 获取屏幕宽度
        int screenWith = metric.widthPixels; // 获取屏幕宽度
        int height = (int) (screenHeight * 0.3);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_banner.getLayoutParams();
        params.width = screenWith;
        params.height = height;
        rl_banner.setLayoutParams(params);
    }

    private void sendTag(String s) {

        HttpClientManager.getInstance().checkTagFormNet(s, new OnCommonCallBack<Product>() {
            @Override
            public void onComplete(List<Product> entityList) {

                LogUtils.d(TAG, "oncomplete");

                if (isPress) { //按下的时候
                    MediaPlayer ringPlayer = MediaPlayer.create(getBaseContext(), ring);
                    ringPlayer.start();
//                    isPress = false;
                    mTagId = null;
                    CertificateResultsActivity.startSelf(MainActivity.this, entityList);

                }
            }

            @Override
            public void onError(Call call, int code) {
                LogUtils.d(TAG, "onerror");
                if (code != 0) {
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                }


            }

            @Override
            public void onEmpty() {
                LogUtils.d(TAG, "emptty");
                if (isPress) {
                    MediaPlayer ringPlayer = MediaPlayer.create(getBaseContext(), ring);
                    ringPlayer.start();
                    CertificateFailed.startSelf(MainActivity.this);
                }

            }
        });
    }

    private void showMessage(int title, int message) {
        DialogHelper.showNoClickDialog(this, title, message);
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }


    private void showWirelessSettingsDialog() {
        DialogHelper.showClickDialog(this, R.string.nfc_disabled, new DialogHelper.DialogPositiveButtonListener() {
            @Override
            public void onDialogPositiveButtonListener() {
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);

                startActivity(intent);

            }

            @Override
            public void onDialogNegativeButtonListener() {
                MainActivity.this.finish();
                //退出app
                AppUtil.exitApp();

            }
        });

    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);//更新intent
        NFCManager.getInstance().onNewIntent(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //处理二维码扫描结果
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, ScannerResultActivity.class);
                    intent.putExtra("scannerResult", result);
                    startActivity(intent);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void showPopup(View view) {
        // 加载pop显示的布局文件
        View contentView = View.inflate(getApplicationContext(),
                R.layout.view_pop, null);
        // 得到pop界面中的控件
        about = (TextView) contentView.findViewById(R.id.about);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                pw.dismiss();
            }
        });

        // 弹出一个泡泡
        // contentView 显示的界面
        // focusable 是否获得焦点
        pw = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置背景：只有添加了背景后才能响应返回键的事件
        pw.setBackgroundDrawable(new ColorDrawable());
        // 把pop显示在某个控件的下面
        // view 现在这个view的下面
        // xoff 在水平方向的偏移量
        // yoff 在垂直方向的偏移量
        pw.showAsDropDown(view, 0, 3);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }


    //


    private long mExitTime;

    /**
     * 连续点击退出当前应用
     *
     */
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - mExitTime > 2000) {
            mExitTime = System.currentTimeMillis();
            Toast.makeText(this, "再次点击退出当前应用", Toast.LENGTH_SHORT).show();
        } else {

            super.onBackPressed();
        }


    }
}
