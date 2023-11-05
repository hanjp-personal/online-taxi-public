package com.mashibing.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.OrderConstants;
import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import com.mashibing.internalcommon.util.RedisPrefixUtils;
import com.mashibing.serviceorder.mapper.OrderInfoMapper;
import com.mashibing.serviceorder.remote.ServicePriceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ServicePriceClient servicePriceClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ResponseResult add(OrderRequest orderRequest){
        //判断计价规则是否是最新版本
        ResponseResult<Boolean> result = servicePriceClient.isNewPriceRule(orderRequest.getFareType(), orderRequest.getFareVersion());
        if (!(result.getData())){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGE.getCode(),CommonStatusEnum.PRICE_RULE_CHANGE.getValue());
        }
        //判断设备是否为黑名单设备
        String deviceCodeKey = RedisPrefixUtils.BlackDeviceCodePrefix + orderRequest.getDeviceCode();
        if (isBalckDevice(deviceCodeKey)) {
            return ResponseResult.fail(CommonStatusEnum.BLACKDEVICE.getCode(), CommonStatusEnum.BLACKDEVICE.getValue());
        }


        //判断是否有正在进行的订单，有则不允许再次下单
        if (isOrderGoningOn(orderRequest.getPassengerId()) > 0){
            return ResponseResult.fail(CommonStatusEnum.OREDER_ISGONG_ON.getCode(),CommonStatusEnum.OREDER_ISGONG_ON.getValue());
        }


        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderRequest,orderInfo);
        orderInfo.setOrderStatus(OrderConstants.ORDER_START);

        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);
        orderInfoMapper.insert(orderInfo);
        return ResponseResult.success(orderInfo);
    }

    private boolean isBalckDevice(String deviceCodeKey) {
        Boolean aBoolean = stringRedisTemplate.hasKey(deviceCodeKey);
        if (aBoolean){
            String deviceStr = stringRedisTemplate.opsForValue().get(deviceCodeKey);
            int count = Integer.parseInt(deviceStr);
            if (count >= 2){
                return true;
            }else {
                stringRedisTemplate.opsForValue().increment(deviceCodeKey);
            }
        }else {
            stringRedisTemplate.opsForValue().setIfAbsent(deviceCodeKey,"1",1, TimeUnit.HOURS);
        }
        return false;
    }

    public int isOrderGoningOn(Long passengerId){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("passenger_id",passengerId);
        queryWrapper.and(wrapper->wrapper.eq("order_status",OrderConstants.ORDER_START)
                .or().eq("order_status",OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status",OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status",OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status",OrderConstants.PICK_UP_PASSENGER)
                .or().eq("order_status",OrderConstants.PASSENGER_GETOFF)
                .or().eq("order_status",OrderConstants.TO_START_PAY));

        Integer result = orderInfoMapper.selectCount(queryWrapper);
        return result;
    }
 }
