package com.mashibing.internalcommon.Response;

import lombok.Data;

@Data
public class TokenResponse {

    private String accessToken;
    
    private String refreshToken;
}
