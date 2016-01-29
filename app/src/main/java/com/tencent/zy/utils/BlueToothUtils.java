package com.tencent.zy.utils;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by bryonliu on 2016/1/27.
 */
public class BlueToothUtils {

    public static void openBlueTooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean state = bluetoothAdapter.isEnabled();
        if (!state) {
            bluetoothAdapter.enable();
        }
    }
}
