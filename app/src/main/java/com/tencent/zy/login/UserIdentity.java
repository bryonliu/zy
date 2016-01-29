package com.tencent.zy.login;

public class UserIdentity {

    public static byte QQ_LOGIN_TYPE = 1;
    public static byte WX_LOGIN_TYPE = 2;
    public static byte MB_LOGIN_TYPE = 3;
    /** 授权登录所获取的openId */
    public String openId;

    // 授权登录获取的Nickname
    public String nickName;

    // 授权登录获取登录类型
    public byte loginType;

    // 授权登录获取的头像URL
    public String avatarUrl;

    // 授权登录接口调用凭证
    public String acessToken;

    // 用户刷新access_token WX专用
    public String refreshToken;

    // access_token接口调用凭证超时时间
    public String expires_in;
    
    public String phoneNumber;
    
    public String pinCode;
    
    public UserIdentity() {
        this.openId = "";
        this.acessToken = "";
        this.expires_in = "";
        this.refreshToken = "";
        this.loginType = 0;
    }

    public UserIdentity(String openId, String acessToken, String expires_in, String refreshToken, byte loginType) {
        this.openId = openId;
        this.acessToken = acessToken;
        this.expires_in = expires_in;
        this.refreshToken = refreshToken;
        this.loginType = loginType;
    }
    
    public UserIdentity(String phoneNumber,String pinCode) {
        this.openId = "";
        this.acessToken = "";
        this.expires_in = "";
        this.refreshToken = "";
        this.loginType = 3;
        this.phoneNumber = phoneNumber;
        this.pinCode = pinCode;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public byte getLoginType() {
        return loginType;
    }

}
