package com.zhibinwang.dt.service.impl;

import com.zhibinwang.dt.enump.ExchangeType;
import com.zhibinwang.dt.model.TransactionalMessage;
import com.zhibinwang.dt.service.Destination;
import com.zhibinwang.dt.service.TransactionMessageService;
import com.zhibinwang.dt.service.TxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author zhibin.wang
 * @desc rabbitmq消息发送
 **/
@Slf4j
@Service
public class RabbitMqTransactionMessageServiceImpl implements TransactionMessageService {

    @Autowired
    private AmqpAdmin amqpAdmin;

    private final  static Map<String,Boolean> RABITMQ_QUEUE = new ConcurrentHashMap<>();

    @Autowired
    private TransactionalMessageManagementService transactionalMessageManagementService;

    @Override
    public void sendTransactionalMessage(Destination destination, TxMessage message) {

        //交换机名称
        String exchangeName = destination.exchangeName();
        // 交换机类型
        ExchangeType exchangeType = destination.exchangeType();
        //队列名称
        String queueName = destination.queueName();
        // 路由key
        String routingKey = destination.routingKey();

        String businessKey = message.businessKey();

        String businessModule = message.businessModule();

        String content = message.content();

        // 绑定交换机，只绑定一次
        RABITMQ_QUEUE.computeIfAbsent(queueName, s -> {
            Queue queue = new Queue(queueName);
            amqpAdmin.declareQueue(queue);
            Exchange exchange = new CustomExchange(exchangeName, exchangeType.getType());
            amqpAdmin.declareExchange(exchange);
            Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs();
            amqpAdmin.declareBinding(binding);
            return true;
        });

        // 数据库保存事务消息
        TransactionalMessage transactionalMessage = new TransactionalMessage();
        transactionalMessage.setExchangeName(exchangeName);
        transactionalMessage.setExchangeType(exchangeType.getType());
        transactionalMessage.setQueueName(queueName);
        transactionalMessage.setRoutingKey(routingKey);
        transactionalMessage.setBusinessKey(businessKey);
        transactionalMessage.setBusinessModule(businessModule);

        // 保存数据库
        transactionalMessageManagementService.saveTransctionMessage(transactionalMessage,content);

        //注册spring事务同步器
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
         // 在提交事务以后
            @Override
            public void afterCommit() {
                transactionalMessageManagementService.sendMessageToMq(transactionalMessage, content);
            }
        });



    }
}
