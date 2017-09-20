package com.cheng315.nfc.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cheng315.nfc.R;
import com.cheng315.nfc.adapter.MypageAdapter;
import com.cheng315.nfc.entity.Banner;
import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.utils.DialogHelper;
import com.cheng315.nfc.utils.LogUtils;
import com.cheng315.nfc.view.ScaleScreenImageView;
import com.efeiyi.Constant;
import com.efeiyi.base.BaseActivity;
import com.efeiyi.net.HttpRequest;
import com.efeiyi.net.NetRequestUtil;
import com.efeiyi.net.RequestCallback;
import com.efeiyi.utils.MyUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainFromNetActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "MainFromNetActivity";



    private GifImageView dvRecieve;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private boolean isPress = false;
    private AlertDialog mDialog;
    private Intent intent;
    //统计下载了几张图片
    int n = 0;
    //统计当前viewpager轮播到第几页
    int p = 0;
    private ViewPager vp;
    //准备好三张网络图片的地址
   /* private String imageUrl[]=new String[]
            {"http://a.hiphotos.baidu.com/zhidao/pic/item/f2deb48f8c5494ee203bf98c2cf5e0fe99257e0e.jpg",
                    "http://img06.tooopen.com/images/20161106/tooopen_sy_185050549459.jpg",
                    "http://img04.tooopen.com/images/20130701/tooopen_20083555.jpg"};*/
    //装载下载图片的集合
    private List<ImageView> data;
    //控制图片是否开始轮播的开关,默认关的
    private boolean isStart = false;

    //存放代表viewpager播到第几张的小圆点
    private LinearLayout ll_tag;
    //存储小圆点的一维数组
    private ImageView tag[];
    private static final int MSG_KEEP_SILENT = 4;
    private static final int MSG_UPDATE_IMAGE = 3;
    private static final int MSG_BREAK_SILENT = 5;
    private static final int MSG_PAGE_CHANGED = 6;
    private static final int DISAPPEAR_TOAST = 7;
    private static final int INIT_BANNER = 8;

    private int currentItem = 0;
    private static final int REQUEST_CODE = 101;

    private TranslateAnimation myAnimation_Translate;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (handler.hasMessages(MSG_UPDATE_IMAGE)) {
                handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case 0:

//                    MiuiToast.MakeText(MainFromNetActivity.this,"请移动手机至合适位置", height, true).show();
                  /*  View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
                    Toast toast=new Toast(getApplicationContext());
                    toast.setView(toastRoot);
                    TextView tv=(TextView)toastRoot.findViewById(R.id.TextViewInfo);
                    tv.setText("说明：这是一个自定义的边框和底色的提示框。");
                    toast.show();*/
//                    MiuiToast.showToast(MainFromNetActivity.this,"hahahaha");
//                    hint.setVisibility(View.VISIBLE);

                    tvToast.setVisibility(View.VISIBLE);
                    myAnimation_Translate = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, -1,
                            Animation.RELATIVE_TO_PARENT, 0);
                    myAnimation_Translate.setDuration(1000);
                    myAnimation_Translate.setInterpolator(AnimationUtils
                            .loadInterpolator(MainFromNetActivity.this,
                                    android.R.anim.accelerate_decelerate_interpolator));
                    tvToast.startAnimation(myAnimation_Translate);
                    new Thread(new MyThread()).start();

                    break;
                case 1:
                    showMessage(R.string.error, R.string.no_net);
                    break;
                case 2:
                    //接受到的线程发过来的p数字
                    int page = (Integer) msg.obj;
                    vp.setCurrentItem(page);
                    break;
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    vp.setCurrentItem(currentItem);
                    //准备下次播放
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, 3000);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, 3000);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                case DISAPPEAR_TOAST:
                    tvToast.setVisibility(View.INVISIBLE);
                    break;
                case INIT_BANNER:
                    initBanner();
                    break;
                default:
                    break;
            }
        }
    };
    private List<Banner> urlList;
    private RelativeLayout rl_banner;
    private TextView hint;
    private boolean mIsShow;
    private RelativeLayout rlTitle;
    private int height;
    private TextView tvToast;
    private TextView about;
    private ImageView menu;
    private PopupWindow pw;
    private LinearLayout llQrCode;
    private boolean isUpdate;
    private ProgressDialog pd;
    private String apkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fresco.initialize(this);

        LogUtils.d(TAG,"进入到 这个方法里面----------------------MainFromNetActivity------------------------------");

        setContentView(R.layout.activity_main_net);
        getBannerUrl();
        pd = new ProgressDialog(MainFromNetActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
       /* urlList = new ArrayList<>();
            urlList.add("http://a.hiphotos.baidu.com/zhidao/pic/item/f2deb48f8c5494ee203bf98c2cf5e0fe99257e0e.jpg");
            urlList.add("http://img06.tooopen.com/images/20161106/tooopen_sy_185050549459.jpg");
            urlList.add("http://img04.tooopen.com/images/20130701/tooopen_20083555.jpg");*/
        dvRecieve = (GifImageView) findViewById(R.id.dv_recieve);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        tvToast = (TextView) findViewById(R.id.tv_toast);

        menu = (ImageView) findViewById(R.id.menu);
        llQrCode = (LinearLayout) findViewById(R.id.ll_qr);

        menu.setOnClickListener(this);
        llQrCode.setOnClickListener(this);

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        rlTitle.measure(w, h);
        height = rlTitle.getMeasuredHeight();

        dvRecieve.setImageResource(R.drawable.recieve_nfc);
        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer((GifDrawable) dvRecieve.getDrawable());
        dvRecieve.setVisibility(View.VISIBLE);
        isPress = true;
        isUpdate = getIntent().getBooleanExtra("isUpdate", false);
        apkUrl = getIntent().getStringExtra("apkUrl");
        if (isUpdate) {
            showVersionUpdataDialog();
        } else {
            showVersionNoUpdataDialog();
        }
        resolveIntent(getIntent());

        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            return;
        }

        intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true)});
        hint = (TextView) findViewById(R.id.hint);
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    protected void initNfc() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int attachLayoutRes() {
        return 0;
    }

    @Override
    protected void setUpListener() {

    }

    private void showVersionNoUpdataDialog() {
        new AlertDialog.Builder(this)
                .setTitle("版本提示")
                .setMessage("已经是最新版本")
                .setPositiveButton("确定", null)
                .show();
    }

    private void showVersionUpdataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_version_updata);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                pd.show();
                downloadNewApp(apkUrl);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
        return;
    }

    private void downloadNewApp(String apkUrl) {
        Map<String, String> paramsMap = new HashMap<>();
        NetRequestUtil.download(apkUrl, paramsMap, new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), getNameFromUrl(apkUrl)) {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(MainFromNetActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(File response) {
                pd.dismiss();
                installApk(response);
            }

            @Override
            public void inProgress(float progress, long total) {
                int mProgress = (int) (progress * 100);
                pd.setMax(100);
                pd.setProgress(mProgress);

            }
        });
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /**
     * 安装apk 隐式调用系统安装界面
     *
     * @param file
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    public void getBannerUrl() {
        HttpRequest.get("http://m.315cheng.com/banner/getBannerList", new RequestCallback<Banner>() {


            @Override
            public void onResponse(List<Banner> entityList) {
                urlList = entityList;
                if (urlList != null) {
                    Message message = Message.obtain();
                    message.what = INIT_BANNER;
                    handler.sendMessage(message);
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

    public void show() {
   /*     //1、构建Toast对象
        Toast toast = new Toast(this);
// 显示文本

                 TextView tv = new TextView(this);
 tv.setText("哈哈哈");
 tv.setTextColor(Color.RED);
 tv.setBackgroundColor(Color.BLUE);
       *//* //2、构建Toast显示图片
        ImageView tv = new ImageView(this);
        tv.setImageResource(R.drawable.lss);*//*
        //3、设置显示出来的view
        toast.setView(tv);
        //4、设置Toast显示位置 (屏幕顶端中间位置开始算)
        toast.setGravity(Gravity.TOP,100,350);
        //5、设置时常
        toast.setDuration(0);
        //6、显示
        toast.show();*/
    /*    Toast toast = Toast.makeText(getApplicationContext(),
                "自定义位置Toast", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
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
                    Toast.makeText(MainFromNetActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu:
                showPopup(v);
                break;
            case R.id.about:
                startActivity(new Intent(MainFromNetActivity.this, AboutActivity.class));
                break;
            case R.id.ll_qr:
                Intent intent = new Intent(MainFromNetActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    private void showPopup(View view) {
        // 加载pop显示的布局文件
        View contentView = View.inflate(getApplicationContext(),
                R.layout.view_pop, null);
        // 得到pop界面中的控件
        about = (TextView) contentView.findViewById(R.id.about);
        about.setOnClickListener(this);


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
        pw.showAsDropDown(view, 0, 0);
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(2000);// 线程暂停10秒，单位毫秒
                    Message message = new Message();
                    message.what = DISAPPEAR_TOAST;
                    handler.sendMessage(message);// 发送消息
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void initBanner() {
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        vp = (ViewPager) findViewById(R.id.vp);
//        ll_tag = (LinearLayout) findViewById(R.id.ll_tag);
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int screenHeight = metric.heightPixels; // 获取屏幕宽度
        int screenWith = metric.widthPixels; // 获取屏幕宽度
        int height = (int) (screenHeight * 0.3);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rl_banner.getLayoutParams();
        params.width = screenWith;
        params.height = height;
        rl_banner.setLayoutParams(params);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                handler.sendMessage(Message.obtain(handler, MSG_PAGE_CHANGED, position, 0));
//把当前的页数赋值给P
                p = position;
                //得到当前图片的索引,如果图片只有三张，那么只有0，1，2这三种情况
                int currentIndex = (position % urlList.size());
                for (int i = 0; i < tag.length; i++) {
                    if (i == currentIndex) {
                        tag[i].setBackgroundResource(R.drawable.dot_focused);
                    } else {
                        tag[i].setBackgroundResource(R.drawable.dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, 3000);
                        break;
                    default:
                        break;
                }
            }
        });
        //构造一个存储照片的集合
        data = new ArrayList<ImageView>();
        //从网络上把图片下载下来
        if (urlList != null)
            for (int i = 0; i < urlList.size(); i++) {
                getImageFromNet(urlList.get(i).getImgUrl().toString());
                if (i == urlList.size() - 1) {
                    vp.setAdapter(new MypageAdapter(data, MainFromNetActivity.this));
                    //创建小圆点
                    creatTag();
                }

            }
        handler.sendEmptyMessageDelayed(MSG_BREAK_SILENT, 3000);
    }

    private void getImageFromNet(final String imagePath) {
        // TODO Auto-generated method stub
      /*  new Thread() {
            public void run() {
                try {*/
        ScaleScreenImageView iv = new ScaleScreenImageView(MainFromNetActivity.this);
        DisplayMetrics metric = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        int screenWidth = metric.widthPixels; // 获取屏幕宽度
        int height = screenWidth / 3;
        Glide.with(this).load(MyUtils.utf8Togb2312(imagePath)).centerCrop().placeholder(R.mipmap.icon_empty).error(R.mipmap.icon_empty).into(iv);
        data.add(iv);
    }

    protected void creatTag() {
        tag = new ImageView[urlList.size()];
        for (int i = 0; i < urlList.size(); i++) {

            tag[i] = new ImageView(MainFromNetActivity.this);
            //第一张图片画的小圆点是白点
            if (i == 0) {
                tag[i].setBackgroundResource(R.drawable.dot_focused);
            } else {
                //其它的画灰点
                tag[i].setBackgroundResource(R.drawable.dot_normal);
            }
            //设置上下左右的间隔
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            params.setMargins(6, 0, 6, 0);
            tag[i].setLayoutParams(params);
            tag[i].setPadding(20, 0, 20, 0);
//            tag[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //添加到viewpager底部的线性布局里面
            ll_tag.addView(tag[i]);
        }

    }

    private void sendTag(String s) {
        HttpRequest.get(Constant.netUrl + "label/checkLabel?code=" + s, new RequestCallback<Product>() {
            /*  @Override
              public <T> void onResponse(T entity) {
                  System.out.println("onResponse");
                  final Intent intent = new Intent(MainFromNetActivity.this, CertificateResultsActivity.class);
                  Bundle bundle = new Bundle();
                  bundle.putSerializable("mProduct", (Product) entity);
                  intent.putExtras(bundle);
                  MediaPlayer mediaPlayer01;
                  mediaPlayer01 = MediaPlayer.create(getBaseContext(), R.raw.ring);
                  mediaPlayer01.start();
                  startActivity(intent);
                  finish();
              }
  */
            @Override
            public void onEmpty() {
                startActivity(new Intent(MainFromNetActivity.this, CertificateFailed.class));
            }

            @Override
            public void onSuccess() {
                System.out.println("onSuccess" + isPress);
            }

            @Override
            public void onError(Call call, int code) {
                super.onError(call, code);
                System.out.println("onError");
                if (code != 0) {
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onResponse(List<Product> entityList) {
                final Intent intent = new Intent(MainFromNetActivity.this, CertificateResultsActivity.class);
               /* Bundle bundle = new Bundle();
                bundle.putSerializable("mProduct", (Product) entity);
                intent.putExtras(bundle);*/
                intent.putExtra("mProduct", (Serializable) entityList);
                MediaPlayer mediaPlayer01;
                mediaPlayer01 = MediaPlayer.create(getBaseContext(), R.raw.ring);
                mediaPlayer01.start();
                startActivity(intent);
                finish();
            }
        });
    }

    private void showMessage(int title, int message) {
//        mDialog.setTitle(title);
//        mDialog.setMessage(getText(message));
//        mDialog.show();
        DialogHelper.showNoClickDialog(this,title, message);
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);

        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;
            byte[] empty = new byte[0];
            byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Tag mTag = (Tag) tag;
            byte[] mTagId = mTag.getId();
            final String tagId = getHex(mTagId) + "";
            sendTag(tagId);
        }
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

}
