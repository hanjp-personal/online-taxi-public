package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.service.ForeCastPriceService;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForeCastPriceController {

    @Autowired
    private ForeCastPriceService foreCastPriceService;

    @PostMapping("/forecast-price")
    public ResponseResult ForecastPrice(@RequestBody ForeCastPriceDTO foreCastPriceDTO){
        String depLongitude = foreCastPriceDTO.getDepLongitude();
        String deplatitude = foreCastPriceDTO.getDeplatitude();
        String destLongitude = foreCastPriceDTO.getDestLongitude();
        String destlatitude = foreCastPriceDTO.getDestlatitude();
        return foreCastPriceService.forecasrPrice(depLongitude,deplatitude,destLongitude,destlatitude);
    }
}
