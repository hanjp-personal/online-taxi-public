package com.mashibing.servicedriveruser.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.mapper.DriverUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityDriverUserService {

    @Autowired
    private DriverUserMapper driverUserMapper;

    public ResponseResult<Boolean> isAvailableDriver(String cityCode){
        int result = driverUserMapper.selectCityDriverUserCountByCityCode(cityCode);
        if (result > 0){
            return ResponseResult.success(true);
        }else {
            return ResponseResult.success(false);
        }


    }
}
