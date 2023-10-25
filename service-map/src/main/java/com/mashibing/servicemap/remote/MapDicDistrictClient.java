package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.constant.AmapConfigComstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MapDicDistrictClient {

    @Value("${amap.key}")
    private String amapkey;

    @Autowired
    private RestTemplate restTemplate;

    public String dicDistrict(String keywords){
        //拼接url
        StringBuilder url = new StringBuilder();
        url.append(AmapConfigComstants.DICDISTRICT_URL);
        url.append("?");
        url.append("keywords="+keywords);
        url.append("&");
        url.append("subdistrict=3");
        url.append("&");
        url.append("key="+amapkey);

        //调用url
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url.toString(), String.class);
        String dicdistrict = forEntity.getBody();
        System.out.println(dicdistrict);
        //存入数据库

        return dicdistrict;
    }
}
