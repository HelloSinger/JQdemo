package com.jq.code.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;


import com.jq.code.R;

import java.util.Locale;

/**
 * Created by hfei on 2015/10/8.
 */
public class MarqueeTextView extends TextView implements View.OnTouchListener {

    /**
     * 四种字体风格
     */
    public static final int EX = 0;
    public static final int HVCN = 1;
    public static final int LTEX = 2;
    public static final int LT = 3;
    public static final int CONDL = 4;
    public static final int CONDM = 5;
    public static final int PROL = 6;
    public static final int PROR = 7;

    protected Context mContext;
    private int textsize;
    private int typeface;
    private boolean clickBackgound = false;
    private int pressedTextColor;
    boolean isInvalid = false;

    public MarqueeTextView(Context context) {
        super(context);
        mContext = context;
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        textsize = array.getInt(R.styleable.CustomTextView_customtTextSize, 35);
        typeface = array.getInt(R.styleable.CustomTextView_customtTypeface, LTEX);
        clickBackgound = array.getBoolean(R.styleable.CustomTextView_customClickBackgound, false);
        pressedTextColor = array.getColor(R.styleable.CustomTextView_customTextPressedColor, 0);
        isInvalid = array.getBoolean(R.styleable.CustomTextView_customTextInvalid, false);
        array.recycle();
        init(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    boolean canLarger = true;
    boolean canSmaller = true;
    ScaleAnimation scaleSmaller = new ScaleAnimation(1.08f, 1f, 1.08f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    ScaleAnimation scaleLarger = new ScaleAnimation(1, 1.08f, 1, 1.08f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    AlphaAnimation alphaSmaller = new AlphaAnimation(1, 0.5f);
    AlphaAnimation alphaLarger = new AlphaAnimation(0.5f, 1);

    public void scaleLarger() {
        if (canLarger) {
            canLarger = false;
            canSmaller = true;
            animation(scaleLarger, alphaLarger);
        }
    }

    public void scaleSmaller() {
        if (canSmaller) {
            canSmaller = false;
            canLarger = true;
            animation(scaleSmaller, alphaSmaller);
        }
    } /**
     * 播放动画
     *
     * @param animation
     * @param alpha
     */
    private void animation(Animation animation, AlphaAnimation alpha) {
        AnimationSet animationSet = new AnimationSet(true);
        animation.setDuration(200);
        animationSet.addAnimation(animation);
        animationSet.addAnimation(alpha);
        animationSet.setFillAfter(true);
        super.startAnimation(animationSet);
    }
    private void init(Context context) {

        mContext = context;
        setTextSize(textsize);
        setTypeface(typeface);
        if (clickBackgound) {
            setOnTouchListener(this);
        }
    }

    public void setTextSize(int size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getFontSize(mContext, size));
    }

    public void setTypeface(int typeface) {
        switch (typeface) {
            case MarqueeTextView.EX:
                setTypeface(getExTypeface(mContext));
                break;
            case MarqueeTextView.HVCN:
                setTypeface(getHvCnTypeface(mContext));
                break;
            case MarqueeTextView.LT:
                setTypeface(getLtTypeface(mContext));
                break;
            case MarqueeTextView.LTEX:
                setTypeface(getLtExTypeface(mContext));
                break;
            case MarqueeTextView.CONDL:
                setTypeface(getCondLTypeface(mContext));
                break;
            case MarqueeTextView.CONDM:
                setTypeface(getCondMTypeface(mContext));
                break;
            case MarqueeTextView.PROL:
                setTypeface(getProLTypeface(mContext));
                break;
            case MarqueeTextView.PROR:
                setTypeface(getProRTypeface(mContext));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawwLine(canvas);
    }

    private void drawwLine(Canvas canvas) {
        if (isInvalid) {
            TextPaint paint = getPaint();
            paint.setStrokeWidth(3.0f);
            canvas.drawLine(0, getHeight() / 2, getWidth(), (getHeight() / 2), paint);
        }
    }

    public static Typeface getProLTypeface(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/pro_light.otf");
    }

    private static boolean isChinese(Context paramContext) {
        return paramContext.getResources().getConfiguration().locale.getDisplayLanguage().equals(
                Locale.CHINESE.getDisplayLanguage());
    }

    public Typeface getProRTypeface(Context paramContext) {
        return Typeface.createFromAsset(this.mContext.getAssets(), "fonts/pro_regular.otf");
    }

    public static Typeface getCondLTypeface(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/cond_light.otf");
    }

    public static Typeface getCondMTypeface(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/cond_medium.otf");
    }

    /**
     * EX字体风格
     *
     * @return
     */
    public static Typeface getExTypeface(Context context) {
        if (!isChinese(context)) {
            return Typeface
                    .createFromAsset(context.getAssets(), "fonts/ex.otf");
        }
        return Typeface.SANS_SERIF;
    }

    /**
     * HVCN字体风格
     *
     * @return
     */
    public static Typeface getHvCnTypeface(Context context) {
        if (!isChinese(context)) {
            return Typeface.createFromAsset(context.getAssets(),
                    "fonts/hvcn.otf");
        }
        return Typeface.SANS_SERIF;
    }

    /**
     * LTEX字体风格
     *
     * @return
     */
    public static Typeface getLtExTypeface(Context context) {
        if (!isChinese(context)) {
            return Typeface.createFromAsset(context.getAssets(),
                    "fonts/ltex.otf");
        }
        return Typeface.SANS_SERIF;
    }

    /**
     * LT字体风格
     *
     * @return
     */
    public static Typeface getLtTypeface(Context context) {
        if (!isChinese(context)) {
            return Typeface
                    .createFromAsset(context.getAssets(), "fonts/lt.otf");
        }
        return Typeface.SANS_SERIF;
    }


    /**
     * 获取字体大小
     *
     * @param context
     * @param textSize 被计算字体大小值
     * @return 计算后的字体大小值
     */
    private int getFontSize(Context context, int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        // screenWidth = screenWidth > screenHeight ?screenWidth :screenHeight;
        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }
    @Override
    public boolean isFocused() {
        return true;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getBackground() != null) {
                    getBackground().setAlpha(210);
                }
                if (pressedTextColor != 0) {
                    setTextColor(pressedTextColor);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getBackground() != null) {
                    getBackground().setAlpha(255);
                }
                if (pressedTextColor != 0) {
                    setTextColor(getTextColors());
                }
                break;
        }
        return false;
    }
}
