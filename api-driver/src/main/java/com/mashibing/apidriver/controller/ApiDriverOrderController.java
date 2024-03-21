package com.mashibing.apidriver.controller;

import com.mashibing.apidriver.service.ApiDriverOrderInfoService;
import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class ApiDriverOrderController {

    @Autowired
    private ApiDriverOrderInfoService apiDriverOrderInfoService;
    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/to-pick-up-passenger")
    public ResponseResult toPickUpPassenger(@RequestBody OrderRequest orderRequest){
        return apiDriverOrderInfoService.toPickUpPassenger(orderRequest);
    }

    /**
     * 司机到达乘客上车点
     * @param orderRequest
     * @return
     */
    @PostMapping("/driver-arrived-departure")
    public ResponseResult driverArrivedDeparture(@RequestBody OrderRequest orderRequest){
        return apiDriverOrderInfoService.driverArrivedDeparture(orderRequest);

    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    @PostMapping("/pick-up-passenger")
    public ResponseResult pickUpPassenger(@RequestBody OrderRequest orderRequest){
        return apiDriverOrderInfoService.pickUpPassenger(orderRequest);
    }

    /**
     * 司机到达目的地，乘客下车
     * @param orderRequest
     * @return
     */
    @PostMapping("/passenger-get-off")
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest){
        return apiDriverOrderInfoService.passengerGetoff(orderRequest);
    }

    /**
     * 司机取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    public ResponseResult cancel(@RequestParam Long orderId){
        return apiDriverOrderInfoService.cancel(orderId);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ResponseResult detail(@RequestParam Long orderId){
        return apiDriverOrderInfoService.detail(orderId);
    }
    @GetMapping("/current")
    public ResponseResult<OrderInfo> current(@RequestParam String phone, @RequestParam String identity){
        return apiDriverOrderInfoService.current(phone,identity);
    }
}
