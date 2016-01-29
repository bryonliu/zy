package com.tencent.zy.login;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth.Resp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.zy.app.ZyApplication;
import com.tencent.zy.wxapi.WXEntryActivity;

public class WxLoginEngine extends BaseLoginEngine {

    public final static int HTTP_CONNECTIONTIMEOUT = 30 * 1000; // 30 seconds
    public final static int HTTP_SOTIMEOUT = 30 * 1000; // 30 seconds
    public final static int HTTP_SOCKETBUFFERSIZE = 4 * 1024; // 4k, the default
    public final static int GET_ACESSTOKEN_SUCESS = 1;
    public final static int GET_USERINFO_SUCESS = 2;
    public final static int GET_USERINFO_FAIL = 3;
    public static final String WX_PACKAGE_NAME = "com.tencent.mm";// 微信的包名
    private String requestCode = null;
    private GetInfoHandler mHandler = new GetInfoHandler();
    private final String TAG = "WxLoginEngine";

    @Override
    public void login() {
        Log.d("visongu", "WxLoginEngine login");
        // TODO Auto-generated method stub
        Context context = ZyApplication.self();
        IWXAPI api = WXAPIFactory.createWXAPI(context, WXEntryActivity.APP_ID, true);
        api.registerApp(WXEntryActivity.APP_ID);

        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
    }

    /*
     * public void onResp(BaseResp resp) { if (resp.errCode == BaseResp.ErrCode.ERR_OK) { if (resp instanceof SendAuth.Resp) { SendAuth.Resp mResp = (Resp)
     * resp; if (!TextUtils.isEmpty(mResp.code)) { GetUserInfo(mResp.code); } } } }
     */

    /*
     * 获取WX登录AcessToken
     */
    public void getUserInfo(String code) {
        requestCode = code;
        GetInfoThead thread = new GetInfoThead();
        thread.start();
    }

    public HttpClient createHttpClient() {
        HttpClient httpClient = null;
        HttpHost proxy = null;

        HttpParams params = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(params, HTTP_CONNECTIONTIMEOUT);
        HttpConnectionParams.setSoTimeout(params, HTTP_SOTIMEOUT);
        HttpConnectionParams.setSocketBufferSize(params, HTTP_SOCKETBUFFERSIZE);
        HttpClientParams.setRedirecting(params, true);

        httpClient = new DefaultHttpClient(params);
        return httpClient;
    }

    public class GetInfoThead extends Thread {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            HttpClient client = createHttpClient();
            HttpGet request = new HttpGet();
            // 拼接微信获取AccessToken请求
            StringBuffer sb = new StringBuffer();
            String getATurl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=";
            sb.append(getATurl).append(WXEntryActivity.APP_ID).append("&secret=").append(WXEntryActivity.APP_SECRET)
                    .append("&code=").append(requestCode).append("&grant_type=authorization_code");
            try {
                URI url = new URI(sb.toString());
                request.setURI(url);
                HttpResponse response = client.execute(request);
                int status = response.getStatusLine().getStatusCode();
                if (status == HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String content = reader.readLine();
                    Log.e("visongu", "GetInfoThead:" + content.toString());
                    JSONObject jsonAcessToken = new JSONObject(content.toString());
                    String acessToken = jsonAcessToken.getString("access_token");
                    String openID = jsonAcessToken.getString("openid");
                    String expires = jsonAcessToken.getString("expires_in");
                    String refreshToken = jsonAcessToken.getString("refresh_token");
                    mIdentity = new UserIdentity(openID, acessToken, expires, refreshToken, UserIdentity.WX_LOGIN_TYPE);
                    StringBuffer getUserInfo = new StringBuffer();
                    getUserInfo.append("https://api.weixin.qq.com/sns/userinfo?access_token=").append(acessToken)
                            .append("&openid=").append(openID);

                    URI getUserInfoUrl = new URI(getUserInfo.toString());
                    request.setURI(getUserInfoUrl);
                    HttpResponse userInfoResponse = client.execute(request);
                    if (status == HttpStatus.SC_OK) {
                        HttpEntity UserInfoentity = userInfoResponse.getEntity();
                        is = UserInfoentity.getContent();
                        reader = new BufferedReader(new InputStreamReader(is));
                        String userInfoContent = reader.readLine();
                        JSONObject userInfo = new JSONObject(userInfoContent.toString());

                        if (mHandler != null) {
                            Message msg = mHandler.obtainMessage();
                            msg.obj = userInfo;
                            msg.what = GET_USERINFO_SUCESS;
                            mHandler.sendMessage(msg);
                        }
                    }
                }
            } catch(Exception e){
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(GET_USERINFO_FAIL);
                }
            } finally {
                if (client != null) {
                    client.getConnectionManager().shutdown();
                    client = null;
                }
            }
        }
    }

    public class GetInfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case GET_USERINFO_SUCESS:
                    JSONObject userInfo = (JSONObject) msg.obj;
                    try {
                        String nickname = userInfo.getString("nickname");
                        String headimgurl = userInfo.getString("headimgurl");
                        if (mIdentity != null) {
                            mIdentity.setNickName(nickname);
                            mIdentity.setAvatarUrl(headimgurl);
                        }
                        finishLogin();
                        Log.e("visongu", userInfo.toString());
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case GET_USERINFO_FAIL:
                    authFailed(LoginEvent.WX_LOGIN_ERROR);
            }
        }

    }

    @Override
    public void loginEvent(LoginEvent event, Object o) {
        Log.e(TAG,"loginEvent~~");
        switch (event) {
            case WX_LOGIN_RESP:
                if (!(o instanceof BaseResp))
                    return;
                BaseResp resp = (BaseResp) o;
                Log.e(TAG,"loginEvent WX_LOGIN_RESP:" + resp.errCode );
                switch(resp.errCode){
                    case BaseResp.ErrCode.ERR_OK:
                        if (resp instanceof SendAuth.Resp) {
                            SendAuth.Resp mResp = (Resp) resp;
                            if (!TextUtils.isEmpty(mResp.code)) {
                                Log.e("visongu", "loginEvent openId: " + mResp.openId);
                                Log.e("visongu", "loginEvent code: " + mResp.code);
                                getUserInfo(mResp.code);
                            }
                        }
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        authFailed(LoginEvent.WX_LOGIN_CANCEL);
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        authFailed(LoginEvent.WX_LOGIN_ERROR);
                        break;
                }
                
            /*    if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                    if (resp instanceof SendAuth.Resp) {
                        SendAuth.Resp mResp = (Resp) resp;
                        if (!TextUtils.isEmpty(mResp.code)) {
                            Log.e("visongu", "loginEvent openId: " + mResp.openId);
                            Log.e("visongu", "loginEvent code: " + mResp.code);
                            getUserInfo(mResp.code);
                        }
                    }
                }*/
                break;
        }
    }

}
