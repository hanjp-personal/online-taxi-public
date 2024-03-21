package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceOrderClient;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiDriverOrderInfoService {

    @Autowired
    private ServiceOrderClient serviceOrderClient;

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest){
        return serviceOrderClient.toPickUpPassenger(orderRequest);
    }
    /**
     * 司机到达乘客上车点
     * @param orderRequest
     * @return
     */
    public ResponseResult driverArrivedDeparture(OrderRequest orderRequest){

        return serviceOrderClient.driverArrivedDeparture(orderRequest);
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger(OrderRequest orderRequest){

        return serviceOrderClient.pickUpPassenger(orderRequest);
    }
    /**
     * 司机到达目的地，乘客下车
     * @param orderRequest
     * @return
     */
    public ResponseResult passengerGetoff(OrderRequest orderRequest){
        return serviceOrderClient.passengerGetoff(orderRequest);
    }
    /**
     * 司机取消订单
     * @param orderId
     * @return
     */
    public ResponseResult cancel(Long orderId){
        return serviceOrderClient.cancel(orderId, IdentityConstants.DRIVER_IDENTITY);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    public ResponseResult detail(Long orderId) {
        return serviceOrderClient.detail(orderId);
    }

    /**
     * 查询正在进行的订单
     * @param phone
     * @param identity
     * @return
     */
    public ResponseResult<OrderInfo> current(String phone, String identity) {
        return serviceOrderClient.current(phone,identity);
    }
}
