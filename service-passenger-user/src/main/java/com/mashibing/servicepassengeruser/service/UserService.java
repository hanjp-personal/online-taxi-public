package com.mashibing.servicepassengeruser.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicepassengeruser.dto.PassengerUser;
import com.mashibing.servicepassengeruser.mapper.PassengerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private PassengerUserMapper passengerUserMapper;
    public ResponseResult loginOrRegister(String passengerPhone){
        System.out.println("user服务被调用了， 手机号是：" + passengerPhone);

        Map<String,Object> map = new HashMap<>();
        map.put("passenger_phone",passengerPhone);
        List<PassengerUser> passengerUsers = passengerUserMapper.selectByMap(map);
        System.out.println(passengerUsers.size() == 0 ? "无记录" : passengerUsers.get(0).getPassengerName());


        return ResponseResult.success();
    }
}
