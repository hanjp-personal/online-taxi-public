package com.mashibing.servicedriveruser.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private DriverUserService driverUserService;

    @GetMapping("/test")
    public  String test(){
        return "service-driver-user";
    }

    @GetMapping("/test-Db")
    public ResponseResult testDb(){
        return driverUserService.testDriverUser();
    }
}
