package com.kongdy.rxnetdemo.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongdy.rxnetdemo.R;


/**
 * author:Kongdy
 * Date: 2017-03-14  16：48
 * 弹框帮助类
 */
public class DialogHelper {

    private static Dialog loadingDialog;
    private static Dialog requestLoadingDialog;
    private static AlertDialog mDialog;

    public static void showDialog(Context context,String title, String content, final OnOkClickListener listenerYes,
                                  final OnCancelClickListener listenerNo) {
        showDialog(context,context.getString(android.R.string.ok), context.getString(android.R.string.cancel), title, content, listenerYes, listenerNo);
    }

    public static void showDialog(Context context,String ok, String cancel, String title, String content, final OnOkClickListener listenerYes,
                                  final OnCancelClickListener listenerNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(content);
        // 设置title
        builder.setTitle(title);
        // 设置确定按钮，固定用法声明一个按钮用这个setPositiveButton
        builder.setPositiveButton(ok,(dialog, which) -> {
            // 如果确定被点击
            if (listenerYes != null) {
                listenerYes.onOkClick(dialog);
            }
            mDialog = null;
        });
        // 设置取消按钮，固定用法声明第二个按钮要用setNegativeButton
        if(listenerNo != null) {
            builder.setNegativeButton(cancel,(dialog, which) -> {
                // 如果取消被点击
                listenerNo.onCancelClick(dialog);
                mDialog = null;
            });
        }
        // 控制这个dialog可不可以按返回键，true为可以，false为不可以
        builder.setCancelable(false);
        // 显示dialog
        mDialog = builder.create();
        if(null != mDialog.getWindow())
            mDialog.getWindow().setWindowAnimations(R.style.Me_PopAnimation);
        if (!mDialog.isShowing())
            mDialog.show();
    }

    public static void showDialog(Context context,int ok, int cancel, int title, int content, final OnOkClickListener listenerYes,
                                  final OnCancelClickListener listenerNo) {
        showDialog(context,context.getString(ok), context.getString(cancel), context.getString(title), context.getString(content), listenerYes, listenerNo);
    }

    public static void showDialogForLoading(Context context,String msg, boolean cancelable) {
        if (null == loadingDialog) {
            if (null == context) return;
            View view = LayoutInflater.from(context).inflate(R.layout.layout_def_loading, null);
            TextView loadingText = (TextView)view.findViewById(R.id.loading_text);
            loadingText.setText(msg);
            loadingDialog = new Dialog(context, R.style.Me_LoadingDialog);
            loadingDialog.setCancelable(cancelable);
            loadingDialog.setCanceledOnTouchOutside(cancelable);
            loadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            Activity activity = (Activity) context;
            if (activity.isFinishing()) return;
            loadingDialog.show();
        }
    }

    public static void showDialogForRequestLoading(Context context,String msg, boolean cancelable) {
        if (null == requestLoadingDialog) {
            if (null == context) return;
            View view = LayoutInflater.from(context).inflate(R.layout.layout_def_loading, null);
            TextView loadingText = (TextView)view.findViewById(R.id.loading_text);
            loadingText.setText(msg);
            requestLoadingDialog = new Dialog(context, R.style.Me_LoadingDialog);
            requestLoadingDialog.setCancelable(cancelable);
            requestLoadingDialog.setCanceledOnTouchOutside(cancelable);
            requestLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            Activity activity = (Activity) context;
            if (activity.isFinishing()) return;
            requestLoadingDialog.show();
        }
    }

    public static void stopRequestShowLoading(){
        if (null != requestLoadingDialog && requestLoadingDialog.isShowing()) {
            requestLoadingDialog.dismiss();
        }
        requestLoadingDialog = null;
    }

    /**
     * 结束进度条
     */
    public static void stopProgressDlg() {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
        loadingDialog = null;
    }
    /**
     * 关闭弹框
     */
    public static void stopDialog() {
        if(null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }


    public interface OnOkClickListener {
        void onOkClick(DialogInterface dialog);
    }

    /**
     * Listener
     */
    public interface OnCancelClickListener {
        void onCancelClick(DialogInterface dialog);
    }

}
