package com.zhibinwang.dt.controller;

import com.zhibinwang.dt.mock.MockBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 花开
 * @create 2020-05-27 17:07
 * @desc 测试控制器
  **/
@RestController
public class McokController {

    @Autowired
    private MockBusinessService mockBusinessService;

    @RequestMapping("/mockBusiness")
    public String mockBusiness() throws Exception {
        mockBusinessService.saveOrder();
        return "success";
    }
}
