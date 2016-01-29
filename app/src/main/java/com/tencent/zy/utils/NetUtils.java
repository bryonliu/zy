package com.tencent.zy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.tencent.zy.app.ZyApplication;

/**
 * 
 * @author trentyang
 *
 */
public class NetUtils {

    private static final String TAG = NetUtils.class.getSimpleName();

    public enum NetworkType {
        NONE, WIFI, MOBILE;
    }

    public static boolean isNetworkUseable() {
        boolean flag = false;
        ConnectivityManager cm = (ConnectivityManager) ZyApplication.self().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkinfo = cm.getActiveNetworkInfo();
            if (networkinfo != null && networkinfo.isAvailable()) {
                flag = true;
            }
        }
        Log.i(TAG, "isNetworkUseable:" + flag);
        return flag;
    }

    public static NetworkType getNetworkType() {
        NetworkType networkType = NetworkType.NONE;
        ConnectivityManager cm = (ConnectivityManager) ZyApplication.self().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                int type = networkInfo.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NetworkType.WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    networkType = NetworkType.MOBILE;
                }
            }
        }
        return networkType;
    }

    public static boolean isWifiNetwork() {
        return getNetworkType() == NetworkType.WIFI;
    }

}
