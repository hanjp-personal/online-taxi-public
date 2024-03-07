package com.mashibing.apidriver.remote;

import com.mashibing.internalcommon.Response.DriverUserResponse;
import com.mashibing.internalcommon.dto.Car;
import com.mashibing.internalcommon.dto.DriverUser;
import com.mashibing.internalcommon.dto.DriverUserWorkStatus;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("service-driver-user")
public interface ServiceDriverUserClient {

    @RequestMapping(method = RequestMethod.PUT,value = "/user")
    public ResponseResult updateDriverUser(@RequestBody DriverUser driverUser);
    @RequestMapping(method = RequestMethod.GET,value = "/check-driver/{driverPhone}")
    public ResponseResult<DriverUserResponse> checkDriverUser(@PathVariable("driverPhone") String driverPhone);

    @RequestMapping(method = RequestMethod.GET,value = "/car")
    public ResponseResult<Car> getCarById(@RequestParam Long carId);

    @RequestMapping(method = RequestMethod.POST,value = "/driver-user-work-status")
    public ResponseResult changeDriverStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus);

    @RequestMapping(method = RequestMethod.GET,value = "/driver-car-binding-relationship/driver-car-relationship")
    public ResponseResult getDriverCarBindingRelationship(@RequestParam String driverPhone);
}
