package com.tencent.zy.login;

public abstract class BaseLoginEngine {

	public enum LoginEvent{
		QQ_LOGIN_COMPLETE,
		QQ_LOGIN_CANCEL,
		QQ_LOGIN_ERROR,
	    QQ_GET_USER_INFO_COMPLETE,
		WX_LOGIN_RESP,
		WX_LOGIN_CANCEL,
		WX_LOGIN_ERROR
	}
	
	protected UserIdentity mIdentity;
	private AuthLoginCallBack mAuthCallBack;

	public abstract void login();
	
/*	public abstract void getIdentity();*/
	
	public abstract void loginEvent(LoginEvent event,Object o);
	
	public void registerCallBack(AuthLoginCallBack callBack){
		mAuthCallBack = callBack;
	}
	
	public void finishLogin(){
		mAuthCallBack.authSucess(mIdentity);
	}
	
	public void authFailed(LoginEvent event){
	    mAuthCallBack.authFailed(event);
	}
	
	public interface AuthLoginCallBack{
		public void authSucess(UserIdentity identity);
		public void authFailed(LoginEvent event);
	}
	
}
