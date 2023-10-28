package com.mashibing.servicedriveruser.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mashibing.internalcommon.constant.CommonStatusEnum;
import com.mashibing.internalcommon.constant.DriverCarConstants;
import com.mashibing.internalcommon.dto.DriverCarBindingRelationship;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.mapper.DriverCarBindingRelationshipMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class DriverCarBindingRelationshipService {

    @Autowired
    private DriverCarBindingRelationshipMapper driverCarBindingRelationshipMapper;

    public ResponseResult bind(DriverCarBindingRelationship driverCarBindingRelationship){
        //判断，车辆和司机绑定后不可重复绑定
        QueryWrapper<DriverCarBindingRelationship> wrapper = new QueryWrapper<>();
        wrapper.eq("driver_id",driverCarBindingRelationship.getDriverId());
        wrapper.eq("car_id",driverCarBindingRelationship.getCarId());
        wrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);

        Integer integer = driverCarBindingRelationshipMapper.selectCount(wrapper);
        if (integer.intValue() > 0){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_CAR_BIND_EXIST.getCode(),CommonStatusEnum.DRIVER_CAR_BIND_EXIST.getValue());
        }
        //司机被绑定了
        wrapper = new QueryWrapper<>();
        wrapper.eq("driver_id",driverCarBindingRelationship.getDriverId());
        wrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);

        integer = driverCarBindingRelationshipMapper.selectCount(wrapper);
        if (integer.intValue() > 0){
            return ResponseResult.fail(CommonStatusEnum.DRIVER_BIND_EXIST.getCode(),CommonStatusEnum.DRIVER_BIND_EXIST.getValue());
        }
        //车辆被绑定了
        wrapper = new QueryWrapper<>();
        wrapper.eq("car_id",driverCarBindingRelationship.getCarId());
        wrapper.eq("bind_state",DriverCarConstants.DRIVER_CAR_BIND);

        integer = driverCarBindingRelationshipMapper.selectCount(wrapper);
        if (integer.intValue() > 0){
            return ResponseResult.fail(CommonStatusEnum.CAR_BIND_EXIST.getCode(),CommonStatusEnum.CAR_BIND_EXIST.getValue());
        }

        LocalDateTime now = LocalDateTime.now();
        driverCarBindingRelationship.setBindingTime(now);
        driverCarBindingRelationship.setBindState(DriverCarConstants.DRIVER_CAR_BIND);
        driverCarBindingRelationshipMapper.insert(driverCarBindingRelationship);
        return ResponseResult.success("");
    }
    public ResponseResult unbind(DriverCarBindingRelationship driverCarBindingRelationship){
        return ResponseResult.success("");

    }
}
