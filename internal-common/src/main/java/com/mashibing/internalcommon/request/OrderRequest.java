package com.mashibing.internalcommon.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderRequest {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 乘客ID
     */
    private Long passengerId;

    /**
     * 乘客手机号
     */
    private String passengerPhone;


    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    private String departure;

    private String depLongitude;

    private String depLatitude;

    private String destination;

    private String desLongitude;

    private String desLatitude;

    private Integer encrypt;

    private String fareType;

    private Integer fareVersion;
    /**
     * 设备号
     */
    private String deviceCode;

    /**
     * 去接乘客时司机的经度
     */
    private String toPickUpPassengerLongitude;

    /**
     * 去接乘客时司机的纬度
     */
    private String toPickUpPassengerLatitude;

    /**
     * 去接乘客时司机的地点
     */
    private String toPickUpPassengerAddress;

    /**
     * 乘客上车的经度
     */
    private String pickUpPassengerLongitude;

    /**
     * 乘客上车的纬度
     */
    private String pickUpPassengerLatitude;

}
