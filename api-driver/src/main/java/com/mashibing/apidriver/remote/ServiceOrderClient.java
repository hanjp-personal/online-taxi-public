package com.mashibing.apidriver.remote;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-order")
public interface ServiceOrderClient {

    @RequestMapping(method = RequestMethod.POST,value = "/order/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/driver-arrived-departure")
    public ResponseResult driverArrivedDeparture(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest);

    @RequestMapping(method = RequestMethod.POST,value = "/order/passenger-get-off")
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest);
    @RequestMapping(method = RequestMethod.POST,value = "/order/cancel")
    public ResponseResult cancel(@RequestParam Long orderId, @RequestParam String identity);



}
