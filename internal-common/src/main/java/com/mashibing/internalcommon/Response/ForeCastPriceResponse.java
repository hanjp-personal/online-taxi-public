package com.mashibing.internalcommon.Response;

import lombok.Data;

@Data
public class ForeCastPriceResponse {
    private double price;

    private String cityCode;

    private String vehicleType;

    private String fareType;

    private Integer fareVersion;
}
