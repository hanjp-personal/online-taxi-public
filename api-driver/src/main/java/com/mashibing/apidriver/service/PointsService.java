package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceDriverUserClient;
import com.mashibing.apidriver.remote.ServiceMapClient;
import com.mashibing.internalcommon.dto.Car;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ApiDriverPointsRequest;
import com.mashibing.internalcommon.request.PointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointsService {

    @Autowired
    private ServiceDriverUserClient serviceDriverUserClient;

    @Autowired
    private ServiceMapClient serviceMapClient;

    public ResponseResult upload(ApiDriverPointsRequest apiDriverPointsRequest){

        //获取carId
        Long carId = apiDriverPointsRequest.getCarId();

        //获取tid，trid
        ResponseResult<Car> car = serviceDriverUserClient.getCarById(carId);
        String tid = car.getData().getTid();
        String trid = car.getData().getTrid();

        //上传到地图
        PointsRequest pointsRequest = new PointsRequest();
        pointsRequest.setTid(tid);
        pointsRequest.setTrid(trid);
        pointsRequest.setPoints(apiDriverPointsRequest.getPoints());
        return serviceMapClient.upload(pointsRequest);

    }
}
