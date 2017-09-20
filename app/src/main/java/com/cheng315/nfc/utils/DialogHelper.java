package com.cheng315.nfc.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

/**
 *
 *  @author storm_
 *  @date 2017/9/12
 *  @address zq329051@outlook.com
 *  @describe :　 dialog 工具类
 *
 */
public class DialogHelper {


    public static void showNoListenerDialog(Context context, String title, String message) {


        AlertDialog.Builder dialog
                = new AlertDialog.Builder(context);

        dialog.setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();

    }


    /**
     * dialog (此处使用的v4包下的dialog)
     *
     * @param title
     */
    public static void showDialog(Context context, @StringRes int title, final DialogPositiveButtonListener dialogPosListener) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setMessage(title);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialogPosListener != null) {
                    dialogPosListener.onDialogPositiveButtonListener();

                }

            }
        });

        dialog.setNegativeButton("取消", null);
        if (!((Activity) context).isFinishing()) {

            dialog.show();
        }

    }


    public static void showClickDialog(Context context, @StringRes int message, final DialogPositiveButtonListener dialogPositiveButtonListener) {


        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage(message);
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (dialogPositiveButtonListener != null) {
                    dialogPositiveButtonListener.onDialogPositiveButtonListener();

                }

            }
        });

        dialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogPositiveButtonListener != null) {
                    dialogPositiveButtonListener.onDialogNegativeButtonListener();

                }

            }
        });


        dialog.show();


    }


    /**
     * 无点击事件的dialog
     *
     * @param title
     * @param message
     */
    public static void showNoClickDialog(Context context, int title, int message) {


        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(context.getText(message));
        dialog.setNeutralButton("OK", null);
        if (!((Activity) context).isFinishing()) {

            dialog.show();
        }

    }



    public interface DialogPositiveButtonListener {

        void onDialogPositiveButtonListener();

        void onDialogNegativeButtonListener();
    }


    /**
     * 加载数据进度条 (水平风格)
     */
    public static ProgressDialog createProgressDialogH(Context context, @Nullable String dialogTitle) {

        ProgressDialog dialogH = new ProgressDialog(context);

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
