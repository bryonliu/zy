package com.tencent.zy.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;

/**
 * Created by bryonliu on 2016/1/26.
 */
public class Person {
    private static final String TAG = "bryon";
    public String nickName;
    public String avatarUrl;
    public String openId;

    public PersonCatory catory;

    public enum PersonCatory {
        Asker("求援者"), Answer("支援者");
        public String des;

        public static PersonCatory get(int order) {
            if (order == Answer.ordinal()) {
                return Answer;
            }
            return Asker;
        }

        private PersonCatory(String des) {
            this.des = des;
        }

    }

    public static Person parserFromJson(String jsonStr) {
        return JSON.parseObject(jsonStr, Person.class);
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }

    public JSONObject toJsonObject() {
        try {
            JSONObject jsonObject = new JSONObject(toJsonString());
            return jsonObject;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
