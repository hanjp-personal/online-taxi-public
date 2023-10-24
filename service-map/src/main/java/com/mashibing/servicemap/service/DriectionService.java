package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.Response.DriectionResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.stereotype.Service;

@Service
public class DriectionService {
    public ResponseResult driving(String depLongitude,String deplatitude,String destLongitude,String destlatitude){

        DriectionResponse driectionResponse = new DriectionResponse();
        driectionResponse.setDistance(123);
        driectionResponse.setDuration(20);
        return ResponseResult.success(driectionResponse);
    }
}
