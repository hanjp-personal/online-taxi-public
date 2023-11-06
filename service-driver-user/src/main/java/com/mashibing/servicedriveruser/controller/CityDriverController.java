package com.mashibing.servicedriveruser.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.service.CityDriverUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/city-driver")
public class CityDriverController {

    @Autowired
    private CityDriverUserService cityDriverUserService;
    @GetMapping("/is-available-user")
    public ResponseResult<Boolean> isAvailable(@RequestParam String cityCode){
        return cityDriverUserService.isAvailableDriver(cityCode);
    }

}
