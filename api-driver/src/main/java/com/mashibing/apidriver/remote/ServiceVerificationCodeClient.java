package com.mashibing.apidriver.remote;

import com.mashibing.internalcommon.Response.NumberCodeResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("service-verificationcode")
public interface ServiceVerificationCodeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/numberCode/{size}")
    public ResponseResult<NumberCodeResponse> getNumberCode(@PathVariable("size") int size);
}
