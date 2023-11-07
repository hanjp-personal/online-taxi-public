package com.mashibing.serviceorder.controller;

import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.serviceorder.mapper.OrderInfoMapper;
import com.mashibing.serviceorder.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    OrderInfoMapper orderInfoMapper;
    @GetMapping("/test")
    public String test(){
        return "service-order";
    }
    @GetMapping("test-real-time-order/{orderId}")
    public String test_real_time_order(@PathVariable("orderId") Long orderId){
        System.out.println("并发测试，OrderId:"+orderId);
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfoService.dispatchOrder(orderInfo);
        return "test-real-time success";
    }
}
