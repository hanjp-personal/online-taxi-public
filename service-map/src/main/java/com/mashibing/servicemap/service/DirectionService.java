package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.Response.DirectionResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.remote.MapDirectionClinet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectionService {

    @Autowired
    private MapDirectionClinet mapDriectionClinet;
    public ResponseResult driving(String depLongitude,String deplatitude,String destLongitude,String destlatitude){

        //调用第三方服务接口
        DirectionResponse directionResponse = mapDriectionClinet.direction(depLongitude, deplatitude, destLongitude, destlatitude);
        return ResponseResult.success(directionResponse);
    }
}
