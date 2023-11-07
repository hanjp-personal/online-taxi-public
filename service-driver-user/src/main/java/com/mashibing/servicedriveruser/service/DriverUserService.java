package com.mashibing.servicedriveruser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.Response.OrderResponse;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.DriverCarConstants;
import com.mashibing.internalcommon.dto.*;
import com.mashibing.servicedriveruser.mapper.CarMapper;
import com.mashibing.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import com.mashibing.servicedriveruser.mapper.DriverUserMapper;
import com.mashibing.servicedriveruser.mapper.DriverUserWorkStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DriverUserService {

    @Autowired
    private DriverUserMapper driverUserMapper;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private DriverUserWorkStatusMapper driverUserWorkStatusMapper;

    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult testDriverUser(){

        DriverUser driverUser = driverUserMapper.selectById(1);
        return ResponseResult.success(driverUser);

    }

    /**
     * 新增司机信息
     * @param driverUser
     * @return
     */
    public ResponseResult addDriverUser(DriverUser driverUser){

        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtCreate(now);
        driverUser.setGmtModified(now);
        driverUserMapper.insert(driverUser);

        DriverUserWorkStatus driverUserWorkStatus = new DriverUserWorkStatus();
        driverUserWorkStatus.setDriverId(driverUser.getId());
        driverUserWorkStatus.setWorkStatus(DriverCarConstants.DRIVER_WORK_STATUS_STOP);
        driverUserWorkStatus.setGmtCreate(now);
        driverUserWorkStatus.setGmtModified(now);

        driverUserWorkStatusMapper.insert(driverUserWorkStatus);
        return ResponseResult.success("");
    }

    /**
     * 修改司机信息
     * @param driverUser
     * @return
     */
    public ResponseResult updateDriverUser(DriverUser driverUser){

        LocalDateTime now = LocalDateTime.now();
        driverUser.setGmtModified(now);
        driverUserMapper.updateById(driverUser);
        return ResponseResult.success("");
    }

    public ResponseResult<DriverUser> getDriverUserByPhone(String driverPhone){
        Map<String,Object> map = new HashMap<>();
        map.put("driver_phone",driverPhone);
        map.put("state", DriverCarConstants.DRIVER_VILID);
        List<DriverUser> driverUsers = driverUserMapper.selectByMap(map);
        if (driverUsers.isEmpty()){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_NOT_EXIST.getCode(),CommonStatusEnum.DRIVER_NOT_EXIST.getValue());
        }
        DriverUser driverUser = driverUsers.get(0);
        return ResponseResult.success(driverUser);
    }

    public ResponseResult<OrderResponse> getAvailableDriver(Long carId){
        //根据司机和车辆绑定关系查询
        QueryWrapper<DriverCarBindingRelationship> driverCarBindingRelationshipqueryWrapper = new QueryWrapper<>();
        driverCarBindingRelationshipqueryWrapper.eq("car_id",carId);
        driverCarBindingRelationshipqueryWrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);
        DriverCarBindingRelationship driverCarBindingRelationship = driverCarBindingRelationshipMapper.selectOne(driverCarBindingRelationshipqueryWrapper);
        if(null == driverCarBindingRelationship){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_BIND_NOT_EXIST.getCode(),CommonStatusEnum.DRIVER_CAR_BIND_NOT_EXIST.getValue());
        }else {
            Long driverId = driverCarBindingRelationship.getDriverId();
            //根据司机工作状态查询
            QueryWrapper<DriverUserWorkStatus> driverUserWorkStatusqueryWrapper = new QueryWrapper<>();
            driverUserWorkStatusqueryWrapper.eq("driver_id",driverId);
            driverUserWorkStatusqueryWrapper.eq("work_status",DriverCarConstants.DRIVER_WORK_STATUS_START);
            DriverUserWorkStatus driverUserWorkStatus = driverUserWorkStatusMapper.selectOne(driverUserWorkStatusqueryWrapper);

            if (null == driverUserWorkStatus){
                return ResponseResult.fail(CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getCode(),CommonStatusEnum.AVAILABLE_DRIVER_EMPTY.getValue());
            }else {
                //根据司机ID查询司机信息
                QueryWrapper<DriverUser> driverUserqueryWrapper = new QueryWrapper<>();
                driverUserqueryWrapper.eq("id",driverUserWorkStatus.getDriverId());
                DriverUser driverUser = driverUserMapper.selectOne(driverUserqueryWrapper);

                //根据车辆ID查询车辆信息
                QueryWrapper<Car> carQueryWrapper = new QueryWrapper<>();
                carQueryWrapper.eq("id",carId);
                Car car = carMapper.selectOne(carQueryWrapper);

                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setDriverId(driverId);
                orderResponse.setCarId(carId);
                orderResponse.setDriverPhone(driverUser.getDriverPhone());
                orderResponse.setLicenseId(driverUser.getLicenseId());
                orderResponse.setVehicleNo(car.getVehicleNo());
                return ResponseResult.success(orderResponse);
            }
        }

    }

}
