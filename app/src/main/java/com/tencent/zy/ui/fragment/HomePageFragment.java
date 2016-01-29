package com.tencent.zy.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.OnClick;

import com.tencent.zy.R;
import com.tencent.zy.entity.event.ChangeFragmentEvent;
import com.tencent.zy.entity.event.RescueAskAction;
import com.tencent.zy.manager.BusManger;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class HomePageFragment extends BaseFragment {

    @Bind(R.id.et_verfy_code_input)
    EditText etInput;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, null);
        return view;
    }

    @OnClick(R.id.btn_ok)
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_ok:
                sendRescueMsg();
                break;
            default:
                break;
        }
    }

    private void sendRescueMsg() {
        String text = etInput.getText().toString();
        RescueAskAction askEvent = new RescueAskAction();
        askEvent.text = text;
        BusManger.getInstance().post(askEvent);
        BusManger.getInstance().post(new ChangeFragmentEvent(1));
    }

}