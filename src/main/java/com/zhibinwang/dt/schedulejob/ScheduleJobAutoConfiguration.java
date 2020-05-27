package com.zhibinwang.dt.schedulejob;

import com.zhibinwang.dt.service.impl.TransactionalMessageManagementService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

/**
 * @author zhibin.wang
 * @desc 定时任务配置
 **/
@Slf4j
@Configuration
@EnableScheduling
public class ScheduleJobAutoConfiguration {

    @Autowired
    private TransactionalMessageManagementService transactionalMessageManagementService;


    /**
     *
     */
    @Autowired
    private  RedissonClient redisson ;

    //每10s执行一次
    @Scheduled(fixedDelay = 10000)
    public void transactionalMessageCompensationTask() throws  Exception{
        RLock lock = redisson.getLock("transactionalMessageCompensationTask");

        /**
         * 等待时间5s，预期300s执行完毕，这两个值需要根据实际使用场景定制
         */
        boolean tryLock = lock.tryLock(5, 300, TimeUnit.SECONDS);
        if (tryLock){
            try {
                long start = System.currentTimeMillis();
                transactionalMessageManagementService.processPendingCompensationRecords();
                long end = System.currentTimeMillis();
                long delta = end-start;
                //防止锁过早释放 什么意思
                if (delta < 5000){
                    Thread.sleep(5000-delta);
                }
                log.info("定时任务补偿机制，耗时{}",delta);

            }finally {
                lock.unlock();
            }


        }
    }

}
