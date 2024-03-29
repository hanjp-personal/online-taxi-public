package com.mashibing.apipassenger.service;

import com.mashibing.apipassenger.remote.ServiceOrderClient;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private ServiceOrderClient serviceOrderClient;

    public ResponseResult add(OrderRequest orderRequest){
       return  serviceOrderClient.add(orderRequest);
    }
    public ResponseResult cancel(Long orderId){
        return serviceOrderClient.cancel(orderId, IdentityConstants.PASSENGER_IDENTITY);
    }

    public ResponseResult detail(Long orderId) {
        return serviceOrderClient.detail(orderId);
    }

    public ResponseResult<OrderInfo> current(String phone, String identity) {
        return serviceOrderClient.current(phone,identity);
    }
}
