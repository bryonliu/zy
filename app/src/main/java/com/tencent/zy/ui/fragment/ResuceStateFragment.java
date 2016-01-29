package com.tencent.zy.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.Bind;

import com.squareup.otto.Subscribe;
import com.tencent.android.talk.IMCloudCallback;
import com.tencent.android.talk.IMCloudManager;
import com.tencent.stat.lbs.StatLbsCallback;
import com.tencent.stat.lbs.StatLbsClient;
import com.tencent.stat.lbs.StatLbsRequestOption;
import com.tencent.stat.lbs.StatUser;
import com.tencent.zy.R;
import com.tencent.zy.entity.event.ChangeFragmentEvent;
import com.tencent.zy.manager.BusManger;
import com.tencent.zy.utils.BlueToothUtils;
import com.tencent.zy.utils.HardwareHelper;
import com.tencent.zy.entity.Person;
import com.tencent.zy.entity.RescueMessage;
import com.tencent.zy.entity.event.RescueAskAction;
import com.tencent.zy.orm.file.AccountConfig;
import com.tencent.zy.ui.adapter.OtherRescueAdapter;
import com.tencent.zy.ui.adapter.RescueAdapter;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class ResuceStateFragment extends BaseFragment {
    private static final String TAG = "bryon";
    private static final int MAX_PERSON = 50;

    private RescueAskAction rescueAskEvent;

    @Bind(R.id.lv_my_rescue_state)
    ListView myRescueListView;
    @Bind(R.id.lv_other_resuce_info)
    ListView otherRescueListView;

    private RescueAdapter myRescueAdapter;
    private OtherRescueAdapter othersRescueAdapter;

    private List<RescueMessage> myRescueMessages = new ArrayList<>();

    private List<RescueMessage> otherRescueMessages = new ArrayList<>();

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resuce_state, null);
        return view;
    }

    @Override
    protected void initView() {
        myRescueListView.setVisibility(View.GONE);
        otherRescueListView.setVisibility(View.VISIBLE);
        myRescueAdapter = new RescueAdapter(getContext());
        myRescueListView.setAdapter(myRescueAdapter);
        othersRescueAdapter = new OtherRescueAdapter(getContext());
        otherRescueListView.setAdapter(othersRescueAdapter);

    }

    private List<Person> sendRescurMsg() {

        StatLbsClient statLbsClient = StatLbsClient.getInstance(getContext());
        if (!statLbsClient.isGpsOpened()) {
            BlueToothUtils.openBlueTooth();
        }
        StatLbsRequestOption requestOption = new StatLbsRequestOption();
        requestOption.setGpsLevel(StatLbsRequestOption.GPS_LEVEL_DEFAULT);
        requestOption.setLimit(MAX_PERSON);

        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put(AccountConfig.KEY_CATEGORY, Person.PersonCatory.Answer.name());

        requestOption.setExt(paramsMap);

        requestOption.setCallback(new StatLbsCallback() {
            @Override
            public void onReceive(int i, String s) {
                Log.d(TAG, "code : " + i + " | msg : " + s);

                if (i == StatLbsClient.OK) {
                    ArrayList<StatUser> statUsers = StatLbsClient.decode(s);
                    List<Person> persons = new ArrayList<>();
                    for (StatUser user : statUsers) {
                        Map<String, String> paramsMap = user.getExt();

                        if (null != paramsMap && paramsMap.size() != 0) {
                            String jsonString = paramsMap.get(AccountConfig.KEY_USER_INFO);
                            Person person = Person.parserFromJson(jsonString);
                            persons.add(person);
                            Log.d(TAG, "get lbs person  " + person.nickName + " " + person.openId);
                        }
                    }
                    sendRescueMsgToPersons(persons);
                } else {
                    Log.e(TAG, "onReceive: can't get lbs persons for help" + " code : " + i + " msg : " + s);
                }
            }
        });
        statLbsClient.requestLocation(requestOption);

        return null;
    }

    private void sendRescueMsgToPersons(List<Person> persons) {

        String text = rescueAskEvent.text;
        for (final Person person : persons) {

            if (person.openId == AccountConfig.openId() || person.catory == Person.PersonCatory.Asker)
                continue;
            RescueMessage msg = new RescueMessage();
            msg.text = text;
            msg.fromPerson = AccountConfig.getAccount();
            msg.toPerson = person;
            msg.rescurState = RescueMessage.RescurState.WAIT;
            myRescueMessages.add(msg);

            IMCloudManager.sendMsgToUserId(getContext(), person.openId, msg.toJsonString(), new IMCloudCallback() {

                @Override
                public void onSuccess(Object o, int i) {
                    Log.d(TAG, "success to send rescue message to " + person.openId);
                }

                @Override
                public void onFail(Object o, int i, String s) {
                    Log.e(TAG, "fail to send rescue message to " + person.openId);
                }
            });
        }
        myRescueAdapter.update(myRescueMessages);
    }

    @Subscribe
    public void sendMessageToPersons(RescueAskAction event) {

        if (myRescueMessages.size() > 0) {
            String tip = getResources().getString(R.string.tip_please_cancle_current_rescue);
            Toast.makeText(getContext(), tip, Toast.LENGTH_SHORT).show();
            return;
        }
        myRescueListView.setVisibility(View.VISIBLE);
        otherRescueListView.setVisibility(View.GONE);

        rescueAskEvent = event;
        sendRescurMsg();
    }

    @Subscribe
    public void reciveIMMessage(RescueMessage rescueMessage) {

        /**
         * 当我在发出求救的时候，不接受其他人的求救信息
         */
        if (myRescueMessages.size() > 0 && rescueMessage.rescurState == RescueMessage.RescurState.WAIT) {
            myRescueListView.setVisibility(View.VISIBLE);
            otherRescueListView.setVisibility(View.GONE);
            return;
        }
        Log.d(TAG, "success  get message from " + rescueMessage.fromPerson.openId);

        BusManger.getInstance().post(new ChangeFragmentEvent(1));
        HardwareHelper.vibrate(getContext());
        /**
         * 我发出的救援得到了别人的反馈
         */
        if (myRescueMessages.size() > 0) {

            for (RescueMessage msg : myRescueMessages) {
                if (msg.toPerson.openId.equals(rescueMessage.fromPerson.openId)) {
                    msg.rescurState = rescueMessage.rescurState;
                }
            }
            myRescueAdapter.update(myRescueMessages);
            return;

        } else {
            /**
             * 我是空闲的情况下，接受到了别人的求救信息
             */

            if (hasReceivedRescueMsgOfSamePerson(rescueMessage)) {
                return;
            }
            myRescueListView.setVisibility(View.GONE);
            otherRescueListView.setVisibility(View.VISIBLE);

            otherRescueMessages.add(rescueMessage);
            othersRescueAdapter.update(otherRescueMessages);
        }
    }

    private boolean hasReceivedRescueMsgOfSamePerson(RescueMessage rescueMessage) {

        for (RescueMessage msg : otherRescueMessages) {
            if (msg.fromPerson.openId == rescueMessage.fromPerson.openId)
                return true;
        }
        return false;
    }

}
