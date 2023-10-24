package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.Response.DriectionResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.remote.MapDriectionClinet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriectionService {

    @Autowired
    private MapDriectionClinet mapDriectionClinet;
    public ResponseResult driving(String depLongitude,String deplatitude,String destLongitude,String destlatitude){

        //调用第三方服务接口
        mapDriectionClinet.driection(depLongitude,deplatitude,destLongitude,destlatitude);

        DriectionResponse driectionResponse = new DriectionResponse();
        driectionResponse.setDistance(123);
        driectionResponse.setDuration(20);
        return ResponseResult.success(driectionResponse);
    }
}
