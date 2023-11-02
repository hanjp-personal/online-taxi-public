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
    private LocalDateTime departtime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ordertime;

    private String departure;

    private String deplongitude;

    private String deplatitude;

    private String destination;

    private String deslongitude;

    private String deslatitude;

    private Integer encrypt;

    private String faretype;

    private Integer fareVersion;

}
