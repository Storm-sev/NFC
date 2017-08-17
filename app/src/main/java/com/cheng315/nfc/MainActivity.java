package com.cheng315.nfc;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
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

import com.bumptech.glide.Glide;
import com.cheng315.nfc.adapter.MypageAdapter;
import com.cheng315.nfc.entity.Banner;
import com.cheng315.nfc.entity.Product;
import com.cheng315.nfc.utils.AppUtil;
import com.cheng315.nfc.utils.DialogHelper;
import com.cheng315.nfc.utils.LogUtils;
import com.cheng315.nfc.view.ScaleScreenImageView;
import com.efeiyi.BaseActivity;
import com.efeiyi.Constant;
import com.efeiyi.net.HttpRequest;
import com.efeiyi.net.NetRequestUtil;
import com.efeiyi.net.RequestCallback;
import com.efeiyi.utils.MyUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends BaseActivity {

    private AnimationDrawable animationDrawable;
    private static final DateFormat TIME_FORMAT = SimpleDateFormat.getDateTimeInstance();
    private LinearLayout mTagContent;
    //  private SimpleDraweeView dvRecieve;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private boolean isPress = false;
    private List<ImageView> data;
    //存放代表viewpager播到第几张的小圆点
    private LinearLayout ll_tag;
    //存储小圆点的一维数组
    private ImageView tag[];
    private static final int MSG_KEEP_SILENT = 4;
    private static final int MSG_UPDATE_IMAGE = 3;
    private static final int MSG_BREAK_SILENT = 5;
    private static final int MSG_PAGE_CHANGED = 6;
    private static final int INIT_BANNER = 7;
    private int currentItem = 0;
    int p = 0;
    private ViewPager vp;
    private List<Banner> urlList;
    private RelativeLayout rl_banner;
    private TextView about;
    private ProgressDialog pd;
    private String apkUrl;

    private AlertDialog mDialog;

    private DialogHelper mDialogHelper;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (handler.hasMessages(MSG_UPDATE_IMAGE)) {
                handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case 1:
                    showMessage(R.string.error, R.string.no_net);
                    break;
                case 2:
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
                case INIT_BANNER:
                    initBanner();
                    break;
//                default:
//                    break;
            }

        }
    };
    private TextView send;
    private TextView hint;
    private String uri;
    private Intent intent;
    private GifImageView dvRecieve;
    private static final int REQUEST_CODE = 101;
    private ImageView menu;
    private PopupWindow pw;
    private LinearLayout llQrCode;
    private String tagId;
    private boolean isUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
        getBannerUrl();
        setUpListener();
        resolveIntent(getIntent());


        mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", null).create();

        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
//            finish();
            return;
        }

        intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intent
                , 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{newTextRecord(
                "Message from NFC Reader :-)", Locale.ENGLISH, true)});
    }

    private void initData() {

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

        resolveIntent(getIntent());

    }

    @Override
    protected void onStart() {
        super.onStart();
        tagId = null;
    }

    /**
     * 监听
     */
    private void setUpListener() {

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });

        //
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
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isPress = true;
                    dvRecieve.setVisibility(View.VISIBLE);
                    hint.setVisibility(View.GONE);
                    send.setBackgroundResource(R.drawable.shape_button_press);
                    send.setTextColor(getResources().getColor(R.color.white));
                    if (tagId != null)
                        sendTag(tagId);
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    isPress = false;
                    dvRecieve.setVisibility(View.GONE);
                    hint.setVisibility(View.VISIBLE);
                    send.setBackgroundResource(R.drawable.shape_button);
                    send.setTextColor(getResources().getColor(R.color.black));
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    isPress = false;
                    send.setBackgroundResource(R.drawable.shape_button);
                    send.setTextColor(getResources().getColor(R.color.black));
                    dvRecieve.setVisibility(View.GONE);
                    hint.setVisibility(View.VISIBLE);
                    return true;
                }
                return true;
            }
        });


    }

    private void initViews() {
        send = (TextView) findViewById(R.id.send);
        hint = (TextView) findViewById(R.id.hint);
        menu = (ImageView) findViewById(R.id.menu);
        llQrCode = (LinearLayout) findViewById(R.id.ll_qr);
        dvRecieve = (GifImageView) findViewById(R.id.iv_recieve);
        dvRecieve.setImageResource(R.drawable.recieve_nfc);

        MediaController mediaController = new MediaController(this);
        mediaController.setMediaPlayer((GifDrawable) dvRecieve.getDrawable());

        mDialogHelper = new DialogHelper(this);

    }

    private void showVersionNoUpdataDialog() {
        new AlertDialog.Builder(this)
                .setTitle("版本提示")
                .setMessage("已经是最新版本")
                .setPositiveButton("确定", null)
                .show();
    }

    private void showVersionUpdataDialog() {

        mDialogHelper.showDialog(R.string.nfc_version_updata);
        mDialogHelper.setDialogPositiveButtonListener(new DialogHelper.DialogPositiveButtonListener() {
            @Override
            public void onDialogPositiveButtonListener() {

                downloadNewApp(apkUrl);
            }
        });

        return;
    }

    private void downloadNewApp(String apkUrl) {

        final ProgressDialog dialogH = mDialogHelper.createProgressDialogH(null);
        dialogH.show();

        Map<String, String> paramsMap = new HashMap<>();
        NetRequestUtil.download(apkUrl, paramsMap, new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), AppUtil.getNameFromUrl(apkUrl)) {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(File response) {
                dialogH.dismiss();
                AppUtil.installApp(MainActivity.this, response);
            }

            @Override
            public void inProgress(float progress, long total) {
                int mProgress = (int) (progress * 100);
                dialogH.setMax(100);
                dialogH.setProgress(mProgress);

            }
        });
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

    private void initBanner() {
        rl_banner = (RelativeLayout) findViewById(R.id.rl_banner);
        vp = (ViewPager) findViewById(R.id.vp);
        ll_tag = (LinearLayout) findViewById(R.id.ll_tag);
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
                    vp.setAdapter(new MypageAdapter(data, MainActivity.this));
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
        ScaleScreenImageView iv = new ScaleScreenImageView(MainActivity.this);
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

            tag[i] = new ImageView(MainActivity.this);
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

            @Override
            public void onEmpty() {
                if (isPress) {
                    MediaPlayer mediaPlayer01;
                    mediaPlayer01 = MediaPlayer.create(getBaseContext(), R.raw.ring);
                    mediaPlayer01.start();
                    startActivity(new Intent(MainActivity.this, CertificateFailed.class));
//                    finish(); //
                }

            }

            @Override
            public void onError(Call call, int code) {
//                super.onError(call, code);
                System.out.println("onError");
                if (code != 0) {
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onResponse(List<Product> entityList) {
                final Intent intent = new Intent(MainActivity.this, CertificateResultsActivity.class);
//                Bundle bundle = new Bundle();
               /* bundle.putSerializable("mProduct", entityList);
                intent.putExtras(bundle);*/
                intent.putExtra("mProduct", (Serializable) entityList);
                if (isPress) {
                    MediaPlayer mediaPlayer01;
                    mediaPlayer01 = MediaPlayer.create(getBaseContext(), R.raw.ring);
                    mediaPlayer01.start();
                    startActivity(intent);
//                    finish();
                }
            }

            @Override
            public void onSuccess() {

            }
        });
    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
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
                Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
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

    // 判断是nfc卡的类型
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
            tagId = getHex(mTagId) + "";
            sendTag(tagId);
//            byte[] payload = dumpTagData(tag).getBytes();
//            NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
//            NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
//            msgs = new NdefMessage[]{msg};
          /*  if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }*/
            // Setup the views
//            buildTagViews(msgs);
        }
    }

    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');

                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');

                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');

                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        /*    if (i > 0) {
                sb.append(" ");
            }*/
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

   /* void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout content = mTagContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        Date now = new Date();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            TextView timeView = new TextView(this);
            timeView.setText(TIME_FORMAT.format(now));
            content.addView(timeView, 0);
            ParsedNdefRecord record = records.get(i);
            content.addView(record.getView(this, inflater, content, i), 1 + i);
            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i);
        }
    }*/


    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);//更新intent
        resolveIntent(intent);
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
}
