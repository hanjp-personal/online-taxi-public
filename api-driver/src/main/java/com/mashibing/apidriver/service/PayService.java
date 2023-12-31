package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceSsePushClient;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayService {

    @Autowired
    private ServiceSsePushClient serviceSsePushClient;

    public ResponseResult pushPayInfo(String orderId,String price,Long passengerId){
        //封装消息
        JSONObject message = new JSONObject();
        message.put("price",price);
        message.put("orderId",orderId);

        //推送消息
        serviceSsePushClient.push(passengerId, IdentityConstants.PASSENGER_IDENTITY,message.toString());

        return ResponseResult.success("");
    }
}
