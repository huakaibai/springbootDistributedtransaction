package com.zhibinwang.dt.model;

import lombok.Data;

/**
 * @author zhibin.wang
 * @desc 事务消息内容
 **/
@Data
public class TransactionalMessageContent {

    private Long id;
    private Long messageId;
    private String content;
}

