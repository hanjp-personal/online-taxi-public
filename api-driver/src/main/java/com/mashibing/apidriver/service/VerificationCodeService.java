package com.mashibing.apidriver.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    public ResponseResult checkAndsSendVerificationCode(String driverPhone){
        //查询手机号时复存在

        //获取验证码

        //调用第三方验证码

        //存入redis


        return ResponseResult.success("");
    }
}
