package com.mashibing.apidriver.controller;

import com.mashibing.apidriver.service.PointsService;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ApiDriverPointsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointsControler {

    @Autowired
    private PointsService pointsService;


    @PostMapping("/upload")
    public ResponseResult upload(@RequestBody ApiDriverPointsRequest apiDriverPointsRequest){
        return pointsService.upload(apiDriverPointsRequest);
    }
}
