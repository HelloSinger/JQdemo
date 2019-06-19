package com.jq.btc.thread;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
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
 * 免登陆，获取token
 */

public class GetAppTokenThread extends Thread {

    private String TAG = "GetAppTokenThread";
    //    private String url = "https://uws.haier.net/uds/v1/protected/deviceinfos";
    private String url = "https://account-api.haier.net/haier/v2/token/exchange-plus";
    private String deviceId;
    private Context context;
    private String accessToken;
    private GetTokenResultListener getDevicesResultListener;

    public GetAppTokenThread(Context context, String accessToken, GetTokenResultListener getDevicesResultListener) {
        this.context = context;
        this.accessToken = accessToken;
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
            String body = getContentString();
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

    private String getContentString() throws JSONException {
//        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("client_id", "bxpdpobi");//
////        jsonObject.put("client_secret", "KgWaUsOtQHr7Vn");
//        jsonObject.put("client_id", "unilife_standard_api_haier");//
//        jsonObject.put("client_secret", "unilife_standard_api_haier_123456");
//        String APP_ID = "MB-STZBG-0000";
//        String APP_KEY = "93527666f529e743bb881c164719ae35";
//
//        String uhome_client_id = getMac(context);
//        jsonObject.put("uhome_client_id", uhome_client_id);
//        String timestamp = System.currentTimeMillis() + "";
//        jsonObject.put("uhome_app_id", APP_ID);
//        jsonObject.put("uhome_app_version", "1.0");//
////        jsonObject.put("captcha_token", "ff03e1e0-77df-4e5a-9afd-4e986e76401b");//
//
//        String sign = getSign(APP_ID, APP_KEY, uhome_client_id, jsonObject.toString());
//        jsonObject.put("uhome_sign", sign);
//        return jsonObject.toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("client_id", "bxpdhongbei");//
        jsonObject.put("client_secret", "s7t!4doIOcgLwr");
//        jsonObject.put("client_id", "unilife_standard_api_haier");//
//        jsonObject.put("client_secret", "unilife_standard_api_haier_123456");
        String APP_ID = "MB-BXDKXKZ-0000";
        String APP_KEY = "15c0c5544355f7d26b2a89f0f5b8484a";

        String uhome_client_id = getMac(context);
        jsonObject.put("uhome_client_id", uhome_client_id);
        String timestamp = System.currentTimeMillis() + "";
        jsonObject.put("uhome_app_id", APP_ID);
        jsonObject.put("uhome_app_version", "1.0");//
//        jsonObject.put("captcha_token", "ff03e1e0-77df-4e5a-9afd-4e986e76401b");//

        String sign = getSign(APP_ID, APP_KEY, uhome_client_id, jsonObject.toString());
        jsonObject.put("uhome_sign", sign);
        return jsonObject.toString();

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
        HashMap<String, String> headParams = setHeaderParams(context, bodyParam);
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
     * @param context
     * @return
     */
    private HashMap setHeaderParams(Context context, String bodyParam) {
        HashMap headerParams = new HashMap();
//        String accessToken = UserUtils.get().uCenterToken();
        String str2 = "";
        try {
            String token = accessToken.substring(accessToken.indexOf(".") + 1, accessToken.lastIndexOf("."));
            str2 = new String(Base64.decode(token.getBytes(), Base64.DEFAULT));
            JSONObject jsonObject = new JSONObject(str2);
            String tokenjson = jsonObject.getString("iss");
            tokenjson = tokenjson.replace("\'", "\"");
            jsonObject = new JSONObject(tokenjson);
            str2 = jsonObject.getString("ucenter_token");
        } catch (Exception e) {
            e.printStackTrace();
            str2 = accessToken;
        }
        headerParams.put("Authorization", "Bearer  " + str2);
        headerParams.put("Content-Type", "application/json");//待核实  "application/json;charset=UTF-8"  必填
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
     * @param timestamp
     * @param body
     * @return
     */
    private String getSign(String appId, String appKey, String timestamp, String body) {
        appKey = appKey.trim();
        appKey = appKey.replaceAll("\"", "");

        if (body != null) {
            body = body.trim();
        }
        if (!body.equals("")) {
            body = body.replaceAll("", "");
            body = body.replaceAll("\t", "");
            body = body.replaceAll("\r", "");
            body = body.replaceAll("\n", "");
        }
        StringBuffer sb = new StringBuffer();
        sb.append(appId).append(appKey).append(timestamp);
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
