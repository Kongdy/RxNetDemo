package com.kongdy.rxnetdemo.tool;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kongdy
 * @date 2017-04-24 17:30
 * @TIME 17:30
 * 工具类
 **/
public class Util {

    private final static String TAG = "Util";

    // 手机分辨率宽高
    public static int screenHeight_;
    public static int screenWidth_;

    private static String ChineseRegEx = "[\u4e00-\u9fa5]";

    /**
     * 获取屏幕分辨率
     * @param context
     */
    public static void windowScreenSize(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        display.getMetrics(outMetrics);
        screenHeight_ = outMetrics.heightPixels;
        screenWidth_ = outMetrics.widthPixels;
    }

    /**
     * 限制文本长度(中文2,字母1)
     */
    public static InputFilter getInputFilter(final int maxLength) {
        return (source,start,end,dest,dstart,dend)-> {
            /*
                  source：表示从缓冲区即将带来的新文本
                  start:表示在文字之后，新文本要放置的开始的坐标，
                            ep. 当前文本长度为6，新输入一个字母放到当前文本之后，那么start为0，end为1
                  end：表示在文字之后，新文本要放置的结束的坐标
                  dest:当前已经存在的文字
                  dstart:要被替换的开始坐标
                  dend:要被替换的结束坐标
                 */
            int dIndex = 0; // 遍历的文本长度(英文/中文未区分)
            int destCount = 0; // 已显示文本的长度
            int sourceCount = 0; // 即将到来的缓冲区文本长度
            int myDend = 0;
            int myDstart = 0;
            // 计算已经存在的文本长度
            while (destCount <= maxLength && dIndex < dest.length()) {
                char c = dest.charAt(dIndex);
                if (c > 0x80) {
                    destCount += 2;
                } else {
                    destCount += 1;
                }
                if (dIndex == dstart - 1 && myDstart == 0) {
                    myDstart = destCount;
                }
                if (dIndex == dend - 1 && myDend == 0) {
                    myDend = destCount;
                }
                dIndex++;
            }

            int sIndex = 0;
            int myStart = 0;
            int myEnd = 0;
            int rangeCount = 0;

            int keep = maxLength - (destCount - (myDend - myDstart));

            while (sIndex < source.length()) {
                char c = source.charAt(sIndex);
                if (c > 0x80) {
                    sourceCount += 2;
                } else {
                    sourceCount += 1;
                }
                if (sIndex == start - 1 && myStart == 0) {
                    myStart = sourceCount;
                }
                if (sIndex == end - 1 && myEnd == 0) {
                    myEnd = sourceCount;
                }
                if (sourceCount == keep) {
                    rangeCount = sIndex + 1;
                }
                sIndex++;
            }

            if (keep <= 0) {
                return "";
            } else if (keep >= myEnd - myStart) {
                return null; // keep original
            } else if (keep <= sourceCount) {
                return source.subSequence(myStart, rangeCount);
            }

            return source.subSequence(0, dIndex);
        };
    }


    /**
     * 判断是否有虚拟按键
     *
     * @param context .
     * @return .
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }
        return hasNavigationBar;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean flymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 替换完字符里所有空白之后，判断是否字符串是否由全0组成
     * @param str
     * @return
     */
    public static boolean isAllZero(String str) {
        if(TextUtils.isEmpty(str)) {
            return false;
        }
        String str2 = str.trim().replace(" ", "");
        String regex = "^0+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str2);
        return matcher.matches();
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    /**
     * check the time weather is today
     * @param time check time
     * @return true: isToday false: not today
     */
    public static boolean isToday(Long time) {
        if(time == null)
            time = 0L;
        Calendar nowCalendar = Calendar.getInstance();
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTimeInMillis(time);
        return nowCalendar.get(Calendar.YEAR) == timeCalendar.get(Calendar.YEAR)
                 && nowCalendar.get(Calendar.DAY_OF_YEAR) == timeCalendar.get(Calendar.DAY_OF_YEAR);
    }

}
