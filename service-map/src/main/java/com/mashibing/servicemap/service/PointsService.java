package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.PointsRequest;
import com.mashibing.servicemap.remote.PointsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointsService {

    @Autowired
    private PointsClient pointsClient;

    public ResponseResult upload(PointsRequest pointsRequest){
        return pointsClient.upload(pointsRequest);

    }
}
