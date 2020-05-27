package com.zhibinwang.dt.mock;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
//启动Spring
@SpringBootTest
public class MockBusinessServiceTest {

    @Autowired
    MockBusinessService businessService;

    @org.junit.Test
    public void saveOrder() throws Exception {

        businessService.saveOrder();
    }
}