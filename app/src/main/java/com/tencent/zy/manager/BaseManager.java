package com.tencent.zy.manager;

import com.tencent.zy.utils.HandlerUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @author trentyang
 */
public abstract class BaseManager {

    private static final String TAG = "BaseManager";
    private static List<BaseManager> managerList = new ArrayList<BaseManager>();

    public void runOnUI(Runnable runnable) {
        HandlerUtils.getMainHandler().post(runnable);
    }

    public BaseManager() {
        managerList.add(this);
    }

    /**
     * 需要在切换账号，或者其他情况下从新被初始化的Manager,实现这个函数
     */
    public void destory() {
    }

    /**
     * 调用该方法来destory所有重载了destory方法的Manager
     */
    protected void clear() {
        for (BaseManager baseManger : managerList) {
            baseManger.destory();
        }
        managerList.clear();
    }

}
