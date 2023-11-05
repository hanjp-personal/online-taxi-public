package com.mashibing.internalcommon.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderRequest {

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

}
