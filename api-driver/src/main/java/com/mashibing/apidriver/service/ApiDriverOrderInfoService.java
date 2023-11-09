package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceOrderClient;
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

}
