package com.mashibing.serviceorder.remote;

import com.mashibing.internalcommon.Response.OrderResponse;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-driver-user")
public interface ServiceCityDriverClient {

    @RequestMapping(method = RequestMethod.GET, value = "/city-driver/is-available-user")
    public ResponseResult<Boolean> isAvialable(@RequestParam String cityCode);
    @RequestMapping(method = RequestMethod.GET,value = "/get-available-driver/{carId}")
    public ResponseResult<OrderResponse> getAvailableDriver(@PathVariable("carId") Long carId);
}
