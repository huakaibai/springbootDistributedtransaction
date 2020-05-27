package com.zhibinwang.dt.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author 花开
 * @create 2020-05-27 16:41
 * @desc 模拟订单服务
 **/
@Component
public class McokRunner implements ApplicationRunner {

    @Autowired
    private  MockBusinessService mockBusinessService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        mockBusinessService.saveOrder();
    }
}
