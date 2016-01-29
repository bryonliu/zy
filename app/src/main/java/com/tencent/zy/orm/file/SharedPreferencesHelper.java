package com.tencent.zy.orm.file;

import android.content.Context;

import com.tencent.zy.app.ZyApplication;

/**
 * @version 1.0
 * @author benpeng
 * @date 2015-10-2
 *  
 */
public class SharedPreferencesHelper {
    public static final String KEY_CONTACTS_LAST_UPDATETIME = "key_contacts_last_update_time";
    public static final String FILE_CONTACTS = "file_contacts";
    /**
     * 使用单例
     */
    private static final SharedPreferencesHelper config = new SharedPreferencesHelper();

    private final Context appContext;

    private SharedPreferencesHelper() {
        appContext = ZyApplication.self().getApplicationContext();
    }

    public static SharedPreferencesHelper get() {
        return config;
    }

    public void put(String fname, String key, String value) {
        appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public void put(String fname, String key, int value) {
        appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public void put(String fname, String key, boolean value) {
        appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public void put(String fname, String key, float value) {
        appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).edit().putFloat(key, value).commit();
    }

    public void put(String fname, String key, long value) {
        appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    public String getString(String fname, String key, String defaultValue) {
        return appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    public int getInt(String fname, String key, int defaultValue) {
        return appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).getInt(key, defaultValue);
    }

    public boolean getBoolean(String fname, String key, boolean defaultValue) {
        return appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public float getFloat(String fname, String key, float defaultValue) {
        return appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).getFloat(key, defaultValue);
    }

    public long getLong(String fname, String key, long defaultValue) {
        return appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).getLong(key, defaultValue);
    }

    public void remove(String fname, String key) {
        if (key != null) {
            appContext.getSharedPreferences(fname, Context.MODE_PRIVATE).edit().remove(key).commit();
        }
    }
}
