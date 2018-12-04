package com.jq.code.code.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;

public class CacheUtil {

    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15; // 软引用缓存容量
    private static LruCache<String, Bitmap> mLruCache; // 硬引用缓存
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache; //
    // 软引用缓存
    private static final String WHOLESALE_CONV = ".cach"; // 缓存文件后缀

    private static final int MB = 1024 * 1024;// 文件缓存单位
    private static final int CACHE_SIZE = 10; // 文件缓存容量
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;// sd卡用于缓存的空间容量
    private static CacheUtil instance;

	/* <========================内存缓存===========================> */

    public CacheUtil(Context context) {
        // 初始化内存缓存
        initMemoryCache(context);
        // 初始化文件缓存
        initFileCache();
    }

    public static CacheUtil getInstance(Context context) {
        if (instance == null) {
            return new CacheUtil(context);
        } else {
            return instance;
        }
    }

    private void initMemoryCache(Context context) {
        int MAXMEMONRY = (int) (Runtime.getRuntime().maxMemory() / MB);
        int cacheSize = MAXMEMONRY / 8; // 硬引用缓存容量，为系统可用内存的1/8
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(
                SOFT_CACHE_SIZE, 0.75f, true) {
            private static final long serialVersionUID = 6040103833179403725L;

            @Override
            protected boolean removeEldestEntry(
                    Entry<String, SoftReference<Bitmap>> eldest) {
                // LogUtil.e("mSoftCache", this.size()+"");
                return size() > SOFT_CACHE_SIZE;
            }
        };

        if (mLruCache != null) {
        } else {
            mLruCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    if (value != null) {
                        return value.getByteCount() / MB;
                    } else
                        return 0;
                }

                @Override
                protected void entryRemoved(boolean evicted, String key,
                                            Bitmap oldValue, Bitmap newValue) {
                    // LogUtil.e("mLruCache", this.size()+"");
                    if (oldValue != null) {
                        mSoftCache
                                .put(key, new SoftReference<Bitmap>(oldValue));
                    }
                }
            };
        }
    }

    public void clearCache() {
        if (mLruCache != null) {
            if (mLruCache.size() > 0) {
                LogUtil.i("CacheUtils",
                        "mMemoryCache.size() " + mLruCache.size());
                mLruCache.evictAll();
                LogUtil.i("CacheUtils",
                        "mMemoryCache.size()" + mLruCache.size());
            }
            mLruCache = null;
        }
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public synchronized void removeImageCache(String key) {
        if (key != null) {
            if (mLruCache != null) {
                Bitmap bm = mLruCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }

    /**
     * 从缓存中获取图片
     */
    public Bitmap getBitmapFromMemCache(String url) {
        if (url == null) {
            return null;
        }
        Bitmap bitmap = null;
        // 先从硬引用缓存中获取
        synchronized (mLruCache) {
            bitmap = mLruCache.get(url);
            if (bitmap != null) {
                // 如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
                mLruCache.remove(url);
                mLruCache.put(url, bitmap);
                LogUtil.e("getBitmapFromMemCache", "mLruCache");
                LogUtil.e("getBitmapFromMemCache", "url:" + url);
                return bitmap;
            }
        }
        // 如果硬引用缓存中找不到，到软引用缓存中找
        synchronized (mSoftCache) {
            SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
            if (bitmapReference != null) {
                bitmap = bitmapReference.get();
                if (bitmap != null) {
                    // 将图片移回硬缓存
                    mLruCache.put(url, bitmap);
                    mSoftCache.remove(url);
                    LogUtil.e("getBitmapFromMemCache", "mSoftCache");
                    return bitmap;
                } else {
                    mSoftCache.remove(url);
                }
            }
        }
        return null;
    }

    /**
     * 添加图片到缓存
     */
    public void addBitmapToMemoryCache(String url, Bitmap bitmap) {
        if (mLruCache.get(url) == null) {
            if (url != null && bitmap != null) {
                synchronized (mLruCache) {
                    mLruCache.put(url, bitmap);
                }
            }
        } else {
            if (mLruCache.get(url).getByteCount() != bitmap.getByteCount()) {
                synchronized (mLruCache) {
                    mLruCache.put(url, bitmap);
                }
            }
            LogUtil.e("CacheUtils", "the res is aready exits");
        }
    }

    /**
     * 清除内存缓存
     */
    // public void clearCache() {
    // mSoftCache.clear();
    // }

	/* <========================文件缓存===========================> */

    /**
     * 清理文件缓存
     */
    private void initFileCache() {
        removeCache(getDirectory());
    }

    /**
     * 将图片存入文件缓存
     **/
    public void saveBitmapToFileCache(String url, Bitmap bm) {
        if (bm == null) {
            return;
        }
        // 判断sdcard上的空间
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // SD空间不足
            return;
        }
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();
        File file = new File(dir + "/" + filename);
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            OutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (FileNotFoundException e) {
            LogUtil.w("ImageFileCache", "FileNotFoundException");
        } catch (IOException e) {
            LogUtil.w("ImageFileCache", "IOException");
        }
    }

    /**
     * 从缓存中获取图片
     **/
    public Bitmap getImageFromFile(final String url) {

        final String path = FileUtil.PATH_ICON_PATH + convertUrlToFileName(url);
        LogUtil.e("path", path);
        File file = new File(path);
        Bitmap bmp;
        if (file.exists()) {
//            BitmapFactory.Options opts = new BitmapFactory.Options();
//            opts.inJustDecodeBounds = true;
//            bmp = BitmapFactory.decodeFile(path,opts);
//            int imageWidth = opts.outWidth;
//            int imageHeight = opts.outHeight;
//            int widthscale = imageWidth / 180;
//            int heightscale = imageHeight / 180;
//            int scale = widthscale > heightscale ? widthscale : heightscale;
//            opts.inSampleSize = scale;
//            opts.inJustDecodeBounds = false;
//            bmp = BitmapFactory.decodeFile(path, opts);
            bmp = BitmapFactory.decodeFile(path);
            if (bmp == null) {
                file.delete();
            } else {
                updateFileTime(path);
                return bmp;
            }
        }
        return null;
    }

    /**
     * 计算存储目录下的文件大小，
     * 当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定
     * 那么删除40%最近没有被使用的文件
     */
    private boolean removeCache(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null) {
            return true;
        }
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return false;
        }

        int dirSize = 0;
        for (int i = 0; i < files.length; i++) {
            if(files[i] != null) continue;
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirSize += files[i].length();
            }
        }

        if (dirSize > CACHE_SIZE * MB
                || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if(files[i] != null) continue;
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }

        return freeSpaceOnSd() > CACHE_SIZE;

    }

    public void deleteFile(String url) {
        if (url == null)
            return;
        String filename = convertUrlToFileName(url);
        String dir = getDirectory();
        File file = new File(dir + "/" + filename);
        if (file.exists()) {
            file.delete();
        }
    }

    public void clearFile(String dirPath) {
        File file = new File(dirPath);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {

            if (file.isDirectory()) {
                clearFile(files[i].getPath());
            } else {
                files[i].delete();
            }
        }
    }

    /**
     * 修改文件的最后修改时间
     **/
    public void updateFileTime(String path) {
        File file = new File(path);
        long newModifiedTime = System.currentTimeMillis();
        file.setLastModified(newModifiedTime);
    }

    /**
     * 计算sdcard上的剩余空间
     **/
    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        @SuppressWarnings("deprecation")
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    /**
     * 将url转成文件名
     **/
    private String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1];
    }

    /**
     * 获得缓存目录
     **/
    private String getDirectory() {
        String dir = FileUtil.createSDDir(FileUtil.CHIPSEA_ICON_DIR);
        return dir;
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
