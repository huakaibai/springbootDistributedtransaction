package com.zhibinwang.dt.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhibinwang.dt.enump.ExchangeType;
import com.zhibinwang.dt.mapper.TOrderMapper;
import com.zhibinwang.dt.model.TOrder;
import com.zhibinwang.dt.service.Destination;
import com.zhibinwang.dt.service.TransactionMessageService;
import com.zhibinwang.dt.service.TxMessage;
import com.zhibinwang.dt.service.impl.DefaultDestination;
import com.zhibinwang.dt.service.impl.DefaultTxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author zhibin.wang
 * @desc 测试rabbitmq分布式事务，软一致性
 **/
@Service
@Slf4j
public class MockBusinessService {

    @Autowired
    private TOrderMapper tOrderMapper;

    @Autowired
    private TransactionMessageService transactionMessageService;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveOrder() throws  Exception{

        String orderId = UUID.randomUUID().toString();
        BigDecimal amount = BigDecimal.valueOf(100l);
        TOrder tOrder = new TOrder();
        tOrder.setOrderId(orderId);
        tOrder.setAmount(amount);
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderId);
        map.put("amount",amount);

        String content = objectMapper.writeValueAsString(map);
        log.info("发送消息:{}",content);
        // 数据库保存订单信息
        tOrderMapper.insertSelective(tOrder);
        DefaultDestination destination = DefaultDestination.builder()
                .exchangeName("tm.test.exchange")
                .queueName("tm.test.queue")
                .routingKey("tm.test.key")
                .exchangeType(ExchangeType.DIRECT)
                .build();
        log.info("DefaultDestination={}",destination);
        DefaultTxMessage txMessage = DefaultTxMessage.builder()
                .businessKey(orderId)
                .businessModule("SAVE_ORDER")
                .content(content)
                .build();
        log.info("DefaultTxMessage={}",txMessage);
        transactionMessageService.sendTransactionalMessage(destination,txMessage);

        log.info("保存订单成功,orderId:{}",orderId);

    }
}
