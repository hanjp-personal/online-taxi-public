package com.mashibing.serviceorder.service;

import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import com.mashibing.serviceorder.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    public ResponseResult add(OrderRequest orderRequest){
        log.info("service-order:"+orderRequest.getAddress());
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setAddress("110000");
        orderInfoMapper.insert(orderInfo);
        return null;
    }
}
