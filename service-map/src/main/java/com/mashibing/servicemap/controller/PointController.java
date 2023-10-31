package com.mashibing.servicemap.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.PointsDTO;
import com.mashibing.servicemap.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/points")
public class PointController {

    @Autowired
    private PointsService pointsService;
    @PostMapping("/upload")
    public ResponseResult upload(@RequestBody PointsDTO pointsDTO){

        return pointsService.upload(pointsDTO);
    }
}
