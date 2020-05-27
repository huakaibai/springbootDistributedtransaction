package com.zhibinwang.dt.service.impl;

import com.zhibinwang.dt.service.TxMessage;
import lombok.Builder;
import lombok.ToString;

/**
 * @author zhibin.wang
 * @desc
 **/

@Builder
@ToString
public class DefaultTxMessage implements TxMessage {

    String businessModule;
    String businessKey;
    String content;

    @Override
    public String businessModule() {
        return businessModule;
    }

    @Override
    public String businessKey() {
        return businessKey;
    }

    @Override
    public String content() {
        return content;
    }
}
