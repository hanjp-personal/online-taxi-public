package com.mashibing.apipassenger.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test(){
        return "test api passenger";
    }

    /**
     * 需要token
     * @return
     */
    @GetMapping("/authtest")
    public ResponseResult authtest(){
        return ResponseResult.success("auth test");
    }

    /**
     * 不需要token
     * @return
     */
    @GetMapping("/noauthtest")
    public ResponseResult noauthtest(){
        return ResponseResult.success("noauth test");
    }
}
