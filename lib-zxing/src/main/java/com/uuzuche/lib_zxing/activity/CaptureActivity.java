package com.uuzuche.lib_zxing.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;

import com.uuzuche.lib_zxing.R;

/**
 * Initial the camera
 *
 * 默认的二维码扫描Activity
 */
public class CaptureActivity extends Activity {
    private static final int REQUEST_CODE_CAMERA = 999;
    private static OnCameraPermissionListener mOnCameraPermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        CaptureFragment captureFragment = new CaptureFragment();
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getFragmentManager().beginTransaction().replace(R.id.fl_zxing_container, captureFragment).commit();
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            CaptureActivity.this.setResult(RESULT_OK, resultIntent);
            CaptureActivity.this.finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // 检查版本
        checkCameraPermission(this, mOnCameraPermissionListener);
    }

    /**
     * 6.0权限申请
     * @param context
     * @param listener
     * @return
     */
    public boolean checkCameraPermission(Context context, OnCameraPermissionListener listener) {
        mOnCameraPermissionListener = listener;
        boolean granted = true;
        //魅族或者6.0以下
        if (isFlyme() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Camera mCamera = null;
            try {
                mCamera = Camera.open();
                // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
                // 对象不为null
                Camera.Parameters mParameters = mCamera.getParameters();
                mCamera.setParameters(mParameters);
            } catch (Exception e) {
                granted = false;
            }
            if (mCamera != null) {
                mCamera.release();
            }
        } else {
            granted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED;
        }
        if (granted) {
            if (mOnCameraPermissionListener != null) {
                mOnCameraPermissionListener.onGrantResult(true);
            }
        } else {
            if (context instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                showWirelessSettingsDialog();
            }
        }
        return granted;
    }
    private static boolean isFlyme() {
        if (Build.BRAND.contains("Meizu")) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (mOnCameraPermissionListener != null) {
                mOnCameraPermissionListener.onGrantResult(checkCameraPermission(this,null));
            }

        }
    }
    public interface OnCameraPermissionListener {
        void onGrantResult(boolean granted);
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.camera_open);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
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
}