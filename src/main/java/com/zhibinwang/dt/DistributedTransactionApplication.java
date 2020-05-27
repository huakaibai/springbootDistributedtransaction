package com.zhibinwang.dt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhibin.wang
 * @desc
 **/
@SpringBootApplication
@MapperScan("com.zhibinwang.dt.mapper")
public class DistributedTransactionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributedTransactionApplication.class, args);
    }
}
