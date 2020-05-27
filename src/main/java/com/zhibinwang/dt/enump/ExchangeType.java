package com.zhibinwang.dt.enump;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhibin.wang
 * @create 2020-05-26 10:52
 * @desc 交换机类型
 **/
@Getter
@RequiredArgsConstructor
public enum ExchangeType {

    FANOUT("fanout"),
    DIRECT("direct"),
    TOPIC("topic"),
    DEFAULT("");
    private final  String type;
}
