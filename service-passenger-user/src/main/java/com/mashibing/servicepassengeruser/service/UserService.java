package com.mashibing.servicepassengeruser.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public ResponseResult loginOrRegister(String passengerPhone){
        System.out.println("user服务被调用了， 手机号是：" + passengerPhone);
        return ResponseResult.success();
    }
}
