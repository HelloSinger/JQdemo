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
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.utils.WechatClientNotExistException;

/**
 * Created by Administrator on 2017/2/16.
 */

public class LoginImp {
    public enum Type {
        WECHAT, QQ, SINAWEIBO
    }

    private Context context;
    private LoginListener loginListener;
    private Platform platform;
    private Type type;

    private LoginImp(Context context) {
        this.context = context;
    }

    public static LoginImp create(Context context) {
        return new LoginImp(context);
    }

    public LoginImp type(Type type) {
        this.type = type;
        ShareSDK.initSDK(context);
        if (type == Type.WECHAT) {
            platform = ShareSDK.getPlatform(Wechat.NAME);
        } else if (type == Type.QQ) {
            platform = ShareSDK.getPlatform(QQ.NAME);
        } else if (type == Type.SINAWEIBO) {
            platform = ShareSDK.getPlatform(SinaWeibo.NAME);
        }
        return this;
    }

    public LoginImp listener(LoginListener loginListener) {
        this.loginListener = loginListener;
        return this;
    }

    private String errmsg;
    public LoginImp errMsg(String msg) {
        errmsg = msg;
        return this;
    }

    public void login() {
        if (platform == null) {
            if(errmsg == null) return;
            if (type == Type.WECHAT) {
                show(String.format(errmsg, context.getString(R.string.wechat)));
                return;
            } else if (type == Type.QQ) {
                show(String.format(errmsg, context.getString(R.string.settingAcountQQ)));
                return;
            } else if (type == Type.SINAWEIBO) {
                show(String.format(errmsg, context.getString(R.string.sinaweibo)));
                return;
            }
        }
        authorizeLogin(platform);
    }


    /**
     * 第三方登录授权回调
     *
     * @param platform
     */
    private void authorizeLogin(Platform platform) {
        if (platform == null) return;
        if (platform.isValid()) {
            platform.removeAccount();
        }
        platform.SSOSetting(false);
        platform.authorize();
        platform.setPlatformActionListener(actionListener);
    }

    private PlatformActionListener actionListener = new PlatformActionListener() {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            String openId = platform.getDb().getUserId(); // 获取用户在此平台的ID
            String token = platform.getDb().getToken();
            if (platform.getName().equals(QQ.NAME)) {
                loginListener.onComplete(Type.QQ, openId, token);
            } else if (platform.getName().equals(SinaWeibo.NAME)) {
                loginListener.onComplete(Type.SINAWEIBO, openId, token);
            } else if (platform.getName().equals(Wechat.NAME)) {
                loginListener.onComplete(Type.WECHAT, openId, token);
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            if (loginListener == null) return;
            if (throwable instanceof WechatClientNotExistException) {
                loginListener.onError(context.getString(R.string.share_wxfailed), 405);
            } else {
                loginListener.onError(context.getString(R.string.err404), 404);
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            if (loginListener == null) return;
            loginListener.onError(context.getString(R.string.cancelOprater), 404);
        }
    };

    public interface LoginListener {
        void onComplete(Type type, String openId, String token);

        void onError(String err, int code);
    }

    private void show(final String text) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
