package com.tencent.zy.manager;

import com.squareup.otto.Bus;

/**
 * Created by bryonliu on 2016/1/28.
 */
public class BusManger {
    private static Bus bus = new Bus();

    private BusManger() {
    }

    public static synchronized Bus getInstance() {

        return bus;
    }
}
