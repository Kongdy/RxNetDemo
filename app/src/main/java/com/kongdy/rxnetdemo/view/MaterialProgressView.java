package com.kongdy.rxnetdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kongdy.rxnetdemo.R;
import com.kongdy.rxnetdemo.tool.ThemeUtil;
import com.kongdy.rxnetdemo.tool.Util;


/**
 * @author kongdy
 *      2017-04-25 17:49 17:49
 **/
public class MaterialProgressView extends RelativeLayout implements Animatable{

    final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    int[] mStrokeColors;

    private int mStrokeColorIndex;

    private Paint transparentPaint;
    private Paint strokePaint;

    private int mRunState = RUN_STATE_STOPPED;

    private static final int RUN_STATE_STOPPED = 0;
    private static final int RUN_STATE_RUNNING = 3;


    public MaterialProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MaterialProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttributes(attrs);
    }

    public MaterialProgressView(Context context) {
        this(context,null);
    }

    // Set atributtes of XML to View
    protected void setAttributes(AttributeSet attrs){
        setMinimumHeight(Util.dpToPx(32, getResources()));
        setMinimumWidth(Util.dpToPx(32, getResources()));

        //Set background Color
        // Color by resource
        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML,"background",-1);
        if(bacgroundColor != -1){
            setBackgroundColor(getResources().getColor(bacgroundColor));
        }else{
            // Color by hexadecimal
            int background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1)
                setBackgroundColor(background);
            else
                setBackgroundColor(Color.parseColor("#1E88E5"));
        }

        setMinimumHeight(Util.dpToPx(3, getResources()));

        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MaterialProgressView);

        int strokeColor = 0;
        boolean singleColorDefined = false;
        int[] strokeColors = null;

        for(int i = 0,count = ta.getIndexCount();i < count;i++) {
            int attr = ta.getIndex(i);
            if(attr == R.styleable.MaterialProgressView_mpv_stroke_color) {
                strokeColor = ta.getColor(attr, ThemeUtil.colorPrimary(getContext(),0xFF000000));
                singleColorDefined = true;
            } else if(attr == R.styleable.MaterialProgressView_mpv_stroke_colors) {
                TypedArray a = getContext().getResources().obtainTypedArray(ta.getResourceId(attr,0));
                strokeColors = new int[a.length()];
                for (int j = 0;j < a.length();j++)
                    strokeColors[j] = a.getColor(j,0);
                a.recycle();
            }
        }

        ta.recycle();

        if(strokeColors != null)
            mStrokeColors = strokeColors;
        else if(singleColorDefined)
            mStrokeColors = new int[]{strokeColor};

        if(mStrokeColorIndex >= mStrokeColors.length)
            mStrokeColorIndex = 0;

        transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);

        start();
    }

    /**
     * Make a dark color to ripple effect
     * @return
     */
    protected int makePressColor(){
        int r = (this.mStrokeColors[0] >> 16) & 0xFF;
        int g = (this.mStrokeColors[0] >> 8) & 0xFF;
        int b = (this.mStrokeColors[0]) & 0xFF;
//		r = (r+90 > 245) ? 245 : r+90;
//		g = (g+90 > 245) ? 245 : g+90;
//		b = (b+90 > 245) ? 245 : b+90;
        return Color.argb(128,r, g, b);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!firstAnimationOver)
            drawFirstAnimation(canvas);
        if(cont > 0)
            drawSecondAnimation(canvas);
        if(mRunState == RUN_STATE_RUNNING)
            invalidate();
    }

    float radius1 = 0;
    float radius2 = 0;
    int cont = 0;
    boolean firstAnimationOver = false;
    /**
     * Draw first animation of view
     * @param canvas
     */
    private void drawFirstAnimation(Canvas canvas){
        if(radius1 < getWidth()/2){
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            radius1 = (radius1 >= getWidth()/2)? (float)getWidth()/2 : radius1+1;
            canvas.drawCircle(getWidth()/2, getHeight()/2, radius1, paint);
        }else{
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);
            strokePaint.setColor(makePressColor());
            temp.drawCircle(getWidth()/2, getHeight()/2, getHeight()/2, strokePaint);
            if(cont >= 50){
                radius2 = (radius2 >= getWidth()/2)? (float)getWidth()/2 : radius2+1;
            }else{
                radius2 = (radius2 >= getWidth()/2-Util.dpToPx(4, getResources()))? (float)getWidth()/2-Util.dpToPx(4, getResources()) : radius2+1;
            }
            temp.drawCircle(getWidth()/2, getHeight()/2, radius2, transparentPaint);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
            if(radius2 >= getWidth()/2-Util.dpToPx(4, getResources()))
                cont++;
            if(radius2 >= getWidth()/2)
                firstAnimationOver = true;
        }
    }

    int arcD = 1;
    int arcO = 0;
    float rotateAngle = 0;
    int limite = 0;
    /**
     * Draw second animation of view
     * @param canvas
     */
    private void drawSecondAnimation(Canvas canvas){
        if(arcO == limite)
            arcD+=6;
        if(arcD >= 290 || arcO > limite){
            arcO+=6;
            arcD-=6;
        }
        if(arcO > limite + 290){
            limite = arcO;
            arcO = limite;
            arcD = 1;
        }
        rotateAngle += 4;
        canvas.rotate(rotateAngle,getWidth()/2, getHeight()/2);

        if( arcD == 1 && mStrokeColorIndex < mStrokeColors.length-1)
            mStrokeColorIndex ++;

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
        strokePaint.setColor(mStrokeColors[mStrokeColorIndex]);
//		temp.drawARGB(0, 0, 0, 255);
        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD, true, strokePaint);
        temp.drawCircle(getWidth()/2, getHeight()/2, (getWidth()/2)-Util.dpToPx(4, getResources()), transparentPaint);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    @Override
    public void start() {
        if(isRunning())
            return;
        mRunState = RUN_STATE_RUNNING;
        invalidate();
    }

    @Override
    public void stop() {
        if(!isRunning())
            return;
        mRunState = RUN_STATE_STOPPED;
        invalidate();
    }

    @Override
    public boolean isRunning() {
        return mRunState == RUN_STATE_RUNNING;
    }
}
