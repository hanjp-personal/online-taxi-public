package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.Response.ServiceResponse;
import com.mashibing.internalcommon.constant.AmapConfigComstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceClient {

    @Value("${amap.key}")
    private String amapkey;

    @Autowired
    private RestTemplate restTemplate;//调用第三方接口

    public ResponseResult createService(String name){
        //组装
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigComstants.SERVICE_ADD_URL);
        urlBuilder.append("?");
        urlBuilder.append("key="+amapkey);
        urlBuilder.append("&");
        urlBuilder.append("name=" + name);
        //调用高德地图API接口
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(urlBuilder.toString(), null, String.class);
        String result = stringResponseEntity.getBody();
        JSONObject data = JSONObject.fromObject(result);
        JSONObject jsonObject = data.getJSONObject("data");
        String sid = jsonObject.getString("sid");
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setSid(sid);
        return ResponseResult.success(serviceResponse);
    }

}
