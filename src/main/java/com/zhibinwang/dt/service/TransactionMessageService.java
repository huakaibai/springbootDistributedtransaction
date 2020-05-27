package com.zhibinwang.dt.service;

/**
 * @author zhibin.wang
 * @create 2020-05-26 10:52
 * @desc 堆外提供的服务接口类
 **/
public interface TransactionMessageService {

    /**
     * 发送消息到mq
     * @param destination
     *
     *
     * @param message
     */
    void sendTransactionalMessage(Destination destination, TxMessage message);
}
