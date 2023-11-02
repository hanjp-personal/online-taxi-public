package com.mashibing.serviceprice.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import com.mashibing.serviceprice.service.ForeCastPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForeCastPriceController {

    @Autowired
    private ForeCastPriceService foreCastPriceService;

    @PostMapping("/forecast-price")
    public ResponseResult forecastPrice(@RequestBody ForeCastPriceDTO foreCastPriceDTO){
        String depLongitude = foreCastPriceDTO.getDepLongitude();
        String deplatitude = foreCastPriceDTO.getDeplatitude();
        String destLongitude = foreCastPriceDTO.getDestLongitude();
        String destlatitude = foreCastPriceDTO.getDestlatitude();
        String cityCode = foreCastPriceDTO.getCityCode();
        String vehicleType = foreCastPriceDTO.getVehicleType();
        return foreCastPriceService.forecasrPrice(depLongitude,deplatitude,destLongitude,destlatitude,cityCode,vehicleType);
    }
}
