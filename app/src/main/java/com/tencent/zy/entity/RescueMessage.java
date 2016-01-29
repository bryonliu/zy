package com.tencent.zy.entity;

import com.alibaba.fastjson.JSON;

/**
 * Created by bryonliu on 2016/1/27.
 */
public class RescueMessage {

    public String text;
    public Person fromPerson;
    public Person toPerson;

    public RescurState rescurState;

    public String toJsonString() {
        return JSON.toJSONString(this);
    }


    public enum RescurState {
        WAIT("请求中"), ACCEPT("已接受"), REJECT("拒绝");
        String stateDes;

        private RescurState(String stateDes) {
            this.stateDes = stateDes;
        }
        public String des() {
            return stateDes;
        }
    }

}
