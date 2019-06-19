package com.jq.btc.thread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.model.TokenEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by zhaowenlong on 2018/2/27.
 * 刷新登陆
 */

public class RefreshAppTokenThread extends Thread {

    private String TAG = "RefreshAppTokenThread";
    //    private String url = "https://uws.haier.net/uds/v1/protected/deviceinfos";
    private String url = "https://account-api.haier.net/oauth/token";
    private String deviceId;
    private Context context;
    private GetTokenResultListener getDevicesResultListener;

    public RefreshAppTokenThread(Context context, GetTokenResultListener getDevicesResultListener) {
        this.context = context;
        this.getDevicesResultListener = getDevicesResultListener;
    }

    @Override
    public void run() {
        super.run();
        try {
            request();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void request() throws JSONException {
        try {
            String body = getBody();
            Log.e(TAG, body);
            String result = post(body);
            if (!TextUtils.isEmpty(result)) {
                JSONObject jsonObject1 = new JSONObject(result);
                if (jsonObject1 != null) {
                    Gson gson = new Gson();
                    TokenEntity entity = gson.fromJson(result, TokenEntity.class);
                    sendMessage(1, entity);

                } else {
                    sendMessage(0, "");
                }
            } else {
                sendMessage(0, "");
            }
        } catch (IOException e) {
            e.printStackTrace();

            sendMessage(0, e.toString());

        }
    }

    private String getBody() {
////        String APP_ID = "MB-HEZNBXQGB-0000";
////        String APP_KEY = "96780ede4629e63c586864e60ab3db7a";
//        String APP_ID = "MB-STZBG-0000";
//        String APP_KEY = "93527666f529e743bb881c164719ae35";
//
//        String uhome_client_id = getMac(context);
//        String body = "client_id=%s&client_secret=%s&grant_type=refresh_token&refresh_token=%s&type_uhome=type_uhome_common_token&uhome_client_id=%s&uhome_app_id=%s";
//        String result = String.format(body,
////                "linkcook",
////                "ttgCrhiizB3w0r",
//                "unilife_standard_api_haier",
//                "unilife_standard_api_haier_123456",
//                UserUtils.get().refreshToken(),
//                uhome_client_id, APP_ID);
//        String sign = getSign(APP_ID, APP_KEY, uhome_client_id);
//        result = result + "&uhome_sign=" + sign;
//        return result;

        String APP_ID = "MB-HEZNBXQGB-0000";
        String APP_KEY = "96780ede4629e63c586864e60ab3db7a";


        String uhome_client_id = getMac(context);
        String body = "client_id=%s&client_secret=%s&grant_type=refresh_token&refresh_token=%s&type_uhome=type_uhome_common_token&uhome_client_id=%s&uhome_app_id=%s";
        String result = String.format(body,
                "linkcook",
                "ttgCrhiizB3w0r",
                UserUtils.get().refreshToken(),
                uhome_client_id, APP_ID);
        String sign = getSign(APP_ID, APP_KEY, uhome_client_id);
        result = result + "&uhome_sign=" + sign;
        return result;

    }

    private void sendMessage(int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        mHandler.sendMessage(msg);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (getDevicesResultListener != null) {
                    getDevicesResultListener.onFaild((String) msg.obj);
                }
            } else if (msg.what == 1) {
                if (getDevicesResultListener != null) {
                    getDevicesResultListener.onSuccess((TokenEntity) msg.obj);
                }
            }

        }
    };

    /**
     * 发起接口请求
     *
     * @param bodyParam
     * @return
     * @throws IOException
     */
    private String post(String bodyParam) throws IOException {
        // 定义返回结果
        String result = "";
        // 声明要用的流对象
        BufferedReader reader = null;
        DataOutputStream out = null;
        HttpURLConnection connection = null;

        try {
            URL postUrl = new URL(url);
            connection = (HttpURLConnection) postUrl.openConnection();

            connection.setDoInput(true);// 允许输入
            connection.setDoOutput(true);// 允许输出
            connection.setUseCaches(false); // 不允许使用缓存
            // 私有配置
            connection.setRequestMethod("POST");
            // 设置HttpURLConnection的一些公用配置
            setHttpURLConnCommPropreties(context, connection, bodyParam);

            // 连接
//            connection.connect();
//            // 推送参数
            out = new DataOutputStream(connection.getOutputStream());

            out.writeBytes(bodyParam);
            out.flush();
            out.close();
            // 获取返回流
            int current = 0;//当前进度
            int i = 0;//流的读取量
            if (connection.getResponseCode() == 200) {
                Log.e(TAG, "getResponseCode = " + 200);

                //result = getEncodedData(connection.getInputStream());
                result = dealResponseResult(connection.getInputStream());
                Log.e(TAG, "========" + result);
            } else {
                Log.e(TAG, "请求数据失败 错误码：getResponseCode" + connection.getResponseCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            closeIO(out, reader);

            // 关闭连接
            if (null != connection) {
                connection.disconnect();
            }
        }
        Log.e("cloud_Platform", result);
        // 返回结果
        return result;
    }

    /**
     * 关闭IO流
     */
    private void closeIO(DataOutputStream out, BufferedReader reader) {
        // 关闭DataOutputStream
        try {
            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 关闭BufferedReader
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Function  :   处理服务器的响应结果（将输入流转化成字符串）
     * Param     :   inputStream服务器的响应输入流
     */
    private String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    /**
     * 设置HttpURLConnection的一些公用配置
     */
    private void setHttpURLConnCommPropreties(Context context, HttpURLConnection connection, String bodyParam) {
        //设置公共HeaderParams
        HashMap<String, String> headParams = setHeaderParams(bodyParam);
        // 设置连接主机超时（30s）
        connection.setReadTimeout(30 * 1000);
        if (headParams != null) {
            for (String key : headParams.keySet()) {
                connection.addRequestProperty(key, headParams.get(key));
            }
        }
    }

    /**
     * 请求头携带的参数
     *
     * @param
     * @return
     */
    private HashMap setHeaderParams(String bodyParam) {
        HashMap headerParams = new HashMap();
        headerParams.put("Content-Type", "application/x-www-form-urlencoded");
        Log.e(TAG, "head:" + new Gson().toJson(headerParams));
        return headerParams;
    }

    @SuppressLint({"DefaultLocale"})
    public static String getMac(Context ctx) {
        WifiManager wifi = (WifiManager) ctx.getSystemService("wifi");
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (mac == null) {
            mac = "00:00:00:00:00";
        }

        return mac.toLowerCase();
    }

    //获取报文流水
    private String getUUID() {
        String s = UUID.randomUUID().toString();
        return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
    }

    /**
     * 获取签名sign方法
     *
     * @param appId
     * @param appKey
     * @param
     * @param
     * @return
     */
    private String getSign(String appId, String appKey, String uhome_client_id) {
        appKey = appKey.trim();
        appKey = appKey.replaceAll("\"", "");
        StringBuffer sb = new StringBuffer();
        sb.append(appId).append(appKey).append(uhome_client_id);
        Log.e(TAG, sb.toString());

        return shaEncrypt(sb.toString());
    }

    private String shaEncrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    private String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
