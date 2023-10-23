package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    public ResponseResult refreshToken(String refreshTokenSrc){
        //解析refreshToken

        //从redis中读取refershToken

        //校验refershToken

        //生成双Token
        return null;
    }
}
