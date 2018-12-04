package com.jq.btlib.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类
 * */
public class ThreadUtil {

	/** 定义线程池 **/
	private static final ThreadPoolExecutor executor;

	static {
		executor = (ThreadPoolExecutor) Executors // 线程池
				.newCachedThreadPool();
	}

	/**
	 * 使用线程池来运行线程,每一个线程都需要等待线程池中的线程有空闲才会执行
	 * */
	public static void executeThread(Runnable runnable) {
		executor.execute(runnable);
	}

}
