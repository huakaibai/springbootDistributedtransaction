package com.zhibinwang.dt.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhibin.wang
 * @desc 事务消息
 **/
@Data
public class TransactionalMessage {

    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime editTime;
    private String creator;
    private String editor;
    //删除
    private Integer deleted;
    // 当前重试次数
    private Integer currentRetryTimes;

    /**
     * 最大重试次数
     */
    private Integer maxRetryTimes;

    /**
     * 队列名
     */
    private String queueName;

    /**
     * 交换机名
     */
    private String exchangeName;

    /**
     * 交换类型
     */
    private String exchangeType;

    /**
     * 路由键
     */
    private String routingKey;

    /**
     * 业务模块
     */
    private String businessModule;

    /**
     * 业务键
     */
    private String businessKey;

    /**
     * 下一次任务执行时间
     */
    private LocalDateTime nextScheduleTime;

    /**
     * 消息状态
     */
    private Integer messageStatus;
    /**
     * 退避初始化值，单位为s
     */
    private Long initBackoff;

    /**
     * 退避因子
     */
    private Integer backoffFactor;

}
