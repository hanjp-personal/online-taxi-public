package com.mashibing.servicemap.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import com.mashibing.servicemap.service.DirectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direction")
public class DirectionController {

    @Autowired
    private DirectionService directionService;

    @GetMapping("/driving")
    public ResponseResult driving(@RequestBody ForeCastPriceDTO foreCastPriceDTO){

        String depLongitude = foreCastPriceDTO.getDepLongitude();
        String deplatitude = foreCastPriceDTO.getDeplatitude();
        String destLongitude = foreCastPriceDTO.getDestLongitude();
        String destlatitude = foreCastPriceDTO.getDestlatitude();
        return directionService.driving(depLongitude,deplatitude,destLongitude,destlatitude);

    }
}
