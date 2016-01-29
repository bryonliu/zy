package com.tencent.zy.ui.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.tencent.android.talk.IMCloudBaseReceiver;
import com.tencent.android.talk.IMMessage;
import com.tencent.zy.entity.RescueMessage;
import com.tencent.zy.manager.BusManger;
import com.tencent.zy.orm.file.AccountConfig;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by bryonliu on 2016/1/28.
 */
public class MessageReceiver extends IMCloudBaseReceiver {

    private static final String TAG = "bryon";

    @Override
    public void onIMMessage(Context context, IMMessage imMessage) {
        Log.d(TAG, "onIMMessage: " + Thread.currentThread().getName());

        Log.d(TAG, "onIMMessage: " + imMessage);

        if (imMessage.toUser.equals(AccountConfig.openId()) && imMessage.msgType == IMMessage.MESSAGE_TYPE_NORMAL) {
            RescueMessage rescueMessage = JSON.parseObject(imMessage.content, RescueMessage.class);

            Observable.just(rescueMessage).subscribeOn(AndroidSchedulers.mainThread()).subscribe(
                    new Action1<RescueMessage>() {
                        @Override
                        public void call(RescueMessage msg) {
                            BusManger.getInstance().post(msg);
                        }
                    });
        }
    }
}
