package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServiceVerificationcodeClient;
import com.mashibing.internalcommon.Response.NumberCodeResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {
    @Autowired
    private ServiceVerificationcodeClient serviceVerificationcodeClient;
    public String generatorCode(String passengerPhone){
        //调用验证码服务，获取验证码
        System.out.println("调用验证码服务，获取验证码");
        ResponseResult<NumberCodeResponse> numberCodeResponse = serviceVerificationcodeClient.getNumberCode(6);
        int numberCode = numberCodeResponse.getData().getNumberCode();

        System.out.println("remote numbercode " + numberCode);

        //存入redis
        System.out.println("存入redis");

        //返回值

        JSONObject result = new JSONObject();
        result.put("code",1);
        result.put("message","success");

        return result.toString();
    }
}
