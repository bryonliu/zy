package com.tencent.zy.utils;

import java.util.concurrent.atomic.AtomicInteger;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.TextUtils;

public class HandlerUtils {
	
	private static Handler mManinHandler;
	
	private static Object mMainHandlerLock = new Object();

	private static final AtomicInteger currentHandlerIndex=new AtomicInteger(0);



	/**
	 * 取得UI线程Handler
	 * @return
	 */
	public static synchronized Handler getMainHandler(){
		if (mManinHandler == null) {
			mManinHandler = new Handler(Looper.getMainLooper());
		}
		return mManinHandler;
//		return new Handler(Looper.getMainLooper());
	}
	
	/**
	 * 取得一个非主线Handler，每次获得这个handler都会产生一个线程，所以获得到的这个handler必须做复用！！！
	 * @param threadName
	 * @return
	 */
	public static Handler getHandler(String threadName){
		if(TextUtils.isEmpty(threadName)){
			threadName = "default-thread";
		}
		HandlerThread handlerThread = new HandlerThread(threadName);
		handlerThread.start();
		Looper loop = handlerThread.getLooper();
		if( loop != null)
			return new Handler(loop);
		else
			return null;

	}



}
