package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServicePriceClient;
import com.mashibing.internalcommon.Response.ForeCastPriceResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForeCastPriceService {

    @Autowired
    private ServicePriceClient servicePriceClient;

    public ResponseResult forecasrPrice(String depLongitude, String deplatitude,String destLongitude,String destlatitude){
        log.info("出发地经度： "+ depLongitude);
        log.info("出发地纬度： "+ deplatitude);
        log.info("目的地经度： "+ destLongitude);
        log.info("目的地纬度： "+ destlatitude);

        log.info("调用计价服务，计算预估价格");
        ForeCastPriceDTO foreCastPriceDTO = new ForeCastPriceDTO();
        foreCastPriceDTO.setDepLongitude(depLongitude);
        foreCastPriceDTO.setDeplatitude(deplatitude);
        foreCastPriceDTO.setDestLongitude(destLongitude);
        foreCastPriceDTO.setDestlatitude(destlatitude);
        ResponseResult<ForeCastPriceResponse> forecast = servicePriceClient.forecast(foreCastPriceDTO);
        double price = forecast.getData().getPrice();

        ForeCastPriceResponse foreCastPriceResponse = new ForeCastPriceResponse();
        foreCastPriceResponse.setPrice(price);
        return ResponseResult.success(foreCastPriceResponse);



    }
}
