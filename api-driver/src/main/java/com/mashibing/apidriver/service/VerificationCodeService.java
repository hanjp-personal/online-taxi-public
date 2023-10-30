package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceUserClient;
import com.mashibing.internalcommon.Response.DriverUserResponse;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.DriverCarConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerificationCodeService {

    @Autowired
    private ServiceUserClient serviceUserClient;

    public ResponseResult checkAndsSendVerificationCode(String driverPhone){
        //查询手机号是否存在
        ResponseResult<DriverUserResponse> driverUserResponseResponseResult = serviceUserClient.checkDriverUser(driverPhone);
        DriverUserResponse data = driverUserResponseResponseResult.getData();
        Integer ifExsit = data.getState();
        if (ifExsit.intValue() == DriverCarConstants.DRIVER_NOT_EXIST){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXIST.getCode(),CommonStatusEnum.DRIVER_NOT_EXIST.getValue());
        }
        log.info(driverPhone+ " 的司机存在");

        //获取验证码

        //调用第三方验证码

        //存入redis


        return ResponseResult.success("");
    }
}
