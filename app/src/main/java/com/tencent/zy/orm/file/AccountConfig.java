package com.tencent.zy.orm.file;

import com.tencent.zy.entity.Person;

public class AccountConfig {

    public static final String KEY_CATEGORY = "category";
    public static final String ZY_FILE_NAME = "login_ini";
    public static final String KEY_USER_INFO = "user_info";
    public static final String KEY_OPEN_ID = "userid";
    public static final String KEY_NICKNAME = "nickname";
    public static final String KEY_AVATARURL = "avatarurl";

    private static Person person;

    public static synchronized void setAccount(String openId, String nickName, String avatarUrl,
            Person.PersonCatory catory) {
        Person person = getAccount();
        person.openId = openId;
        person.nickName = nickName;
        person.avatarUrl = avatarUrl;
        person.catory = catory;
        SharedPreferencesHelper.get().put(ZY_FILE_NAME, KEY_OPEN_ID, openId);
        SharedPreferencesHelper.get().put(ZY_FILE_NAME, KEY_NICKNAME, nickName);
        SharedPreferencesHelper.get().put(ZY_FILE_NAME, KEY_AVATARURL, avatarUrl);
        SharedPreferencesHelper.get().put(ZY_FILE_NAME, KEY_CATEGORY, catory.ordinal());
    }

    public static synchronized void setAccount(Person account) {
        if (account != null) {
            setAccount(account.openId, account.nickName, account.avatarUrl, account.catory);
        }
    }

    public static synchronized Person getAccount() {
        if (person == null) {
            person = new Person();
            person.openId = SharedPreferencesHelper.get().getString(ZY_FILE_NAME, KEY_OPEN_ID, "");
            person.nickName = SharedPreferencesHelper.get().getString(ZY_FILE_NAME, KEY_NICKNAME, "");
            person.avatarUrl = SharedPreferencesHelper.get().getString(ZY_FILE_NAME, KEY_AVATARURL, "");
            person.catory = Person.PersonCatory.get(SharedPreferencesHelper.get().getInt(ZY_FILE_NAME, KEY_CATEGORY,
                    0));
        }
        return person;
    }

    public static synchronized String openId() {
        return getAccount().openId;
    }

    public static synchronized Person.PersonCatory category() {
        return getAccount().catory;
    }

    public static synchronized String nickName() {
        return getAccount().nickName;
    }

    public static synchronized String avatarUrl() {
        return getAccount().avatarUrl;
    }

    /**
     * 清楚用户信息
     */
    public static void clear() {
        setAccount(new Person());
    }
}
