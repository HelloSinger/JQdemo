package com.jq.code.code.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 获得屏幕相关的辅助类
 *
 * @author zhy
 */
public class ScreenUtils {

    /**
     * 屏幕尺寸比例表
     */
    private static int[][] sizes = new int[][]{{4, 3}, {3, 2},
            {16, 10}, {17, 10}, {16, 9}};

    /**
     * 显示软键盘
     *
     * @param view
     */
    public static void showSoftInput(View view) {
        view.requestFocus();
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftKeyboard(EditText editText) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(EditText editText) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, 0);
        }
    }

    /**
     * 获取虚拟键盘高度
     *
     * @param context
     * @return
     */
    public static int getVirtualHeight(Activity context) {
        int virtual = 0;
        int height = context.getWindowManager().getDefaultDisplay().getHeight();
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            virtual = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return virtual - height;
    }

    /**
     * 获取屏幕比例
     *
     * @return
     */
    public static String getScreenMode(Context context) {
        int x = getScreenWidth(context);
        int y = getScreenHeight(context);
        double curSize = y / x;
        double tmpSum = Math.abs(curSize - sizes[0][0] / sizes[0][1]);
        int index = 0;
        for (int i = 1; i < sizes.length; i++) {
            double sum = Math.abs(curSize - sizes[i][0] / sizes[i][1]);
            tmpSum = Math.min(tmpSum, sum);
            if (Double.compare(tmpSum, sum) == 0) {
                index = i;
            }
        }
        return sizes[index][0] + "-" + sizes[index][1];
    }

    public static long lastClickTime;

    public static boolean isFastClick(long limitTime) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < limitTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    // 获取指定Activity的截屏，保存到png文件
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 保存图片到文件中
     *
     * @param b
     * @param strFileName
     */
    private static void storeShotToFile(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            File file = new File(FileUtil.PATH_PHOTO_SHARE);
            if (!file.exists()) {
                file.mkdir();
            }
            File file2 = new File(FileUtil.PATH_PHOTO_SHARE + strFileName);
            if (!file2.exists()) {
                file2.createNewFile();
            }
            fos = new FileOutputStream(FileUtil.PATH_PHOTO_SHARE + strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(Context context) {
        if (context instanceof Activity) {
            InputMethodManager imm = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(((Activity) context).getWindow()
                        .getDecorView().getWindowToken(), 0);
            }
        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置屏幕全屏状态栏透明并跟随背景颜色变化状态栏文字颜色
     *
     * @param activity
     */
    public static void setScreenFullStyle(Activity activity, int statusColor) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT_WATCH) {
            return;
        }
        Window window = activity.getWindow();
        //设置透明状态栏,这样才能让 ContentView 向上
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
            ViewCompat.setFitsSystemWindows(mChildView, false);
        }
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    /**
     * 截取scrollview的屏幕
     *
     * @param scrollView
     * @return
     */
    public static Bitmap getBitmapByView(ScrollView scrollView) {
        int h = 0;
        Bitmap bitmap = null;
        // 获取scrollview实际高度
        for (int i = 0; i < scrollView.getChildCount(); i++) {
            h += scrollView.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(scrollView.getWidth(), h,
                Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    /**
     * 压缩图片
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 100) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 一键截图
     *
     * @param a
     */
    public static String oneKeyShoot(Activity a) {
        String path = TimeUtil.getCurrentTime("yyyyMMddHHmmss") + ".png";
        storeShotToFile(takeScreenShot(a), path);
        return FileUtil.PATH_PHOTO_SHARE + path;
    }

    /**
     * @param view
     * @return
     */
    public static String oneKeyShoot(ScrollView view) {
        String path = TimeUtil.getCurrentTime("yyyyMMddHHmmss") + ".png";
        storeShotToFile(compressImage(getBitmapByView(view)), path);
        return FileUtil.PATH_PHOTO_SHARE + path;
    }
}
