package com.mashibing.internalcommon.Response;

import lombok.Data;

@Data
public class OrderResponse {

    private Long driverId;

    private Long carId;

    private String driverPhone;

    /**
     * 机动车驾驶证号
     */
    private String licenseId;

    /**
     * 车牌号
     */
    private String vehicleNo;
    /**
     * 车辆类型
     */
    private String vehicleType;

}
