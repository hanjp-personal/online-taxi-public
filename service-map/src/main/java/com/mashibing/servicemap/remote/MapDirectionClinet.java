package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.Response.DirectionResponse;
import com.mashibing.internalcommon.constant.AmapConfigComstants;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MapDirectionClinet {

    @Value("${amap.key}")
    private String amapkey;

    @Autowired
    private RestTemplate restTemplate;//调用第三方接口

    public DirectionResponse direction(String depLongitude, String deplatitude, String destLongitude, String destlatitude){
        //组装请求的url
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
        //调用高德地图API接口
        log.info("处理高德地图请求："+ urlBuilder.toString());
        ResponseEntity<String> directionEntity = restTemplate.getForEntity(urlBuilder.toString(), String.class);
        String directionEntityBody = directionEntity.getBody();
        log.info("处理高德地图响应："+directionEntity.getBody());

        //解析接口
        DirectionResponse directionResponse = parseDirectionEntity(directionEntityBody);
        return directionResponse;

    }

    /**
     * 解析接口实体
     * @param directionEntityBody
     * @return
     */
    private DirectionResponse parseDirectionEntity(String directionEntityBody){
        DirectionResponse directionResponse = null;
        try {

            JSONObject result = JSONObject.fromObject(directionEntityBody);
            if(result.has(AmapConfigComstants.STATUS)){
                int status = result.getInt(AmapConfigComstants.STATUS);
                if(status == 1){
                    if(result.has(AmapConfigComstants.ROUTE)){
                        JSONObject routeObject = result.getJSONObject(AmapConfigComstants.ROUTE);
                        JSONArray pathsArray = routeObject.getJSONArray(AmapConfigComstants.PATHS);
                        JSONObject pathsObject = pathsArray.getJSONObject(0);
                        directionResponse = new DirectionResponse();
                        if(pathsObject.has(AmapConfigComstants.DISTANCE)){
                            int distance = pathsObject.getInt(AmapConfigComstants.DISTANCE);
                            directionResponse.setDistance(distance);
                        }
                        if (pathsObject.has(AmapConfigComstants.DURATION)){
                            int duration = pathsObject.getInt(AmapConfigComstants.DURATION);
                            directionResponse.setDuration(duration);
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return directionResponse;
    }
}
