package com.mashibing.apidriver.controller;

import com.mashibing.apidriver.service.UserService;
import com.mashibing.internalcommon.dto.DriverUser;
import com.mashibing.internalcommon.dto.DriverUserWorkStatus;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/user")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser){
        return userService.updateDriverUser(driverUser);
    }
    @PostMapping("/driver-user-work-status")
    public ResponseResult changeDriverStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus){
        return userService.changeWorkStatus(driverUserWorkStatus);
    }
    @GetMapping("/testttt")
    private String test(){
        return "api-driverttt";
    };

}
