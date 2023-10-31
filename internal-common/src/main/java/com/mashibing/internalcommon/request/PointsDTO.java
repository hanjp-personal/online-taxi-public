package com.mashibing.internalcommon.request;

import com.mashibing.internalcommon.dto.Points;
import lombok.Data;

@Data
public class PointsDTO {
    
    private String tid;
    
    private String trid;
    
    private Points[] points;
}


