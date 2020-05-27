package com.zhibinwang.dt.enump;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author zhibin.wang
 * @create 2020-05-26 11:14
 * @desc 事务消息状态
 **/
@Getter
@RequiredArgsConstructor
public enum TxMessageStatus {

    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 待处理
     */
    PENDING(0),

    /**
     * 失败
     */
    FAIL(-1);



   private final  Integer status;
}
