package com.mashibing.serviceorder.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {

    public ResponseResult add(OrderRequest orderRequest){
        log.info("service-order:"+orderRequest.getAddress());
        return null;
    }
}
