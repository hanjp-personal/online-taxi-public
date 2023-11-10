package com.mashibing.apipassenger.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayController payController;


    @PostMapping("/push-pay-info")
    public ResponseResult pushPayInfo(@RequestParam String orderId, @RequestParam String price, @RequestParam String passengerId){

        return payController.pushPayInfo(orderId,price,passengerId);

    }
}
