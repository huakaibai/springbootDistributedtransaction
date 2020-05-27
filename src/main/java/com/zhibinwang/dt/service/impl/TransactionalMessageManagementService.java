package com.zhibinwang.dt.service.impl;

import com.zhibinwang.dt.enump.TxMessageStatus;
import com.zhibinwang.dt.mapper.TTransactionalMessageContentMapper;
import com.zhibinwang.dt.mapper.TTransactionalMessageMapper;
import com.zhibinwang.dt.model.TTransactionalMessage;
import com.zhibinwang.dt.model.TTransactionalMessageContent;
import com.zhibinwang.dt.model.TTransactionalMessageContentExample;
import com.zhibinwang.dt.model.TTransactionalMessageExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhibin.wang
 * @desc 事务消息管理服务
 **/
@Service
@Slf4j
public class TransactionalMessageManagementService {

    @Autowired
    private TTransactionalMessageMapper transactionalMessageMapper;
    @Autowired
    private TTransactionalMessageContentMapper transactionalMessageContentMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final LocalDateTime END = LocalDateTime.of(2999, 1, 1, 0, 0, 0);
    private static final long DEFAULT_INIT_BACKOFF = 10L;
    private static final int DEFAULT_BACKOFF_FACTOR = 2;
    private static final int DEFAULT_MAX_RETRY_TIMES = 5;
    private static final int LIMIT = 100;

    public void  saveTransctionMessage(TTransactionalMessage transactionalMessage, String content){
        transactionalMessage.setCreateTime(LocalDateTime.now());
        transactionalMessage.setEditTime(LocalDateTime.now());
        transactionalMessage.setCreator("admin");
        transactionalMessage.setCreator("admin");

        // 计算洗一次执行时间
        transactionalMessage.setNextScheduleTime(calculateNextScheduleTime(LocalDateTime.now(), DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, 0));

        transactionalMessage.setBackoffFactor(DEFAULT_BACKOFF_FACTOR);
        transactionalMessage.setCurrentRetryTimes(0);
        transactionalMessage.setMaxRetryTimes(DEFAULT_MAX_RETRY_TIMES);
        transactionalMessage.setMessageStatus(TxMessageStatus.PENDING.getStatus());
        // 插入数据库
        transactionalMessageMapper.insertSelective(transactionalMessage);
        TTransactionalMessageContent transactionalMessageContent = new TTransactionalMessageContent();
        transactionalMessageContent.setContent(content);
        transactionalMessageContent.setMessageId(transactionalMessage.getId());

        //插入数据库
        transactionalMessageContentMapper.insertSelective(transactionalMessageContent);

    }

    /**
     * 发送数据到mq
     * @param transactionalMessage
     * @param content
     */
    public void sendMessageToMq(TTransactionalMessage transactionalMessage,String content){

       try {

           rabbitTemplate.convertAndSend(transactionalMessage.getExchangeName(), transactionalMessage.getRoutingKey(), content);
           //没有报错说明发送成功
           makeSuccess(transactionalMessage);
       }catch (Exception e){
           // 报错说明发送失败
           makeFail(transactionalMessage, e);
       }
    }

    private void  makeSuccess(TTransactionalMessage transactionalMessage){
        // 设置下次重发时间为最大重发时间
        transactionalMessage.setNextScheduleTime(END);

        transactionalMessage.setMessageStatus(TxMessageStatus.SUCCESS.getStatus());
        transactionalMessage.setEditTime(LocalDateTime.now());
        //更新数据库，当前重发次数
        transactionalMessage.setCurrentRetryTimes(transactionalMessage.getCurrentRetryTimes().compareTo(transactionalMessage.getMaxRetryTimes()) >= 0 ?
                transactionalMessage.getMaxRetryTimes() : transactionalMessage.getCurrentRetryTimes() + 1);

        // 更新数据库 id已经赋值并传进去了，所以可以根据id进行更新 TODO
        transactionalMessageMapper.updateByPrimaryKeySelective(transactionalMessage);
    }

    private void makeFail(TTransactionalMessage transactionalMessage,Exception e){
        log.error("发送队列{}消息失败", transactionalMessage.getQueueName(),e);

        transactionalMessage.setEditTime(LocalDateTime.now());

        transactionalMessage.setMessageStatus(TxMessageStatus.FAIL.getStatus());
       Integer retryTimes =  transactionalMessage.getCurrentRetryTimes().compareTo(transactionalMessage.getMaxRetryTimes()) >= 0 ?
                transactionalMessage.getMaxRetryTimes() : transactionalMessage.getCurrentRetryTimes() + 1;
       transactionalMessage.setCurrentRetryTimes(retryTimes);
       // 达到最大重发次数
       if (retryTimes == transactionalMessage.getMaxRetryTimes()){
           // 设置下次重发时间为最大重发时间
           transactionalMessage.setNextScheduleTime(END);
       }else {
           transactionalMessage.setNextScheduleTime(calculateNextScheduleTime(transactionalMessage.getNextScheduleTime(), Long.valueOf(retryTimes)));
       }

       // 更新数据库
        transactionalMessageMapper.updateByPrimaryKeySelective(transactionalMessage);
    }

    /**
     * 进行业务补偿
     */
    public void processPendingCompensationRecords (){


        // 时间的右值为当前时间减去退避初始值，这里预防把刚保存的消息也推送了
        LocalDateTime max = LocalDateTime.now().plusSeconds(-DEFAULT_INIT_BACKOFF);
        // 时间的左值为右值减去1小时
        LocalDateTime min = max.plusHours(-1);
        log.info("重发时间 max={},min={}",max,min);
        TTransactionalMessageExample tTransactionalMessageExample = new TTransactionalMessageExample();
        TTransactionalMessageExample.Criteria criteria = tTransactionalMessageExample.createCriteria();
        criteria.andNextScheduleTimeBetween(min,max);
        // 查询需要进行业务补偿的数据
        List<TTransactionalMessage> tTransactionalMessages = transactionalMessageMapper.selectByExample(tTransactionalMessageExample);
        Map<Long, TTransactionalMessage> collect = tTransactionalMessages.stream().collect(Collectors.toMap(TTransactionalMessage::getId, x -> x));
        // 查询状态为失败，
        Set<Long> messageId = collect.keySet();
        if (messageId == null || messageId.size() < 1){
            log.info("定时任务补偿，无可补偿数据-------------");
            return;
        }

        // 根据id查询消息
        TTransactionalMessageContentExample messageContentExample = new TTransactionalMessageContentExample();
        TTransactionalMessageContentExample.Criteria contentExampleCriteria = messageContentExample.createCriteria();
        contentExampleCriteria.andMessageIdIn(messageId.stream().collect(Collectors.toList()));

        transactionalMessageContentMapper.selectByExample(messageContentExample).stream().forEach(item->{
            TTransactionalMessage message = collect.get(item.getMessageId());
            sendMessageToMq(message, item.getContent());
            log.info("补偿发送分布式事务,消息id{}",item.getMessageId());
        });

    }

    /**
     *
     * @param base 基准时间
     * @param initBackoff 退避基准值
     * @param backoffFactor 退避指数
     * @param round 轮数
     * @return
     */
    public static LocalDateTime calculateNextScheduleTime(LocalDateTime base,
                                                    long initBackoff,
                                                    long backoffFactor,
                                                    long round) {
        double delta = initBackoff * Math.pow(backoffFactor, round);
        return base.plusSeconds((long) delta);
    }


    private LocalDateTime calculateNextScheduleTime(LocalDateTime base,Long round){
        return calculateNextScheduleTime(base, DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, round);
    }


    public static void main(String[] args) {
        LocalDateTime localDateTime1 = LocalDateTime.now();
        LocalDateTime localDateTime2 = calculateNextScheduleTime(localDateTime1, DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, 0);
        System.out.println(localDateTime2);

        LocalDateTime localDateTime3 = calculateNextScheduleTime(localDateTime1, DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, 1);
        System.out.println(localDateTime3);

        LocalDateTime localDateTime4 = calculateNextScheduleTime(localDateTime1, DEFAULT_INIT_BACKOFF,
                DEFAULT_BACKOFF_FACTOR, 2);

        System.out.println(localDateTime4);
    }
}
