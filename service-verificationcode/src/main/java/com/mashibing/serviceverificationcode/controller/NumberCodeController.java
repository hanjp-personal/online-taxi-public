package com.mashibing.serviceverificationcode.controller;

import com.mashibing.internalcommon.Response.NumberCodeResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NumberCodeController {
    @GetMapping("/numberCode/{size}")
    public ResponseResult numberCode(@PathVariable("size") int size){

        System.out.println("size:"+ size);
        //生成验证码
        double mathrandom = (Math.random() * 9 + 1) * Math.pow(10,size-1);
        int resultInt = (int)mathrandom;

        NumberCodeResponse numberCodeResponse = new NumberCodeResponse();
        numberCodeResponse.setNumberCode(resultInt);
        return ResponseResult.success(numberCodeResponse);
    }
}
