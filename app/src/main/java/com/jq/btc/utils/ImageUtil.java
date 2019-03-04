package com.jq.btc.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class ImageUtil {
    private static ImageUtil instance = null;
    private final RequestOptions requestOptions;

    private ImageUtil() {
        requestOptions = new RequestOptions()
                .placeholder(new ColorDrawable(Color.BLACK))
                .error(new ColorDrawable(Color.BLUE))
                .fallback(new ColorDrawable(Color.RED));
    }

    public static ImageUtil getInstance() {
        synchronized (ImageUtil.class) {
            if (instance == null) {
                instance = new ImageUtil();
            }
        }
        return instance;
    }

    //默认图片加载
    public void loadImageView(Context mContext, String path, ImageView mImageView) {
        Glide.with(mContext).load(path).into(mImageView);
    }

    public void loadImageViewLoding(Context mContext, String path, ImageView mImageView, int lodingImage, int errorImageView) {
        Glide.with(mContext)
                .load(path)
                .apply(requestOptions)
                .into(mImageView);

    }
    public void loadImageViewLoding(Context mContext, String path, ImageView mImageView, RequestOptions requestOptions) {
        RoundedCorners roundedCorners= new RoundedCorners(4);
        RequestOptions options=RequestOptions.bitmapTransform(roundedCorners);
        options.placeholder(new ColorDrawable(Color.BLACK));
        options.error(new ColorDrawable(Color.BLUE));
        options.fallback(new ColorDrawable(Color.RED));
        Glide.with(mContext)
                .load(path)
                .apply(options)
                .into(mImageView);

    }

    /**
     * 加载网络图片
     * @param context
     * @param path
     * @param imageView
     * @param glideOptions  自定义图片的属性集，如圆角，占位图等
     */
    public static void display(Context context, String path, ImageView imageView, GlideOptions glideOptions) {
        if (context!=null){
            if (context instanceof Activity) {
                if (((Activity)context).isFinishing() || ((Activity)context).isDestroyed()) {
                    return;
                }
            }
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(glideOptions.getReplaceImage())
                    .error(glideOptions.getErrImage())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE) //缓存策略
                    .centerCrop()
                    .dontAnimate()
                    .optionalTransform(new GlideRoundedCornersTransform(glideOptions.getRadius(), glideOptions.getCornerType()));
            Glide.with(context)
                    .load(path)
                    .apply(requestOptions)//图片是圆形的
                    .into(imageView);
        }
    }

    /**
     * 加载本地资源文件
     * @param context
     * @param
     * @param imageView
     * @param glideOptions
     */
    public static void display(Context context, Integer resourceId, ImageView imageView, GlideOptions glideOptions) {
        if (context!=null){
            if (context instanceof Activity) {
                if (((Activity)context).isFinishing() || ((Activity)context).isDestroyed()) {
                    return;
                }
            }
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(glideOptions.getReplaceImage())
                    .error(glideOptions.getErrImage())
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .dontAnimate()
                    .optionalTransform(new GlideRoundedCornersTransform(glideOptions.getRadius(), glideOptions.getCornerType()));
            Glide.with(context)
                    .load(resourceId)
                    .apply(requestOptions)//图片是圆形的
                    .into(imageView);
        }
    }
    public static void playGif(Context context, int resourceid, ImageView imageView){
        Glide.with(context).asGif().load(resourceid).into(imageView);

    }
    public static void stopGif(Context context, ImageView imageView){
        Glide.with(context).clear(imageView);
    }

    public static boolean isContextExisted(Context context) {
        if (context != null) {
            if (context instanceof Activity) {
                if (!((Activity)context).isFinishing() && !((Activity)context).isDestroyed()) {
                    return true;
                }
            }
        }
        return false;
    }
}
