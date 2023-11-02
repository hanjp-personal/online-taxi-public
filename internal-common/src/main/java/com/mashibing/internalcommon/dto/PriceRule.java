package com.mashibing.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author hanjp
 * @since 2023-11-02
 */
@Data
public class PriceRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 城市代码
     */
    private String cityCode;

    /**
     * 车辆类型
     */
    private String vehicleType;

    private Double startFare;

    private Integer startMile;

    private Double unitPricePerMile;

    private Double unitPricePerMinute;

    /**
     * 运价类型编码
     */
    private String fareType;

    /**
     * 运价版本,默认1，修改后递增
     */
    private Integer fareVersion;


}
