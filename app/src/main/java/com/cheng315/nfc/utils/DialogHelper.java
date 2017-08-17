package com.cheng315.nfc.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

/**
 * Created by Administrator on 2017/8/16.
 * dialog 统一工具类
 */

public class DialogHelper {

    private Context mContext;

    private DialogPositiveButtonListener mListener;


    public DialogHelper(Context context) {
        this.mContext = context;
    }


    /**
     * dialog (此处使用的v4包下的dialog)
     *
     * @param title
     */
    public void showDialog(@StringRes int title) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);

        dialog.setMessage(title);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (mListener != null) {
                    mListener.onDialogPositiveButtonListener();
                }

            }
        });

        dialog.setNegativeButton("取消", null);
        dialog.show();

    }


    public void setDialogPositiveButtonListener(DialogPositiveButtonListener listener) {

        this.mListener = listener;

    }

    public interface DialogPositiveButtonListener {

        void onDialogPositiveButtonListener();

    }


    /**
     * 水平加载进度条
     */
    public ProgressDialog createProgressDialogH(@Nullable String dialogTitle) {

        ProgressDialog dialogH = new ProgressDialog(mContext);
        dialogH.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        if (null == dialogTitle) {
            dialogH.setTitle(null);
        } else {
            dialogH.setTitle(dialogTitle);
        }
        dialogH.setCanceledOnTouchOutside(false);

        return dialogH;


    }


}
