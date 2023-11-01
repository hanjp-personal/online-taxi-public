package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceUserClient;
import com.mashibing.internalcommon.dto.Car;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ApiDriverPointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointsService {

    @Autowired
    private ServiceUserClient serviceUserClient;

    public ResponseResult upload(ApiDriverPointsRequest apiDriverPointsRequest){

        //获取carId
        Long carId = apiDriverPointsRequest.getCarId();

        //获取tid，trid
        ResponseResult<Car> car = serviceUserClient.getCarById(carId);
        String tid = car.getData().getTid();
        String trid = car.getData().getTrid();

        //上传到地图
        return null;

    }
}
