package com.mashibing.apipassenger.controller;

import com.mashibing.apipassenger.service.OrderService;
import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 乘客下单
     * @param orderRequest
     * @return
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderRequest orderRequest){
         return orderService.add(orderRequest);
    }

    /**
     * 乘客取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    public ResponseResult cancel(@RequestParam Long orderId){
        return orderService.cancel(orderId);
    }

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/detail")
    public ResponseResult detail(@RequestParam Long orderId){
        return orderService.detail(orderId);
    }
    /**
     * 查询正在进行的订单
     * @param phone
     * @param identity
     * @return
     */
    @GetMapping("/current")
    public ResponseResult<OrderInfo> current(@RequestParam String phone, @RequestParam String identity) {
        return orderService.current(phone,identity);
    }
}
