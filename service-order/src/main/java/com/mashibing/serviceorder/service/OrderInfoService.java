package com.mashibing.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.Response.OrderResponse;
import com.mashibing.internalcommon.Response.TerminalResponse;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.IdentityConstants;
import com.mashibing.internalcommon.constant.OrderConstants;
import com.mashibing.internalcommon.dto.Car;
import com.mashibing.internalcommon.dto.OrderInfo;
import com.mashibing.internalcommon.dto.PriceRule;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.OrderRequest;
import com.mashibing.internalcommon.request.PriceRuleIsNewRequest;
import com.mashibing.internalcommon.util.RedisPrefixUtils;
import com.mashibing.serviceorder.mapper.OrderInfoMapper;
import com.mashibing.serviceorder.remote.ServiceCityDriverClient;
import com.mashibing.serviceorder.remote.ServiceMapClient;
import com.mashibing.serviceorder.remote.ServicePriceClient;
import com.mashibing.serviceorder.remote.ServiceSsePushClient;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderInfoService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ServicePriceClient servicePriceClient;

    @Autowired
    private ServiceCityDriverClient serviceCityDriverClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ServiceMapClient serviceMapClient;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ServiceSsePushClient serviceSsePushClient;

    public ResponseResult add(OrderRequest orderRequest){

        //根据城市代码查询当前城市是否有司机
        ResponseResult<Boolean> avialable = serviceCityDriverClient.isAvialable(orderRequest.getAddress());
        log.info("测试当前城市是否有司机："+avialable.getData());
        if(!avialable.getData()){
            return ResponseResult.fail(CommonStatusEnum.CITY_DRIVER_EMPTY.getCode(),CommonStatusEnum.CITY_DRIVER_EMPTY.getValue());
        }

        //判断计价规则是否是最新版本
        PriceRuleIsNewRequest priceRuleIsNewRequest = new PriceRuleIsNewRequest();
        priceRuleIsNewRequest.setFareType(orderRequest.getFareType());
        priceRuleIsNewRequest.setFareVersion(orderRequest.getFareVersion());
        ResponseResult<Boolean> result = servicePriceClient.isNewPriceRule(priceRuleIsNewRequest);
        if (!(result.getData())){
            return ResponseResult.fail(CommonStatusEnum.PRICE_RULE_CHANGE.getCode(),CommonStatusEnum.PRICE_RULE_CHANGE.getValue());
        }
        //判断设备是否为黑名单设备
        if (isBalckDevice(orderRequest)) {
            return ResponseResult.fail(CommonStatusEnum.BLACKDEVICE.getCode(), CommonStatusEnum.BLACKDEVICE.getValue());
        }

        //
        if (!priceRuleIsExist(orderRequest)){
            return ResponseResult.fail(CommonStatusEnum.CITY_SERVICE_NOT_SERVICE.getCode(),CommonStatusEnum.CITY_SERVICE_NOT_SERVICE.getValue());
        }


        //判断是否有正在进行的订单，有则不允许再次下单
        if (isPassengerOrderGoningOn(orderRequest.getPassengerId()) > 0){
            return ResponseResult.fail(CommonStatusEnum.OREDER_ISGONG_ON.getCode(),CommonStatusEnum.OREDER_ISGONG_ON.getValue());
        }


        OrderInfo orderInfo = new OrderInfo();
        BeanUtils.copyProperties(orderRequest,orderInfo);
        orderInfo.setOrderStatus(OrderConstants.ORDER_START);

        LocalDateTime now = LocalDateTime.now();
        orderInfo.setGmtCreate(now);
        orderInfo.setGmtModified(now);
        orderInfoMapper.insert(orderInfo);

        //创建成功后进行实时派单
        dispatchOrder(orderInfo);
        return ResponseResult.success(orderInfo);
    }

    private boolean isBalckDevice(OrderRequest orderRequest) {
        String deviceCodeKey = RedisPrefixUtils.BlackDeviceCodePrefix + orderRequest.getDeviceCode();
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

    /**
     * 业务判断，乘客正在进行的订单
     * @param passengerId
     * @return
     */
    public int isPassengerOrderGoningOn(Long passengerId){
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

    /**
     * 业务判断，司机正在进行的订单
     * @param driverId
     * @return
     */
    public int isDriverOrderGoningOn(Long driverId){
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("driver_id",driverId);
        queryWrapper.and(wrapper->wrapper.eq("order_status",OrderConstants.DRIVER_RECEIVE_ORDER)
                .or().eq("order_status",OrderConstants.DRIVER_TO_PICK_UP_PASSENGER)
                .or().eq("order_status",OrderConstants.DRIVER_ARRIVED_DEPARTURE)
                .or().eq("order_status",OrderConstants.PICK_UP_PASSENGER));
        Integer result = orderInfoMapper.selectCount(queryWrapper);
        log.info("司机ID："+ driverId +"正在进行的订单数量：" + result);
        return result;
    }

    public Boolean priceRuleIsExist(OrderRequest orderRequest){
        String fareType = orderRequest.getFareType();
        int index = fareType.indexOf("$");
        String cityCode = fareType.substring(0, index);
        String vehicleType = fareType.substring(index + 1);

        PriceRule priceRule = new PriceRule();
        priceRule.setCityCode(cityCode);
        priceRule.setVehicleType(vehicleType);

        ResponseResult<Boolean> booleanResponseResult = servicePriceClient.priceRuleIsExist(priceRule);
        return booleanResponseResult.getData();

    }

    public  synchronized void dispatchOrder(OrderInfo orderInfo){
        String depLongitude = orderInfo.getDepLongitude();
        String depLatitude = orderInfo.getDepLatitude();
        String center = depLatitude +","+ depLongitude;

        List<Integer> radiuslist = new ArrayList<>();
        radiuslist.add(2000);
        radiuslist.add(4000);
        radiuslist.add(5000);

        ResponseResult<List<TerminalResponse>> listResponseResult = null;
        radius:
        for (int i = 0; i < radiuslist.size(); i++) {
            Integer radius = radiuslist.get(i);
            listResponseResult = serviceMapClient.terminalAroundsearch(center, radius);

            log.info("在半径"+radius+"的范围里寻找车辆,:"+ JSONArray.fromObject(listResponseResult.getData()));

            //解析数据，获取车辆ID
            List<TerminalResponse> data = listResponseResult.getData();
            for (int j = 0; j < data.size(); j++) {

                TerminalResponse terminalResponse = data.get(j);
                long carId = terminalResponse.getCarId();
                String destlongitude = terminalResponse.getLongitude();
                String destlatitude = terminalResponse.getLatitude();
                //根据车辆ID，查询是否有可派用的司机
                ResponseResult<OrderResponse> availableDriver = serviceCityDriverClient.getAvailableDriver(carId);
                if (availableDriver.getCode() == CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode()){
                    log.info("没有车辆ID：" +carId+"对应的司机");
                    continue;
                }else {
                    log.info("找到了当前出车的司机，车辆ID为："+ carId);
                    OrderResponse orderResponse = availableDriver.getData();
                    Long driverId = orderResponse.getDriverId();
                    String driverPhone = orderResponse.getDriverPhone();
                    String licenseId = orderResponse.getLicenseId();
                    String vehicleNo = orderResponse.getVehicleNo();

                    String lockKey = (driverId+"").intern();
                    RLock lock = redissonClient.getLock(lockKey);
                    lock.lock();

                    //判断司机是否有正在进行的订单
                    if (isDriverOrderGoningOn(driverId) > 0){
                        //此处不进行处理，会造成死锁的问题
                        lock.unlock();
                        continue ;
                    }
                    //订单直接匹配司机
                    orderInfo.setDriverId(driverId);
                    orderInfo.setDriverPhone(driverPhone);
                    orderInfo.setCarId(carId);
                    //从地图中获取
                    orderInfo.setDestLongitude(destlongitude);
                    orderInfo.setDestLatitude(destlatitude);
                    orderInfo.setReceiveOrderCarLongitude(depLongitude);
                    orderInfo.setReceiveOrderCarLatitude(depLatitude);
                    orderInfo.setReceiveOrderTime(LocalDateTime.now());
                    //从司机信息中获取
                    orderInfo.setLicenseId(licenseId);
                    //从车辆信息中获取
                    orderInfo.setVehicleNo(vehicleNo);
                    orderInfo.setOrderStatus(OrderConstants.DRIVER_RECEIVE_ORDER);
                    //更新订单信息
                    orderInfoMapper.updateById(orderInfo);

                    //通知司机
                    JSONObject driverContent = new JSONObject();
                    driverContent.put("passengerId",orderInfo.getPassengerId());
                    driverContent.put("passengerPhone",orderInfo.getPassengerPhone());
                    driverContent.put("departure",orderInfo.getDeparture());
                    driverContent.put("depLongitude",orderInfo.getDepLongitude());
                    driverContent.put("depLatitude",orderInfo.getDepLatitude());
                    driverContent.put("destination",orderInfo.getDestination());
                    driverContent.put("destLongitude",orderInfo.getDestLongitude());
                    driverContent.put("destLatitude",orderInfo.getDestLatitude());

                    serviceSsePushClient.push(driverId, IdentityConstants.DRIVER_IDENTITY,driverContent.toString());

                    //通知乘客
                    JSONObject passengerContent = new JSONObject();
                    passengerContent.put("driverId",orderInfo.getDriverId());
                    passengerContent.put("driverPhone",orderInfo.getDriverPhone());
                    passengerContent.put("vehicleNo",orderInfo.getVehicleNo());
                    //车辆信息，根据车辆ID 查询车辆信息
                    ResponseResult<Car> carResponseResult = serviceCityDriverClient.getCarById(orderInfo.getCarId());
                    Car carRemote = carResponseResult.getData();
                    passengerContent.put("brand",carRemote.getBrand());
                    passengerContent.put("model",carRemote.getModel());
                    passengerContent.put("vehicleColor",carRemote.getVehicleColor());
                    passengerContent.put("receiveOrderCarLongitude",orderInfo.getReceiveOrderCarLongitude());
                    passengerContent.put("receiveOrderCarLatitude",orderInfo.getReceiveOrderCarLatitude());

                    serviceSsePushClient.push(orderInfo.getPassengerId(), IdentityConstants.PASSENGER_IDENTITY,passengerContent.toString());

                    lock.unlock();
                    break radius;
                }
            }

        }
    }
 }
