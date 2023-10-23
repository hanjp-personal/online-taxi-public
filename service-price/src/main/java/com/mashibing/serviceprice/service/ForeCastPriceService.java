package com.mashibing.serviceprice.service;

import com.mashibing.internalcommon.Response.ForeCastPriceResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForeCastPriceService {

    public ResponseResult forecasrPrice(String depLongitude, String deplatitude,String destLongitude,String destlatitude){
        log.info("出发地经度： "+ depLongitude);
        log.info("出发地纬度： "+ deplatitude);
        log.info("目的地经度： "+ destLongitude);
        log.info("目的地纬度： "+ destlatitude);

        log.info("调用地图服务，查询距离和时长");

        log.info("读取计价规则");

        log.info("根据距离、时长、计价规则，计算预估价格");

        ForeCastPriceResponse foreCastPriceResponse = new ForeCastPriceResponse();
        foreCastPriceResponse.setPrice(9.9);
        return ResponseResult.success(foreCastPriceResponse);
    }
}
