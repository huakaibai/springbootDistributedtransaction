package com.zhibinwang.dt.service;

import com.zhibinwang.dt.enump.ExchangeType;

/**
 * @author zhibin.wang
 * @create 2020-05-26 10:55
 * @desc 目标地址
 **/
public interface Destination {

    ExchangeType exchangeType();
    String queueName();
    String exchangeName();
    String routingKey();
}
