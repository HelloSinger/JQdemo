package com.jq.code.code.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 负责操作本地数据库文件foodandsports.db的类
 * Created by hfei on 2016/3/21.
 */
public class LocalDB {

    public static final String PACKAGE_NAME = "com.jq.btc";
    public static final String DB_PATH = "/data/data/" + PACKAGE_NAME + "/files/";  //在手机里存放数据库的位置
    public static final String DB_NAME = "foodandsports_1.db";


    /**
     * 将外部数据库拷贝到程序数据库
     * 这个外部数据库是预先写好实物、运动类别数据的，这些数据会在添加“早餐”、“中餐”、“运动”等时，作为展示的类别，并在选择之后
     * 选取部分字段添加到自己的数据库
     * @param context
     * @return
     */
    public static boolean copyDB(Context context) {
        try {
            deleteFile("foodandsports.db");
            // 判断程序内存中是否有拷贝后的文件
            if (!(new File(DB_PATH + DB_NAME)).exists()) {
                InputStream is = context.getResources().getAssets().open("foodandsports.db");
                FileOutputStream fos = context.openFileOutput(DB_NAME, Context.MODE_PRIVATE);
                // 一次拷贝的缓冲大小1M
                byte[] buffer = new byte[1024 * 1024];
                int count = 0;
                // 循环拷贝数据库文件
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void deleteFile(String fileName) {
        File file = new File(DB_PATH + fileName);
        if(file.exists()){
            file.getAbsoluteFile().delete();
        }
    }

    /**
     * 获取数据
     *
     * @return
     */
    public SQLiteDatabase getSQLiteDatabase() {
        return SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
    }
}
