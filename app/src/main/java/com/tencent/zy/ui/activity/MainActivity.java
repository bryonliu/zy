package com.tencent.zy.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.tencent.android.talk.IMCloudCallback;
import com.tencent.android.talk.IMCloudManager;
import com.tencent.stat.lbs.StatLbsCallback;
import com.tencent.stat.lbs.StatLbsClearOption;
import com.tencent.stat.lbs.StatLbsClient;
import com.tencent.stat.lbs.StatLbsRegisterOption;
import com.tencent.stat.lbs.StatUser;
import com.tencent.zy.R;
import com.tencent.zy.entity.event.ChangeFragmentEvent;
import com.tencent.zy.entity.event.RescueAskAction;
import com.tencent.zy.orm.file.AccountConfig;
import com.tencent.zy.ui.adapter.MainAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public static final long LBS_ID = Long.valueOf("1500001062");
    public static final String LBS_KEY = "D68TWAIC11DZ";
    private static final String TAG = "bryon";
    @Bind({ R.id.tv_homePage, R.id.tv_rescue_state, R.id.tv_other, R.id.tv_my_info })
    List<TextView> tabViews;

    @Bind(R.id.vp_contains)
    ViewPager mViewPager;

    private int current = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        regist2MTA();
        initIMCompent();
        initView();
    }

    private void regist2MTA() {
        StatLbsClient statLbsClient = StatLbsClient.getInstance(this);
        statLbsClient.setLbsId(LBS_ID);
        statLbsClient.setLbsKey(LBS_KEY);

        StatUser statUser = new StatUser(AccountConfig.openId());

        // TODO 通过 分类过滤无效（MTA）
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put(AccountConfig.KEY_USER_INFO, AccountConfig.getAccount().toJsonString());

        paramsMap.put(AccountConfig.KEY_CATEGORY, AccountConfig.getAccount().catory.name());

        statUser.setExt(paramsMap);

        StatLbsRegisterOption registerOption = new StatLbsRegisterOption();

        registerOption.setCallback(new StatLbsCallback() {
            @Override
            public void onReceive(int resultCode, String msg) {
                if (resultCode == StatLbsClient.OK) {
                    Log.d(TAG, "resigter success : " + resultCode + " " + msg);
                } else {
                    Log.d(TAG, "resigter fail :  " + resultCode + " " + msg);
                }
            }
        });
        statLbsClient.register(statUser, registerOption);

    }

    private void initIMCompent() {
        IMCloudManager.start(getApplicationContext());
        IMCloudManager.login(getApplicationContext(), AccountConfig.openId(), AccountConfig.openId(),
                new IMCloudCallback() {
                    @Override
                    public void onSuccess(Object o, int i) {
                        Log.d(TAG, "onSuccess:login.");
                    }

                    @Override
                    public void onFail(Object o, int i, String s) {

                        Log.e(TAG, "onFail: login" + s);
                    }
                });
    }

    private void signoutIMCompent() {
        IMCloudManager.unLogin(getApplicationContext(), AccountConfig.openId(), AccountConfig.openId(),
                new IMCloudCallback() {
                    @Override
                    public void onSuccess(Object o, int i) {
                        Log.d(TAG, "onSuccess: logout im.");
                    }

                    @Override
                    public void onFail(Object o, int i, String s) {
                        Log.e(TAG, "onFail: logout." + i + "  " + s);
                    }
                });
    }

    private void initView() {
        modifyTab(current);
        mViewPager.setAdapter(new MainAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(3);
    }

    @OnClick({ R.id.tv_homePage, R.id.tv_rescue_state, R.id.tv_other, R.id.tv_my_info })
    public void onClick(View view) {
        int index = -1;
        for (int i = 0; i < tabViews.size(); i++) {
            if (view.getId() == tabViews.get(i).getId()) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return;
        modifyTab(index);
        mViewPager.setCurrentItem(current);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                modifyTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void modifyTab(int index) {
        tabViews.get(current).setTextColor(getResources().getColor(R.color.black));
        tabViews.get(index).setTextColor(getResources().getColor(R.color.darkRed));
        current = index;
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        signoutIMCompent();
        clearLbsInfo();
    }

    private void clearLbsInfo() {

        // 清除请求的配置项
        StatLbsClearOption option = new StatLbsClearOption();
        // 【可选】设置清除附近的人的回调函数
        option.setCallback(new StatLbsCallback() {

            @Override
            public void onReceive(int opcode, String msg) {

                if (opcode == StatLbsClient.OK) {
                    Log.d(TAG, "StatLbsClear success, opcode:" + opcode + " ,msg:" + msg);
                } else {
                    Log.d(TAG, "StatLbsClear fail , opcode:" + opcode + " ,msg:" + msg);
                }
            }
        });
        StatLbsClient statLbsClient = StatLbsClient.getInstance(this);
        statLbsClient.clearLocation(option);
    }

    @Subscribe
    public void changeFragment(ChangeFragmentEvent event) {
        int index = event.index;

        if (index >= tabViews.size() || index < 0) {
            return;
        }
        modifyTab(index);
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
