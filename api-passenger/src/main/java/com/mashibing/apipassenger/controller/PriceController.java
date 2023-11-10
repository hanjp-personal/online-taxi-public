package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.service.PriceService;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PriceController {

    @Autowired
    private PriceService priceService;

    @PostMapping("/forecast-price")
    public ResponseResult<Double> ForecastPrice(@RequestBody ForeCastPriceDTO foreCastPriceDTO){
        String depLongitude = foreCastPriceDTO.getDepLongitude();
        String deplatitude = foreCastPriceDTO.getDeplatitude();
        String destLongitude = foreCastPriceDTO.getDestLongitude();
        String destlatitude = foreCastPriceDTO.getDestlatitude();
        String cityCode = foreCastPriceDTO.getCityCode();
        String vehicleType = foreCastPriceDTO.getVehicleType();
        return priceService.forecasrPrice(depLongitude,deplatitude,destLongitude,destlatitude,cityCode,vehicleType);
    }
}
