package com.mashibing.serviceorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.Response.OrderResponse;
import com.mashibing.internalcommon.Response.TerminalResponse;
import com.mashibing.internalcommon.Response.TrsearchResponse;
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
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    /**
     * 新建订单
     * @param orderRequest
     * @return
     */
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

        for (int i = 0; i < 6; i++) {
            //创建成功后进行实时派单
            int orderResult = dispatchOrder(orderInfo);
            if (orderResult == 1){
                break;
            }
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseResult.success(orderInfo);
    }

    /**
     * 是否是黑名单
     * @param orderRequest
     * @return
     */
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

    /**
     * 计价规则是否存在
     * @param orderRequest
     * @return
     */
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

    /**
     * 实时派单逻辑
     * @param orderInfo
     */
    public int dispatchOrder(OrderInfo orderInfo){
        log.info("循环一次");
        int result = 0;
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
            //为了测试循环找车辆业务功能
//            List<TerminalResponse> data = new ArrayList<>();
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
                    String vehicleTypeFromCar = orderResponse.getVehicleType();

                    String vehicleType = orderInfo.getVehicleType();

                    if (vehicleType.trim().equals(vehicleTypeFromCar.trim())){
                        log.info("车型不符合！");
                        continue ;
                    }

                    //创建Redisson锁
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
                    result = 1;
                    lock.unlock();
                    break radius;
                }
            }

        }
        return result;
    }

    /**
     * 去接乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult toPickUpPassenger(OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        String toPickUpPassengerLongitude = orderRequest.getToPickUpPassengerLongitude();
        String toPickUpPassengerLatitude = orderRequest.getToPickUpPassengerLatitude();
        String toPickUpPassengerAddress = orderRequest.getToPickUpPassengerAddress();
        //根据订单ID查询订单数据
        QueryWrapper<OrderInfo> orderInfoQueryWrapper = new QueryWrapper();
        orderInfoQueryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(orderInfoQueryWrapper);
        orderInfo.setToPickUpPassengerLongitude(toPickUpPassengerLongitude);
        orderInfo.setToPickUpPassengerLatitude(toPickUpPassengerLatitude);
        orderInfo.setToPickUpPassengerAddress(toPickUpPassengerAddress);
        orderInfo.setToPickUpPassengerTime(LocalDateTime.now());
        //更改订单状态
        orderInfo.setOrderStatus(OrderConstants.DRIVER_TO_PICK_UP_PASSENGER);
        //更新数据库
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");


    }

    /**
     * 司机到达乘客上车点
     * @param orderRequest
     * @return
     */
    public ResponseResult driverArrivedDeparture(OrderRequest orderRequest){

        Long orderId = orderRequest.getOrderId();
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setDriverArrivedDepartureTime(LocalDateTime.now());
        orderInfo.setOrderStatus(OrderConstants.DRIVER_ARRIVED_DEPARTURE);

        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");
    }

    /**
     * 司机接到乘客
     * @param orderRequest
     * @return
     */
    public ResponseResult pickUpPassenger(OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        String pickUpPassengerLongitude = orderRequest.getPickUpPassengerLongitude();
        String pickUpPassengerLatitude = orderRequest.getPickUpPassengerLatitude();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectOne(queryWrapper);
        orderInfo.setPickUpPassengerLongitude(pickUpPassengerLongitude);
        orderInfo.setPickUpPassengerLatitude(pickUpPassengerLatitude);
        orderInfo.setPickUpPassengerTime(LocalDateTime.now());

        orderInfo.setOrderStatus(OrderConstants.PICK_UP_PASSENGER);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success("");
    }

    /**
     * 司机到达目的地，乘客下车
     * @param orderRequest
     * @return
     */
    public ResponseResult passengerGetoff(@RequestBody OrderRequest orderRequest){
        Long orderId = orderRequest.getOrderId();
        String passengerGetoffLongitude = orderRequest.getPassengerGetoffLongitude();
        String passengerGetoffLatitude = orderRequest.getPassengerGetoffLatitude();

        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",orderId);
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfo.setPassengerGetoffLongitude(passengerGetoffLongitude);
        orderInfo.setPassengerGetoffLatitude(passengerGetoffLatitude);

        orderInfo.setPassengerGetoffTime(LocalDateTime.now());
        orderInfo.setOrderStatus(OrderConstants.PASSENGER_GETOFF);

        //从地图中获取行驶里程和时间
        ResponseResult<Car> car = serviceCityDriverClient.getCarById(orderInfo.getCarId());
        ResponseResult<TrsearchResponse> trsearch = serviceMapClient.trsearch(car.getData().getTid(), orderInfo.getPickUpPassengerTime().toInstant(ZoneOffset.of("+8")).toEpochMilli(), LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        TrsearchResponse data = trsearch.getData();
        Long driverMile = data.getDriverMile();
        Long driverTime = data.getDriverTime();
        orderInfo.setDriverMile(driverMile);
        orderInfo.setDriverTime(driverTime);
        String cityCode = orderInfo.getAddress();
        String vehicleType = orderInfo.getVehicleType();
        ResponseResult<Double> responseResult = servicePriceClient.calculatePrice(driverMile.intValue(), driverTime.intValue(), cityCode, vehicleType);
        Double price = responseResult.getData();
        orderInfo.setPrice(price);
        orderInfoMapper.updateById(orderInfo);
        return ResponseResult.success(price);
    }
 }
