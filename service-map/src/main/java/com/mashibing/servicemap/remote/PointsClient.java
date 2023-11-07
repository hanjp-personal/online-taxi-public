package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.constant.AmapConfigComstants;
import com.mashibing.internalcommon.dto.Points;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.request.PointsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class PointsClient {
    @Value("${amap.key}")
    private String amapkey;

    @Value("${amap.sid}")
    private String amapsid;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult upload(PointsRequest pointsRequest){

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigComstants.PIONTS_UPLOAD_URL);
        urlBuilder.append("?");
        urlBuilder.append("key="+amapkey);
        urlBuilder.append("&");
        urlBuilder.append("sid="+ amapsid);
        urlBuilder.append("&");
        urlBuilder.append("tid="+pointsRequest.getTid());
        urlBuilder.append("&");
        urlBuilder.append("trid="+pointsRequest.getTrid());
        urlBuilder.append("&");
        urlBuilder.append("points=");
        Points[] points = pointsRequest.getPoints();
        urlBuilder.append("%5B");
        for (Points point : points) {
            urlBuilder.append("%7B");
            String location = point.getLocation();
            String locatetime = point.getLocatetime();
            urlBuilder.append("%22location%22");
            urlBuilder.append("%3A");
            urlBuilder.append("%22"+location+"%22");
            urlBuilder.append("%2C");

            urlBuilder.append("%22locatetime%22");
            urlBuilder.append("%3A");
            urlBuilder.append(locatetime);
            urlBuilder.append("%7D");
        }
        urlBuilder.append("%5D");

        log.info("处理高德地图请求："+urlBuilder.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(URI.create(urlBuilder.toString()), null, String.class);
        log.info("处理高德地图响应："+stringResponseEntity.getBody());
        return ResponseResult.success("");
    }

}
