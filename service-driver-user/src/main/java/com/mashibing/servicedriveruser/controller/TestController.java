package com.mashibing.servicedriveruser.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.mapper.DriverUserMapper;
import com.mashibing.servicedriveruser.service.DriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private DriverUserService driverUserService;

    @Autowired
    private DriverUserMapper driverUserMapper;

    @GetMapping("/test")
    public  String test(){
        return "service-driver-user";
    }

    @GetMapping("/test-Db")
    public ResponseResult testDb(){
        return driverUserService.testDriverUser();
    }
    @GetMapping("/test-xml")
    public int testxml(String cityCode){
        int i = driverUserMapper.selectCityDriverUserCountByCityCode(cityCode);
        return i;
    }
}
