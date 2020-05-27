package com.zhibinwang.dt.service.impl;

import com.zhibinwang.dt.enump.ExchangeType;
import com.zhibinwang.dt.service.Destination;
import lombok.Builder;
import lombok.ToString;

/**
 * @author zhibin.wang
 * @desc 默认
 **/
@Builder
@ToString
public class DefaultDestination implements Destination {
     ExchangeType exchangeType;
     String queueName;
     String exchangeName;
     String routingKey;

    @Override
    public ExchangeType exchangeType() {
        return exchangeType;
    }

    @Override
    public String queueName() {
        return queueName;
    }

    @Override
    public String exchangeName() {
        return exchangeName;
    }

    @Override
    public String routingKey() {
        return routingKey;
    }
}
