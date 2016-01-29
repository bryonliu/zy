/*
package com.tencent.zy.login;

import android.content.Context;

import com.tencent.ny.login.BaseLoginEngine.LoginEvent;
import com.tencent.ny.manager.LoginManager;
import com.tencent.ny.util.XLog;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class QQUiListener implements IUiListener{
	
	private Context mContext;
	private String mScope;
	private String TAG = "QQUiListener";
	
	public QQUiListener(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public QQUiListener(Context mContext, String mScope) {
		super();
		this.mContext = mContext;
		this.mScope = mScope;
	}
	
	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
	    XLog.e(TAG," QQUiListener onCancel: " + mScope);
		if(mScope == "get_login")
			LoginManager.getInstance().getLoginEngine().loginEvent(LoginEvent.QQ_LOGIN_CANCEL,null);
	}

	@Override
	public void onComplete(Object arg0) {
		// TODO Auto-generated method stub
		if(mScope == "get_login")
			LoginManager.getInstance().getLoginEngine().loginEvent(LoginEvent.QQ_LOGIN_COMPLETE, arg0);
		if(mScope == "get_simple_userinfo")
			LoginManager.getInstance().getLoginEngine().loginEvent(LoginEvent.QQ_GET_USER_INFO_COMPLETE, arg0);
	}

	@Override
	public void onError(UiError arg0) {
		// TODO Auto-generated method stub
		
	}

}
*/
