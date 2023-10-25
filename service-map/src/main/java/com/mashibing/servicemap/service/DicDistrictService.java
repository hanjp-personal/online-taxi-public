package com.mashibing.servicemap.service;

import com.mashibing.internalcommon.constant.AmapConfigComstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DicDistrictService {

    @Value("${amap.key}")
    private String amapkey;

    public ResponseResult initDicDistrict(String keywords){
        //拼接url
        /**
         * &key=<用户的key>
         */
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigComstants.DICDISTRICT_URL);
        url.append("?");
        url.append("keywords="+keywords);
        url.append("&");
        url.append("subdistrict=3");
        url.append("&");
        url.append("key="+amapkey);

        //调用url

        //存入数据库
        return ResponseResult.success();
    }
}
