package com.mashibing.serviceprice.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import com.mashibing.serviceprice.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PriceController {

    @Autowired
    private PriceService priceService;

    @PostMapping("/forecast-price")
    public ResponseResult forecastPrice(@RequestBody ForeCastPriceDTO foreCastPriceDTO){
        String depLongitude = foreCastPriceDTO.getDepLongitude();
        String deplatitude = foreCastPriceDTO.getDeplatitude();
        String destLongitude = foreCastPriceDTO.getDestLongitude();
        String destlatitude = foreCastPriceDTO.getDestlatitude();
        String cityCode = foreCastPriceDTO.getCityCode();
        String vehicleType = foreCastPriceDTO.getVehicleType();
        return priceService.forecasrPrice(depLongitude,deplatitude,destLongitude,destlatitude,cityCode,vehicleType);
    }
    @PostMapping("/calculate-price")
    public ResponseResult calculatePrice(@RequestParam Integer distance, @RequestParam Integer duration, @RequestParam String cityCode,@RequestParam String vehicleType){
        return priceService.calculatePrice(distance,duration,cityCode,vehicleType);
    }


}
