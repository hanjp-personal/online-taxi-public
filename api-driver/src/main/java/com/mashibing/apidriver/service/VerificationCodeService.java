package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceDriverUserClient;
import com.mashibing.apidriver.remote.ServiceVerificationCodeClient;
import com.mashibing.internalcommon.Response.DriverUserResponse;
import com.mashibing.internalcommon.Response.NumberCodeResponse;
import com.mashibing.internalcommon.Response.TokenResponse;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.DriverCarConstants;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.constant.TokenConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.util.JwtUtils;
import com.mashibing.internalcommon.util.RedisPrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeService {

    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    private ServiceVerificationCodeClient serviceVerificationCodeClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult checkAndsSendVerificationCode(String driverPhone){
        //查询手机号是否存在
        ResponseResult<DriverUserResponse> driverUserResponseResponseResult = serviceDriverUserClient.checkDriverUser(driverPhone);
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

    public ResponseResult checkCode(String driverPhone, String verificationCode){
        //根据手机号，去redis读取验证码
        //生成key
        String key = RedisPrefixUtils.generatorKeyByPhone(driverPhone,IdentityConstants.DRIVER_IDENTITY);
        //根据key获取redis中的value
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("获取的Redis中的code：" + codeRedis);

        //校验验证码
        if(StringUtils.isBlank(codeRedis)){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }
        if(!verificationCode.trim().equals(codeRedis.trim())){
            return ResponseResult.fail(CommonStatusEnum.VERIFICATION_CODE_ERROR.getCode(),CommonStatusEnum.VERIFICATION_CODE_ERROR.getValue());
        }

        //颁发令牌
        System.out.println("颁发令牌");
        String accesstoken = JwtUtils.generatorToken(driverPhone, IdentityConstants.DRIVER_IDENTITY, TokenConstants.ACCESS_TOKEN_TYPE);
        String refreshtoken = JwtUtils.generatorToken(driverPhone, IdentityConstants.DRIVER_IDENTITY, TokenConstants.REFRESH_TOKEN_TYPE);

        String accesstokenKey = RedisPrefixUtils.generatorTokenKey(driverPhone, IdentityConstants.DRIVER_IDENTITY,TokenConstants.ACCESS_TOKEN_TYPE);
        String refreshtokenKey = RedisPrefixUtils.generatorTokenKey(driverPhone, IdentityConstants.DRIVER_IDENTITY,TokenConstants.REFRESH_TOKEN_TYPE);

        stringRedisTemplate.opsForValue().set(accesstokenKey,accesstoken,30,TimeUnit.DAYS);
        stringRedisTemplate.opsForValue().set(refreshtokenKey,refreshtoken,31,TimeUnit.DAYS);

        //响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(accesstoken);
        tokenResponse.setRefreshToken(refreshtoken);
        return ResponseResult.success(tokenResponse);
    }
}
