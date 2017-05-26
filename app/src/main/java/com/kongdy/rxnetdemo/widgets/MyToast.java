package com.kongdy.rxnetdemo.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongdy.rxnetdemo.R;


/**
 * @author kongdy
 * @date 2017-04-24 18:45
 * @TIME 18:45
 * 提示框
 **/
public class MyToast {
    private static MyToast myToast;
    private static Dialog mDialog;
    private Toast toast;

    /**
     * 简短显示
     **/
    public static final int shortShow = 0;
    /**
     * 一直显示，直到触摸取消
     **/
    public static final int alwaysShow = 1;

    private long lastShowTime;

    private MyToast() {
    }

    public static void show(Context context, CharSequence msg, boolean suc, int showTime) {
        if (myToast == null)
            myToast = new MyToast();
        if (showTime == shortShow)
            showToast(context, msg, suc);
        else
            showDialog(context, msg, suc);
        myToast.lastShowTime = System.currentTimeMillis();
    }

    private static void showDialog(Context context, CharSequence msg, boolean suc) {
        mDialog  = new Dialog(context, R.style.Me_LoadingDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.view_toast_dialog,null);
        TextView tv = (TextView) view.findViewById(R.id.tv_msg);
        tv.setText(msg);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setContentView(view,new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        if(context instanceof Activity && ((Activity)context).isFinishing())
            return;
        mDialog.show();
    }

    private static void showToast(Context context, CharSequence msg, boolean suc) {
        myToast.toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        myToast.toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) myToast.toast.getView();
        toastView.removeAllViews();
        toastView.setOrientation(LinearLayout.VERTICAL);
        toastView.setGravity(Gravity.CENTER);
        toastView.setBackgroundResource(R.drawable.shape_radius_white_bg);
        int dp_15 = (int) context.getResources().getDimension(R.dimen.dp_15);
        int minWidth = (int) context.getResources().getDimension(R.dimen.dp_200);
        int minHeight = (int) context.getResources().getDimension(R.dimen.dp_80);
        toastView.setPadding(dp_15, dp_15, dp_15, dp_15);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(context);
        tv.setText(msg);
        tv.setTextColor(context.getResources().getColor(R.color.text_black));
        toastView.addView(tv, 0, layoutParams);
        toastView.setMinimumHeight(minHeight);
        toastView.setMinimumWidth(minWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            toastView.setElevation(dp_15);
        myToast.toast.show();
    }

    public static boolean isShortShow() {
        return !(myToast == null || myToast.toast == null) && System.currentTimeMillis() -
                myToast.lastShowTime < 3000;
    }

    public static boolean isAlwaysShow() {
        return myToast.mDialog == null || !myToast.mDialog.isShowing();
    }

    public static void dismissAlways() {
        if(null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
