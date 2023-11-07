package com.mashibing.servicedriveruser.controller;

import com.mashibing.internalcommon.Response.DriverUserResponse;
import com.mashibing.internalcommon.Response.OrderResponse;
import com.mashibing.internalcommon.dto.DriverUser;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.service.DriverUserService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DriverUserController {

    @Autowired
    private DriverUserService driverUserService;

    /**
     * 添加司机信息
     * @param driverUser
     * @return
     */
    @PostMapping("/user")
    public ResponseResult addDriverUser(@RequestBody DriverUser driverUser){
        log.info(JSONObject.fromObject(driverUser).toString());
        return driverUserService.addDriverUser(driverUser);

    }

    /**
     * 修改司机信息
     * @param driverUser
     * @return
     */
    @PutMapping("/user")
    public ResponseResult updateDriver(@RequestBody DriverUser driverUser){

        log.info(JSONObject.fromObject(driverUser).toString());
        return driverUserService.updateDriverUser(driverUser);

    }
    @GetMapping("/check-driver/{driverPhone}")
    public ResponseResult getDriverUserByPhone(@PathVariable("driverPhone") String driverPhone){
        ResponseResult<DriverUser> driverUserByPhone = driverUserService.getDriverUserByPhone(driverPhone);
        DriverUser driverUserDb = driverUserByPhone.getData();
        DriverUserResponse driverUserResponse = new DriverUserResponse();
        int ifExist = 1;
        if (driverUserDb == null){
            ifExist = 0;
            driverUserResponse.setDriverUserPhone(driverPhone);
            driverUserResponse.setState(ifExist);
        }else {
            driverUserResponse.setDriverUserPhone(driverUserDb.getDriverPhone());
            driverUserResponse.setState(ifExist);
        }
        return ResponseResult.success(driverUserResponse);
    }
    @GetMapping("/get-available-driver/{carId}")
    public ResponseResult<OrderResponse> getAvailableDriver(@PathVariable("carId") Long carId){
        return driverUserService.getAvailableDriver(carId);

    }
}
