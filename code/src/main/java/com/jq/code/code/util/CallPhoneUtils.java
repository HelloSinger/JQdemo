package com.jq.code.code.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

/**
 * Created by Administrator on 13-11-13.
 */
public class CallPhoneUtils {
    public static  void dail(final Context context, final String phone){
        if (context == null || TextUtils.isEmpty(phone)) {
            return ;
        }
        //6.0权限处理
        Acp.getInstance(context).request(new AcpOptions.Builder().setPermissions(
                Manifest.permission.CALL_PHONE).build(), new AcpListener() {
            @Override public void onGranted() {
                Uri u = Uri.parse("tel:" + phone);
                Intent it = new Intent(Intent.ACTION_CALL, u);
                context.startActivity(it);
            }


            @Override public void onDenied(List<String> permissions) {

            }
        });
    }
    public static void sendSMS(Context context, String phone){
        Uri uriMsg= Uri.parse("smsto:"+phone);
        Intent intentMsg=new Intent();
        intentMsg.setAction(Intent.ACTION_SENDTO);
        intentMsg.putExtra("sms_body", "");
        intentMsg.setType("vnd.android-dir/mms-sms"); //短信的MIME类型
        intentMsg.setData(uriMsg);
        context.startActivity(intentMsg);
    }
    public static void sendEmail(Context context, String emailAddress){
        // 必须明确使用mailto前缀来修饰邮件地址,如果使用
        // intent.putExtra(Intent.EXTRA_EMAIL, email)，结果将匹配不到任何应用
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"+emailAddress));
        intent.putExtra(Intent.EXTRA_SUBJECT, ""); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, ""); // 正文
        context.startActivity(Intent.createChooser(intent, "请选择邮箱完成功作"));
    }
}
