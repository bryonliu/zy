package com.tencent.zy.utils;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by bryonliu on 2016/1/29.
 */
public class HardwareHelper {
    public static void vibrate(Context context, long mills) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(mills);
    }

    public static void vibrate(Context context) {
        vibrate(context, 2000);
    }

}
