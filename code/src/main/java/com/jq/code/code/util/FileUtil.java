package com.jq.code.code.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

/**
 * 文件工具类
 */
public class FileUtil {
	private static final String TAG = "FileUtils";
	/**
	 * 开始消息提示常量
	 * */
	public static final int startDownloadMeg = 1;

	/**
	 * 更新消息提示常量
	 * */
	public static final int updateDownloadMeg = 2;

	/**
	 * 完成消息提示常量
	 * */
	public static final int endDownloadMeg = 3;

	/**
	 * chipsea目录！
	 */
	public static final String CHIPSEA_ROOT_DIR = "/jq/download/";
	public static final String CHIPSEA_ICON_DIR = "/jq/icon/";
	public static final String CHIPSEA_LOG_DIR = "/jq/log/";
	public static final String CHIPSEA_SHARE_DIR = "/jq/share/";

	public static final String CHIPSEA_ROOT = Environment
			.getExternalStorageDirectory() +"/jq/";
	/**
	 * 头像存储路径
	 */
	public static final String PATH_ICON_PATH = Environment
			.getExternalStorageDirectory() + CHIPSEA_ICON_DIR;

	/**
	 * 分享图片路径
	 */
	public static final String PATH_PHOTO_SHARE = Environment
			.getExternalStorageDirectory() + CHIPSEA_SHARE_DIR;
	/**
	 * 图片存储路径
	 */
	public static final String PATH_PICTURE = Environment
			.getExternalStorageDirectory() + CHIPSEA_ICON_DIR;
	/**
	 * 异常日志存储路径
	 */
	public static final String PATH_LOG = Environment
			.getExternalStorageDirectory() + CHIPSEA_LOG_DIR;

	/**
	 * 截屏
	 */
	public static final String SHOT_SCREEN_IMAGE_NAME = "shotimage.png"; // 截屏名称
	public static final String SCREENSHOT_IMAG_PATH = PATH_PICTURE
			+ SHOT_SCREEN_IMAGE_NAME; // 截屏路径

	/**
	 * 拍照与相册
	 */
	public static final String PICTURE_TMP_IMAGE_NAME = "tmp_image.jpg";
	public static final String PICTURE_TMP_IMAGE_PATH = PATH_PICTURE
			+ PICTURE_TMP_IMAGE_NAME;

	/**
	 * 检验SDcard状态
	 *
	 * @return boolean
	 */
	public static boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
	}

	/**
	 * 保存文件文件到目录
	 *
	 * @param context
	 * @return 文件保存的目录
	 */
	public static String setMkdir(Context context) {
		String filePath;
		if (checkSDCard()) {
			filePath = Environment.getExternalStorageDirectory()
					+ File.separator + "myfile";
		} else {
			filePath = context.getCacheDir().getAbsolutePath() + File.separator
					+ "myfile";
		}
		File file = new File(filePath);
		if (!file.exists()) {
			boolean b = file.mkdirs();
			LogUtil.e("file", "文件不存在  创建文件    " + b);
		} else {
			LogUtil.e("file", "文件存在");
		}
		return filePath;
	}

	/**
	 * 得到文件的名称
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getFileName(String url) {
		String name = null;
		try {
			name = url.substring(url.lastIndexOf("/") + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 得到文件的名称
	 *
	 * @return
	 * @throws IOException
	 */
	public static String getFileType(String url) {
		String name = null;
		try {
			name = url.substring(url.lastIndexOf(".") + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 创建目录
	 *
	 * @param dirName
	 * @return
	 */
	public static String createSDDir(String dirName) {
		if (checkSDCard()) {
			File dir = new File(Environment.getExternalStorageDirectory()
					+ dirName);
			dir.mkdirs(); // mkdirs 连chipsea/download两个目录都创建
							// mkdir 只能创建一个~~
			return dir.getAbsolutePath();
		}
		return "";
	}

	/**
	 * 判断文件或文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExist(String fileName) {
		File file = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + fileName);
		return file.exists();
	}

	/**
	 * 关闭输入流>/br>
	 *
	 * @param input
	 */
	public static void closeInputStream(InputStream input) {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				LogUtil.e("InputStream",
						"InputStream IOException " + e.getMessage());
			}
		}
	}

	/**
	 * 关闭输出流</br>
	 *
	 * @param output
	 */
	public static void closeOutputStream(OutputStream output) {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				LogUtil.e("OutputStream",
						"OutputStream IOException " + e.getMessage());
			}
		}
	}

	/**
	 * 删除目录下面的所有文件
	 *
	 * @param file
	 */
	public static void deleteDirAllFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				// file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				deleteDirAllFile(childFiles[i]);
			}
			// file.delete();
		}
	}

	public static File getAppLogFile(){
		return new File(FileUtil.PATH_LOG, "app.data");
	}


	private static void writeAppLog(String line, boolean append){
		File log = getAppLogFile();
		if (!log.exists()){
			log.getParentFile().mkdirs();
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(log, append)));
			writer.write(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(writer != null ) {
				writer.close();
			}
		}
	}

	public static void appendAppLog(String line){
		writeAppLog(line, true);
	}

	public static void clearAppLogFile(String headline){
		writeAppLog(headline, false);
	}


	public static File compress(File file) throws IOException {
		BufferedOutputStream os = null;
		BufferedInputStream in = null;
		try{
			File temp = File.createTempFile("temp", ".gz");
			os = new BufferedOutputStream(new GZIPOutputStream(new FileOutputStream(temp)));
			in = new BufferedInputStream(new FileInputStream(file));
			int i ;
			while ((i = in.read()) != -1) {
				os.write(i);
			}
			os.flush();
			return temp;
		}catch (IOException e){
			throw e;
		} finally {
			try{
				if(in!=null){
					in.close();
				}
				if(os!=null){
					os.close();
				}
			}catch (IOException e){}
		}
	}
	public static boolean saveImageToGallery(Context context, Bitmap bmp) {
		String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "haier";
		File appDir = new File(storePath);
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			//通过io流的方式来压缩保存图片
			boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
			fos.flush();
			fos.close();
			Uri uri = Uri.fromFile(file);
			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
			if (isSuccess) {
				return true;
			} else {
				return false;
			}
			}catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getFolderName(String name) {
		File mediaStorageDir =
				new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						name);

		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return "";
			}
		}

		return mediaStorageDir.getAbsolutePath();
	}

	private static boolean isSDAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static File getNewFile(Context context, String folderName) {

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);

		String timeStamp = simpleDateFormat.format(new Date());

		String path;
		if (isSDAvailable()) {
			path = getFolderName(folderName) + File.separator + timeStamp + ".jpg";
		} else {
			path = context.getFilesDir().getPath() + File.separator + timeStamp + ".jpg";
		}

		if (TextUtils.isEmpty(path)) {
			return null;
		}

		return new File(path);
	}


	public static Bitmap createBitmapByView(View puzzleView) {
		Bitmap bitmap =Bitmap.createBitmap(puzzleView.getWidth(), puzzleView.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		puzzleView.draw(canvas);

		return bitmap;
	}
	public static Bitmap savePuzzle(View puzzleView, File file, int quality) {
		Bitmap bitmap = null;
		FileOutputStream outputStream = null;

		try {
			bitmap = createBitmapByView(puzzleView);
			outputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

			if (!file.exists()) {
				Log.e(TAG, "notifySystemGallery: the file do not exist.");
				return null;
			}

			try {
				MediaStore.Images.Media.insertImage(puzzleView.getContext().getContentResolver(),
						file.getAbsolutePath(), file.getName(), null);
				return bitmap ;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			puzzleView.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
			}

			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap ;
	}
}

