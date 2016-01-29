package com.tencent.zy.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tencent.zy.manager.BusManger;

import butterknife.ButterKnife;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusManger.getInstance().register(this);
    }

    public void setContentView(int layout) {
        super.setContentView(layout);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        BusManger.getInstance().unregister(this);
    }
}
