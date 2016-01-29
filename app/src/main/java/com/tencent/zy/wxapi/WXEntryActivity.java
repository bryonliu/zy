package com.tencent.zy.wxapi;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.zy.login.BaseLoginEngine;
import com.tencent.zy.login.WxLoginEngine;
import com.tencent.zy.manager.LoginManager;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	// APP_ID 应用从官方网站申请到的合法appId
	public static final String APP_ID = "wx7b9cdbbead2e71b4";
	// APP_SECRET 应用从官方网站申请到的合法appsecret								 
	public static final String APP_SECRET = "18e36ebf5134368b9e516c55d3e3002b";
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		api.registerApp(APP_ID); 
		api.handleIntent(this.getIntent(), this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub		
	    Log.e("visongu", "onReq");
	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub
		Log.e("visongu", "onResp");
		if(LoginManager.getInstance().getLoginEngine() != null && LoginManager.getInstance().getLoginEngine() instanceof WxLoginEngine)
		    LoginManager.getInstance().getLoginEngine().loginEvent(BaseLoginEngine.LoginEvent.WX_LOGIN_RESP, arg0);
		/*if(LoginManager.getInstance().getLoginEngine() instanceof WxLoginEngine){
			WxLoginEngine engine = (WxLoginEngine) LoginManager.getInstance().getLoginEngine();
			engine.onResp(arg0);
		}*/
		finish();
	}
	
	

}
