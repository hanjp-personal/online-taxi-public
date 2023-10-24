package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.Response.DriectionResponse;
import com.mashibing.internalcommon.constant.AmapConfigComstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapDriectionClinet {

    @Value("${amap.key}")
    private String amapkey;

    @Autowired
    private RestTemplate restTemplate;

    public DriectionResponse driection(String depLongitude, String deplatitude, String destLongitude, String destlatitude){
        //组装请求的url
        /**
         * &key=33484ab1266b2677596b06b39e7e7d92
         */
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigComstants.DRIECTION_URL);
        urlBuilder.append("?");
        urlBuilder.append("origin="+depLongitude + "," + deplatitude);
        urlBuilder.append("&");
        urlBuilder.append("destination=" + destLongitude + "," + destlatitude);
        urlBuilder.append("&");
        urlBuilder.append("extensions=base");
        urlBuilder.append("&");
        urlBuilder.append("output=json");
        urlBuilder.append("&");
        urlBuilder.append("key=" + amapkey);
        System.out.println(urlBuilder.toString());
        //调用高德地图API接口
        ResponseEntity<String> driectionEntity = restTemplate.getForEntity(urlBuilder.toString(), String.class);
        log.info(driectionEntity.getBody());

        //解析接口
        return null;

    }
}
