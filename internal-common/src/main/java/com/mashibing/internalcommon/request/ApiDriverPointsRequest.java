package com.mashibing.internalcommon.request;

import com.mashibing.internalcommon.dto.Points;
import lombok.Data;

@Data
public class ApiDriverPointsRequest {

    private Long carId;

    private Points[] points;
}
