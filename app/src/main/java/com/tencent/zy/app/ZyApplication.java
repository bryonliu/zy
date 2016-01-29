package com.tencent.zy.app;

import android.app.Application;
import android.util.Log;

import com.tencent.stat.lbs.StatLbsCallback;
import com.tencent.stat.lbs.StatLbsClient;
import com.tencent.stat.lbs.StatLbsRegisterOption;
import com.tencent.stat.lbs.StatUser;
import com.tencent.stat.lbs.StatUserAttr;
import com.tencent.zy.orm.file.AccountConfig;

import java.util.HashMap;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class ZyApplication extends Application {
    private static ZyApplication sApp;

    private static final String TAG = "bryon";

    public static ZyApplication self() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }



}
