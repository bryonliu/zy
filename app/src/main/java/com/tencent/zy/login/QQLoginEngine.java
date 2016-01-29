/*
package com.tencent.zy.login;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.ny.NyApplication;
import com.tencent.ny.util.XLog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

public class QQLoginEngine extends BaseLoginEngine {

    private String TAG = QQLoginEngine.class.getName();
    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";//QQ的包名
	private Tencent mTencent;
	private static String mQQAppid = "1104886992";
	private IUiListener loginListener;
	private Activity currentActivity;
	private UserInfo mUserInfo;

	public void initEngine(Activity activity, IUiListener loginListener) {
		currentActivity = activity;
		loginListener = loginListener;
	}

	public void getUserInfo() {
		if (ready(currentActivity)) {
			mUserInfo = new UserInfo(currentActivity, mTencent.getQQToken());
			mUserInfo.getUserInfo(new QQUiListener(currentActivity,
					"get_simple_userinfo"));
		}
	}

	@Override
	public void login() {
	    XLog.e(TAG, "QQLoginengine login");
		if (mTencent == null)
			mTencent = Tencent.createInstance(mQQAppid, NyApplication.self());
		// TODO Auto-generated method stub
		mTencent.login(currentActivity, "all", loginListener);
	}

	@Override
	public void loginEvent(LoginEvent event, Object o) {
		// TODO Auto-generated method stub
	    XLog.e(TAG, "QQ loginEvent:" + event.toString());
		switch (event) {
		case QQ_LOGIN_COMPLETE:
			try {
			    if(o == null)
			        authFailed(LoginEvent.QQ_LOGIN_ERROR);
				initOpenidAndToken((JSONObject) o);
				getUserInfo();
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case QQ_GET_USER_INFO_COMPLETE:
			try {
				JSONObject jsonResponse = (JSONObject) o;
				String mNickName = (String) jsonResponse.get("nickname");
				String headimgurl = (String) jsonResponse.get("figureurl_qq_2");
				if(mIdentity != null){
					mIdentity.setNickName(mNickName);
					mIdentity.setAvatarUrl(headimgurl);
				}
				finishLogin();
			} catch (Exception e) {
				// TODO: handle exception
			}
			break;
		case QQ_LOGIN_CANCEL:
		    authFailed(LoginEvent.QQ_LOGIN_CANCEL);
		    break;
		case QQ_LOGIN_ERROR:
		    authFailed(LoginEvent.QQ_LOGIN_ERROR);
		    break;
		default:
			break;
		}
	}

	public boolean ready(Context context) {
		if (mTencent == null) {
			return false;
		}
		boolean ready = mTencent.isSessionValid()
				&& mTencent.getQQToken().getOpenId() != null;
		if (!ready) {
			Log.e("visongu", "login and get openId first, please!");
		}
		return ready;
	}

	public void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);

			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mIdentity = new UserIdentity(openId, token, expires, null, UserIdentity.QQ_LOGIN_TYPE);
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
				Log.e("visongu", "QQ : " + openId + "  " + token + " ");
			}
		} catch (Exception e) {
		}
	}

}
*/
