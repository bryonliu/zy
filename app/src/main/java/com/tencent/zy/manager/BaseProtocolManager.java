package com.tencent.zy.manager;

import com.qq.taf.jce.JceStruct;
import com.tencent.zy.protocol.ProtocolListener;

/**
 * 协议基类
 * @author benpeng
 */
public abstract class BaseProtocolManager extends BaseManager implements ProtocolListener {

    protected int send(JceStruct request) {
        //return ProtocolClient.request(request, this);
        return  0;
    }

    public static void cancel(int requestId) {
      //  ProtocolClient.cancel(requestId);
    }

    @Override
    public void onFinish(int resultCode, int cmdId, int requestId, JceStruct request, JceStruct response) {
       /* // 一个正常的网络回包
        if (resultCode == ProtocolResultCode.OK && response != null && request != null) {
            onRequestSuccessed(requestId, cmdId, request, response);
        } else {
            onRequestFailed(requestId, cmdId, resultCode, request, response);
        }*/
    }

    /**
     * 请求成功
     */
    abstract protected void onRequestSuccessed(int requestId, int cmdId, JceStruct request, JceStruct response);

    /**
     * 请求失败
     * @param errorCode     错误码
     */
    abstract protected void onRequestFailed(int requestId, int cmdId, int errorCode, JceStruct request,
            JceStruct response);
    
}
