package com.tencent.zy.ui.activity;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;

import com.tencent.zy.R;
import com.tencent.zy.entity.Person;
import com.tencent.zy.login.UserIdentity;
import com.tencent.zy.manager.LoginManager;
import com.tencent.zy.manager.callback.LoginCallback;
import com.tencent.zy.orm.file.AccountConfig;

/**
 * Created by bryonliu on 2016/1/25.
 */
public class LoginActivity extends BaseActivity implements LoginCallback {
    private static final String TAG = "bryon";

    @Bind(R.id.v_login)
    View vLogin;
    @Bind(R.id.v_verification)
    View vVerification;

    @Bind(R.id.v_user_info)
    View vUserInfo;

    @Bind(R.id.et_verfy_code_input)
    EditText evVerifyCodeInput;

    @Bind(R.id.ev_telephone)
    EditText evTelePhone;

    @Bind(R.id.ev_id_card)
    EditText evIDcard;

    @Bind(R.id.ev_home_address)
    EditText evHomeAdd;

    @Bind(R.id.ev_emergency_contact_info_1)
    EditText evEmergencyContactFirst;

    @Bind(R.id.ev_emergency_contact_info_2)
    EditText evEmergencyContactSecond;

    @Bind(R.id.ev_medical_record)
    EditText evMedicalRecord;

    @Bind(R.id.sp_user_category)
    Spinner spUserCategory;

    private Person person = new Person();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (AccountConfig.getAccount() != null && !TextUtils.isEmpty(AccountConfig.openId())) {
            MainActivity.start(this);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginManager.getInstance().registerCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().unregisterCallback(this);
    }

    @OnClick(R.id.tv_wx_login)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_wx_login:
                wxLogin();
                break;
            default:
                break;
        }
    }

    private void wxLogin() {
        LoginManager.getInstance().login(LoginManager.LoginType.WX);
        /*
         * UserIdentity userIdentity = new UserIdentity(); userIdentity.setAvatarUrl("https://img3.doubanio.com/spic/s28343210.jpg"); userIdentity.openId =
         * "10010101"; userIdentity.nickName = "Monkey.D"; onLoginFinish(10, userIdentity);
         */
    }

    @OnClick({ R.id.btn_ok, R.id.btn_submit_user_info })
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                verifyCode();
                break;
            case R.id.btn_submit_user_info:
                submitUserInfo();
                break;
            default:
                break;
        }

    }

    private void submitUserInfo() {

        int position = spUserCategory.getSelectedItemPosition();
        person.catory = position == 0 ? Person.PersonCatory.Asker : Person.PersonCatory.Answer;

        // TODO add user info to person
        Observable.just(person).map(new Func1<Person, Boolean>() {
            @Override
            public Boolean call(Person person) {
                // TODO
                return true;
            }
        }).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                AccountConfig.setAccount(person);
                MainActivity.start(LoginActivity.this);
                LoginActivity.this.finish();
            }
        });

    }

    private void verifyCode() {
        String verifyCode = evVerifyCodeInput.getText().toString().trim();

        Observable.just(verifyCode).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                // TODO 去验证邀请码

                return true;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    vVerification.setVisibility(View.GONE);
                    vUserInfo.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(LoginActivity.this, "请输入正确的邀请码", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onLoginFinish(int loginState, UserIdentity userIdentity) {
        Log.d(TAG, loginState + "");
        if (loginState == LoginManager.LOGIN_AUTH_LOGIN_SUCESS) {
            Log.d(TAG, userIdentity.getNickName() + " " + userIdentity.getAvatarUrl() + " " + userIdentity.getOpenId());
            person.openId = userIdentity.getOpenId();
            person.nickName = userIdentity.getNickName();
            person.avatarUrl = userIdentity.getAvatarUrl();
            person.catory = Person.PersonCatory.Asker;
            vLogin.setVisibility(View.GONE);
            vVerification.setVisibility(View.VISIBLE);
        }
    }
}
