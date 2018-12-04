package com.jq.code.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.jq.code.R;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.util.Locale;

/**
 * Created by hfei on 2015/10/8.
 */
public class CustomTextView extends TextView {

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
    private boolean isChecked = false;
    private int[] checkedRes;

    public CustomTextView(Context context) {
        super(context);
        mContext = context;
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        int textsize = array.getInt(R.styleable.CustomTextView_customtTextSize, 35);
        setTextSize(textsize);
        int typeface = array.getInt(R.styleable.CustomTextView_customtTypeface, LTEX);
        setTypeface(typeface);
        isChecked = array.getBoolean(R.styleable.CustomTextView_customtChecked, false);
        array.recycle();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
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
    }

    /**
     * 设置是否check
     *
     * @param isChecked
     */
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    /**
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


    public void setText(RoleInfo bigText, WeightEntity SmallText) {
        setText(Html.fromHtml(bigText +
                "<small>" + SmallText + "</small>"));
    }

    public void setTextSize(int size) {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, getFontSize(mContext, size));
    }

    public void setTypeface(int typeface) {
        switch (typeface) {
            case CustomTextView.EX:
                setTypeface(getExTypeface(mContext));
                break;
            case CustomTextView.HVCN:
                setTypeface(getHvCnTypeface(mContext));
                break;
            case CustomTextView.LT:
                setTypeface(getLtTypeface(mContext));
                break;
            case CustomTextView.LTEX:
                setTypeface(getLtExTypeface(mContext));
                break;
            case CustomTextView.CONDL:
                setTypeface(getCondLTypeface(mContext));
                break;
            case CustomTextView.CONDM:
                setTypeface(getCondMTypeface(mContext));
                break;
            case CustomTextView.PROL:
                setTypeface(getProLTypeface(mContext));
                break;
            case CustomTextView.PROR:
                setTypeface(getProRTypeface(mContext));
                break;
            default:
                break;
        }
    }

    public static Typeface getProLTypeface(Context paramContext) {
        return Typeface.createFromAsset(paramContext.getAssets(), "fonts/pro_light.otf");
    }

    private static boolean isChinese(Context paramContext) {
        return paramContext.getResources().getConfiguration().locale.getDisplayLanguage()
                .equals(Locale.CHINESE.getDisplayLanguage());
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
        float dividend = 1280 + (screenHeight <= 1280 ? 0 : (screenHeight - 1289) * 0.2f);
        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }
}
