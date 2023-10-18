package com.mashibing.apipassenger.service;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

    public String generatorCode(String passengerPhone){
        //调用验证码服务，获取验证码
        System.out.println("调用验证码服务，获取验证码");
        String code = "111111";

        //存入redis
        System.out.println("存入redis");

        //返回值

        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("message","success");

        return result.toString();
    }
}
