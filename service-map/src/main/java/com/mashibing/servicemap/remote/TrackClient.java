package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.Response.TrackResponse;
import com.mashibing.internalcommon.constant.AmapConfigComstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TrackClient {
    @Value("${amap.key}")
    private String amapkey;

    @Value("${amap.sid}")
    private String amapsid;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseResult<TrackResponse> addTrack(String tid){

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigComstants.TRACK_ADD_URL);
        urlBuilder.append("?");
        urlBuilder.append("key="+amapkey);
        urlBuilder.append("&");
        urlBuilder.append("sid="+ amapsid);
        urlBuilder.append("&");
        urlBuilder.append("tid="+tid);
        log.info("处理高德地图请求："+ urlBuilder.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(urlBuilder.toString(), null, String.class);
        log.info("处理高德地图响应："+stringResponseEntity.getBody());
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSONObject.fromObject(body);
        JSONObject data = jsonObject.getJSONObject("data");
        String trid = data.getString("trid");
        String trname = "";
        if (data.has("trname")){
            trname = data.getString("trname");
        }
        TrackResponse trackResponse = new TrackResponse();
        trackResponse.setTrid(trid);
        trackResponse.setTrname(trname);
        return ResponseResult.success(trackResponse);



    }

}
