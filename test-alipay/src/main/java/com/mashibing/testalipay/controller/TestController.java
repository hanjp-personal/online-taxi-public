package com.mashibing.testalipay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-alipay")
    public String test(){
        return "test-alipay";
    }
}
