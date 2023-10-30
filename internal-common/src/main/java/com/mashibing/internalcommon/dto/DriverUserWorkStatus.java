package com.mashibing.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author hanjp
 * @since 2023-10-30
 */
@Data
public class DriverUserWorkStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long driverId;

    private Integer workStatus;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

}
