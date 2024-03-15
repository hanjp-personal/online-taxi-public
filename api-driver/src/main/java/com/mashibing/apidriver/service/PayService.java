package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceOrderClient;
import com.mashibing.apidriver.remote.ServiceSsePushClient;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {

    @Autowired
    private ServiceSsePushClient serviceSsePushClient;

    @Autowired
    private ServiceOrderClient serviceOrderClient;

    public ResponseResult pushPayInfo(Long orderId,String price,Long passengerId){
        //封装消息
        JSONObject message = new JSONObject();
        message.put("price",price);
        message.put("orderId",orderId);
        //修改订单状态
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId(orderId);
        serviceOrderClient.updatePayInfo(orderRequest);
        //推送消息
        serviceSsePushClient.push(passengerId, IdentityConstants.PASSENGER_IDENTITY,message.toString());

        return ResponseResult.success("");
    }
}
