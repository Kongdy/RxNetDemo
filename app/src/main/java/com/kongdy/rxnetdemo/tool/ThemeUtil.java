package com.kongdy.rxnetdemo.tool;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;

import com.kongdy.rxnetdemo.R;


/**
 * @author kongdy
 * @date 2017-04-26 11:13
 * @TIME 11:13
 **/

public class ThemeUtil {

    private static TypedValue value;

    public static int colorPrimary(Context context, int defaultValue){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getColor(context, android.R.attr.colorPrimary, defaultValue);

        return getColor(context, R.attr.colorPrimary, defaultValue);
    }

    private static int getColor(Context context, int id, int defaultValue){
        if(value == null)
            value = new TypedValue();

        try{
            Resources.Theme theme = context.getTheme();
            if(theme != null && theme.resolveAttribute(id, value, true)){
                if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT)
                    return value.data;
                else if (value.type == TypedValue.TYPE_STRING)
                    return context.getResources().getColor(value.resourceId);
            }
        }
        catch(Exception ex){}

        return defaultValue;
    }

    public static int getType(TypedArray array, int index){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return array.getType(index);
        else{
            TypedValue value = array.peekValue(index);
            return value == null ? TypedValue.TYPE_NULL : value.type;
        }
    }

    public static CharSequence getString(TypedArray array, int index, CharSequence defaultValue){
        String result = array.getString(index);
        return result == null ? defaultValue : result;
    }
}
