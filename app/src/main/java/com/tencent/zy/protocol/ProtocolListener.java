package com.tencent.zy.protocol;

import com.qq.taf.jce.JceStruct;

/**
 * 
 * @author trentyang
 * TODO: 改为RequestListener
 * 
 */
public interface ProtocolListener {

    public void onFinish(int resultCode, int cmdId, int requestId, JceStruct request, JceStruct response);

}
