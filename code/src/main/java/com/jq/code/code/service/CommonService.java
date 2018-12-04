package com.jq.code.code.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 基础服务类
 * */
public class CommonService extends Service {

	private final IBinder mBinder = new LocalBinder();
	protected CommonService instance;

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = CommonService.this;
	}

	/**
	 * 继承一个Binder,就是用来返回本服务的实体类！
	 * */
	public class LocalBinder extends Binder {
		public CommonService getService() {
			return instance;
		}
	}

}
