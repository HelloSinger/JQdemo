package com.jq.code.code.shareSDK;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.jq.code.R;
import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QQClientNotExistException;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ShareImp implements PlatformActionListener {

    private Context context;

    public ShareImp(Context context) {
        this.context = context;
        ShareSDK.initSDK(context);
    }

    /**
     * 微信
     *
     * @param fileName
     */
    public void wechart(String fileName) {
        Platform.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setImagePath(fileName);
        sp.setText(context.getString(R.string.share_text));
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_IMAGE);
        execute(ShareSDK.getPlatform(Wechat.NAME), sp);
    }

    /**
     * 微信朋友圈
     */
    public void wechatMoments(String fileName) {
        Platform.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setImagePath(fileName);
        sp.setText(context.getString(R.string.share_text));
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(Platform.SHARE_IMAGE);
        execute(ShareSDK.getPlatform(WechatMoments.NAME), sp);
    }

    /**
     * 新浪微博
     *
     * @param fileName
     */
    public void sinaWeibo(String fileName) {
        Platform.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setImagePath(fileName);
        sp.setText(context.getString(R.string.share_text));
        execute(ShareSDK.getPlatform(SinaWeibo.NAME), sp);
    }

    /**
     * qq分享
     *
     * @param fileName
     */
    public void QQ(String fileName) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setImagePath(fileName);
        execute(ShareSDK.getPlatform(QQ.NAME), sp);
    }

    /**
     * qq空间
     *
     * @param fileName
     */
    public void qZone(String fileName) {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(context.getString(R.string.share));
        sp.setTitleUrl("http://www.tookok.cn"); // 标题的超链接
        sp.setText(context.getString(R.string.share_text));
        sp.setImagePath(fileName);
        execute(ShareSDK.getPlatform(QZone.NAME), sp);
    }

    /**
     * 执行分享
     * @param platform
     * @param sp
     */
    public void execute(Platform platform, Platform.ShareParams sp) {
        platform.setPlatformActionListener(this); // 设置分享事件回调
        platform.share(sp);// 执行图文分享
    }


    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(Wechat.NAME) || platform.getName().equals(WechatMoments.NAME)) {
            return;
        }
        show(R.string.share_completed);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        if (throwable instanceof WechatClientNotExistException) {
            show(R.string.share_wxfailed);
        } else if (throwable instanceof QQClientNotExistException) {
            show(R.string.share_QQZonefailed);
        } else {
            show(R.string.share_failed);
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        show(R.string.share_canceled);
    }

    private void show(final int textid) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, textid, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
