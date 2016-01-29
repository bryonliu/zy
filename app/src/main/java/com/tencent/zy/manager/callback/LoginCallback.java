package com.tencent.zy.manager.callback;

import com.tencent.zy.login.UserIdentity;

public interface LoginCallback {
    public void onLoginFinish(int loginState,UserIdentity identity);
}
