package com.tencent.zy.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.util.Log;

import com.qq.taf.jce.JceStruct;
import com.tencent.zy.utils.ApkUtil;
import com.tencent.zy.utils.NetUtils;
import com.tencent.zy.login.BaseLoginEngine;
import com.tencent.zy.login.UserIdentity;
import com.tencent.zy.login.WxLoginEngine;
import com.tencent.zy.manager.callback.LoginCallback;

/**
 * @author visongu
 * @date 2015/10/02 登陆管理器
 */
public class LoginManager extends BaseProtocolManager {

    private String TAG = "bryon";

    public static final int LOGIN_UI_EVENT_OK = 1;
    public static final int LOGIN_UI_EVENT_FAILED = 2;
    public static final int LOGIN_UI_EVENT_LOGINING = 3;
    public static final int LOGIN_UI_EVENT_LOGOUT = 4;
    public static final int LOGIN_ERROR_NO_NETWORK = 5;
    public static final int LOGIN_ERROR_WX_INSTALL = 6;
    public static final int LOGIN_ERROR_QQ_INSTALL = 7;
    public static final int LOGIN_AUTH_LOGIN_ERROR = 8;
    public static final int LOGIN_AUTH_LOGIN_CANCEL = 9;
    public static final int LOGIN_AUTH_LOGIN_SUCESS = 10;
    public static final int LOGIN_GET_IDENTIFY_CODE_FAIL = 11;
    public static final int LOGIN_GET_IDENTIFY_CODE_SUCESS = 12;

    private BaseLoginEngine mLoginEngine = null;
    private Activity mCurrentActivity = null;
    // private IUiListener qqLoinListener = null;
    private UserIdentity mUserIdentity;

    private List<LoginCallback> mLoginCallBacks;

    // 手机验证码相关，应该抽离出来一个Engine来更合适 后面改进
    public static final int GET_IDENTIFY_CODE_WRONG_PHONE_NUMBER = -2;
    public static final int GET_IDENTIFY_CODE_MAX_TIME = -3;
    public static final int GET_IDENTIFY_SUCESS = 0;
    public static final int GET_IDENTIFY_ERROR = -1;

    private HashMap<String, Integer> mGetIdentifHistory = new HashMap<String, Integer>();
    public final static int MAX_GETIDENTIFY_TIME = 5;

    public static enum LoginType {
        WX, MOBLIEQQ, NONE
    }

    private static class Singleton {
        private static LoginManager sInstance = new LoginManager();
    }

    public static LoginManager getInstance() {
        return Singleton.sInstance;
    }

    private LoginManager() {
        mLoginCallBacks = new ArrayList<LoginCallback>();
    }

    private BaseLoginEngine.AuthLoginCallBack callBack = new BaseLoginEngine.AuthLoginCallBack() {

        @Override
        public void authSucess(UserIdentity identity) {
            // TODO Auto-generated method stub
            Log.e(TAG, "AuthSucess");
            onLoginCallBack(LOGIN_AUTH_LOGIN_SUCESS,identity);
            mUserIdentity = identity;
        }

        @Override
        public void authFailed(BaseLoginEngine.LoginEvent event) {
            // TODO Auto-generated method stub
            Log.e(TAG, "authFailed:" + event.toString());
            switch (event) {
                case QQ_LOGIN_CANCEL:
                case WX_LOGIN_CANCEL:
                    onLoginCallBack(LOGIN_AUTH_LOGIN_CANCEL,null);
                    break;
                case QQ_LOGIN_ERROR:
                case WX_LOGIN_ERROR:
                    onLoginCallBack(LOGIN_AUTH_LOGIN_ERROR,null);
                    break;
            }
        }
    };

    private BaseLoginEngine createLoginEngine(LoginType type) {
        if (type == LoginType.WX) {
            if (!ApkUtil.isAppInstalledFromSystem(WxLoginEngine.WX_PACKAGE_NAME)) {
                onLoginCallBack(LOGIN_ERROR_WX_INSTALL,null);
                return null;
            }
            WxLoginEngine wxEngine = new WxLoginEngine();
            wxEngine.registerCallBack(callBack);
            return wxEngine;
        } else if (type == LoginType.MOBLIEQQ) {
            // TODO
        }
        return null;
    }

    public BaseLoginEngine getLoginEngine() {
        return mLoginEngine;
    }

    public void login(LoginType type) {
        if (!NetUtils.isNetworkUseable()) {
            onLoginCallBack(LOGIN_ERROR_NO_NETWORK,null);
            return;
        }
        mLoginEngine = createLoginEngine(type);
        if (mLoginEngine != null)
            mLoginEngine.login();
    }

    public void setCurrentAcitity(Activity activity) {
        mCurrentActivity = activity;
    }

    public void registerCallback(LoginCallback callback) {
        if (callback == null) {
            return;
        }
        if (!mLoginCallBacks.contains(callback))
            mLoginCallBacks.add(callback);
    }

    public void unregisterCallback(LoginCallback callback) {
        if (callback == null) {
            return;
        }
        mLoginCallBacks.remove(callback);
    }

    @Override
    protected void onRequestSuccessed(int requestId, int cmdId, JceStruct request, JceStruct response) {
        //TODO
    }

    @Override
    protected void onRequestFailed(int requestId, int cmdId, int errorCode, JceStruct request, JceStruct response) {
        //TODO
    }

    private void onLoginCallBack(final int loginState, final UserIdentity userIdentity) {
        runOnUI(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (LoginCallback callBack : mLoginCallBacks) {
                    if (callBack != null) {
                        try {
                            callBack.onLoginFinish(loginState,userIdentity);
                        } catch (Exception e) {
                            Log.e(TAG,e.getMessage(),e);
                        }
                    }
                }
            }
        });
    }

}
