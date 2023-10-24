package com.mashibing.servicemap.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.ForeCastPriceDTO;
import com.mashibing.servicemap.service.DriectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direction")
public class DriectionController {

    @Autowired
    private DriectionService driectionService;

    @GetMapping("/driving")
    public ResponseResult driving(@RequestBody ForeCastPriceDTO foreCastPriceDTO){

        String depLongitude = foreCastPriceDTO.getDepLongitude();
        String deplatitude = foreCastPriceDTO.getDeplatitude();
        String destLongitude = foreCastPriceDTO.getDestLongitude();
        String destlatitude = foreCastPriceDTO.getDestlatitude();
        return driectionService.driving(depLongitude,deplatitude,destLongitude,destlatitude);

    }
}
