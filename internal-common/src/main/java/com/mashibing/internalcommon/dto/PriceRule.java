package com.mashibing.internalcommon.dto;

import lombok.Data;

@Data
public class PriceRule {
    
    private String cityCode;

    private String vehicleType;

    private double startFare;

    private int startMile;

    private double unitPricePerMile;

    private double unitPricePerMinute;
}
