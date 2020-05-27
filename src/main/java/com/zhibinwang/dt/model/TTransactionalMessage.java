package com.zhibinwang.dt.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TTransactionalMessage {
    private Long id;

    private LocalDateTime createTime;

    private LocalDateTime editTime;

    private String creator;

    private String editor;

    private Integer deleted;

    private Integer currentRetryTimes;

    private Integer maxRetryTimes;

    private String queueName;

    private String exchangeName;

    private String exchangeType;

    private String routingKey;

    private String businessModule;

    private String businessKey;

    private LocalDateTime nextScheduleTime;

    private Integer messageStatus;

    private Long initBackoff;

    private Integer backoffFactor;


}