package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.servicemap.remote.MapDicDistrictClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DicDistrictService {

    @Autowired
    private MapDicDistrictClient mapDicDistrictClient;

    public ResponseResult initDicDistrict(String keywords){

        String dicDistrict = mapDicDistrictClient.dicDistrict(keywords);
        return ResponseResult.success(dicDistrict);
    }
}
