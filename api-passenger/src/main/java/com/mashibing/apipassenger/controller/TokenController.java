package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.service.TokenService;
import com.mashibing.internalcommon.Response.TokenResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;
    @PostMapping("/Token-refresh")
    public ResponseResult refreshToken(@RequestBody TokenResponse tokenResponse){
        String refreshTokenSrc = tokenResponse.getRefreshToken();
        System.out.println("原来的refreshToken： "+ refreshTokenSrc);
        return tokenService.refreshToken(refreshTokenSrc);
    }
}
