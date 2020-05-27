package com.zhibinwang.dt.service.impl;

import com.zhibinwang.dt.service.TxMessage;
import lombok.Builder;

/**
 * @author zhibin.wang
 * @desc
 **/

@Builder
public class DefaultTxMessage implements TxMessage {

    String businessModule;
    String businessKey;
    String content;

    @Override
    public String businessModule() {
        return null;
    }

    @Override
    public String businessKey() {
        return null;
    }

    @Override
    public String content() {
        return null;
    }
}
