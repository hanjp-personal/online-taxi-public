package com.mashibing.serviceorder.controller;

import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.serviceorder.mapper.OrderInfoMapper;
import com.mashibing.serviceorder.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    OrderInfoMapper orderInfoMapper;

    @Value("${server.port}")
    String port;
    @GetMapping("/test")
    public String test(){
        return "service-order";
    }

    /**
     * 测试派单逻辑
     * @param orderId
     * @return
     */
    @GetMapping("test-real-time-order/{orderId}")
    public String dispatchRealTimeOrder(@PathVariable("orderId") Long orderId){
        System.out.println("service-order 端口"+port+" 并发测试，OrderId:"+orderId);
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfoService.dispatchOrder(orderInfo);
        return "test-real-time success";
    }
}
