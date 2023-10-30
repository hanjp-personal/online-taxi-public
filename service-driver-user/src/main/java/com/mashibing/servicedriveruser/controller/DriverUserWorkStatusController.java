package com.mashibing.servicedriveruser.controller;


import com.mashibing.internalcommon.dto.DriverUserWorkStatus;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicedriveruser.service.DriverUserWorkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hanjp
 * @since 2023-10-30
 */
@RestController
public class DriverUserWorkStatusController {

    @Autowired
    private DriverUserWorkStatusService driverUserWorkStatusService;

    @PostMapping("/driver-user-work-status")
    public ResponseResult changeDriverStatus(@RequestBody DriverUserWorkStatus driverUserWorkStatus){

        return driverUserWorkStatusService.changeWorkStatus(driverUserWorkStatus.getDriverId(),driverUserWorkStatus.getWorkStatus());
    }

}
