package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceUserClient;
import com.mashibing.apidriver.remote.ServiceVerificationCodeClient;
import com.mashibing.internalcommon.Response.DriverUserResponse;
import com.mashibing.internalcommon.Response.NumberCodeResponse;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.DriverCarConstants;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeService {

    @Autowired
    private ServiceUserClient serviceUserClient;

    @Autowired
    private ServiceVerificationCodeClient serviceVerificationCodeClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

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
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationCodeClient.getNumberCode(6);
        NumberCodeResponse codeResponseData = numberCodeResponse.getData();
        int numberCode = codeResponseData.getNumberCode();
        log.info("获取的验证码："+numberCode);
        //调用第三方验证码

        //存入redis 1、生成key 2、存入redis
        String driverkey = RedisPrefixUtils.generatorKeyByPhone(driverPhone, IdentityConstants.DRIVER_IDENTITY);
        stringRedisTemplate.opsForValue().set(driverkey,numberCode+"",2, TimeUnit.MINUTES);


        return ResponseResult.success("");
    }
}
