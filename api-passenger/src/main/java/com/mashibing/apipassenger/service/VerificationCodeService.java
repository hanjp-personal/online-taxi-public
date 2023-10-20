package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServiceVerificationcodeClient;
import com.mashibing.internalcommon.Response.NumberCodeResponse;
import com.mashibing.internalcommon.Response.TokenResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class VerificationCodeService {
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String verificationCodeprefix = "passenger-verification-code-";

    /**
     * 生成key
     * @param passengerPhone
     * @return
     */
    public String generatorKeyByPhone(String passengerPhone){
        return verificationCodeprefix + passengerPhone;
    }
    /**
     * 生成验证码
     * @param passengerPhone
     * @return
     */
    public ResponseResult generatorCode(String passengerPhone){
        //调用验证码服务，获取验证码
        System.out.println("调用验证码服务，获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();
        System.out.println("remote numbercode " + numberCode);

        //存入redis key value ttl
        String key = generatorKeyByPhone(passengerPhone);
        stringRedisTemplate.opsForValue().set(key,numberCode+"",2, TimeUnit.MINUTES);
        return ResponseResult.success("");
    }

    /**
     * 校验验证码
     * @param passengerPhone
     * @param verificationCode
     * @return
     */
    public ResponseResult checkCode(String passengerPhone, String verificationCode){
        //根据手机号，去redis读取验证码
        //生成key
        String key = generatorKeyByPhone(passengerPhone);
        //根据key获取redis中的value
        String codeRedis = stringRedisTemplate.opsForValue().get(key);
        System.out.println("获取的Redis中的code：" + codeRedis);

        //校验验证码
        System.out.println("校验验证码");

        //判断原来是有用户，并进行对应的处理
        System.out.println("判断原来是有用户，并进行对应的处理");

        //颁发令牌
        System.out.println("颁发令牌");

        //响应
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setToken("token value");
        return ResponseResult.success(tokenResponse);
    }


}
