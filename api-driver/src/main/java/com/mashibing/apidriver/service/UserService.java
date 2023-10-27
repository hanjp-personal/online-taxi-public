package com.mashibing.apidriver.service;

import com.mashibing.apidriver.remote.ServiceUserClient;
import com.mashibing.internalcommon.dto.DriverUser;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private ServiceUserClient serviceUserClient;

    public ResponseResult updateDriverUser(DriverUser driverUser){
        return serviceUserClient.updateDriverUser(driverUser);

    }
}
