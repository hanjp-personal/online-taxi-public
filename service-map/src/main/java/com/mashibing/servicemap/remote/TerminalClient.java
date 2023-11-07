package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.Response.TerminalResponse;
import com.mashibing.internalcommon.constant.AmapConfigComstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TerminalClient {

    @Value("${amap.key}")
    private String amapkey;
    @Value("${amap.sid}")
    private String amapsid;

    @Autowired
    private RestTemplate restTemplate;//调用第三方接口

    public ResponseResult<TerminalResponse> createTerminal(String name, Long desc){

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigComstants.Terminal_ADD_URL);
        urlBuilder.append("?");
        urlBuilder.append("key="+amapkey);
        urlBuilder.append("&");
        urlBuilder.append("sid="+amapsid);
        urlBuilder.append("&");
        urlBuilder.append("name=" + name);
        urlBuilder.append("&");
        urlBuilder.append("desc=" + desc);
        System.out.println("调用前的高德请求："+ urlBuilder.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(urlBuilder.toString(), null, String.class);
        System.out.println("调用后的高得请求:"+ stringResponseEntity.getBody());
        String body = stringResponseEntity.getBody();
        JSONObject jsonObject = JSONObject.fromObject(body);
        String data = jsonObject.getString("data");
        JSONObject jsonObject1 = JSONObject.fromObject(data);
        String tid = jsonObject1.getString("tid");
        TerminalResponse terminalResponse = new TerminalResponse();
        terminalResponse.setTid(tid);
        terminalResponse.setCarId(desc);
        return ResponseResult.success(terminalResponse);
    }
    public ResponseResult<List<TerminalResponse>>  aroundsearch(String center,Integer radius){
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigComstants.TERMINAL_AROUNFSEARCH_URL);
        urlBuilder.append("?");
        urlBuilder.append("key="+amapkey);
        urlBuilder.append("&");
        urlBuilder.append("sid="+amapsid);
        urlBuilder.append("&");
        urlBuilder.append("center=" + center);
        urlBuilder.append("&");
        urlBuilder.append("radius=" + radius);
        log.info("调用前的高德请求："+ urlBuilder.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(urlBuilder.toString(), null, String.class);
        log.info("调用后的高德响应:"+ stringResponseEntity.getBody());

        List<TerminalResponse> terminalResponseList = new ArrayList<>();

        //解析获取的数据
        String body = stringResponseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        JSONArray jsonArrayResult = data.getJSONArray("results");
        for (int i = 0; i < jsonArrayResult.size(); i++) {
            TerminalResponse terminalResponse =new TerminalResponse();

            JSONObject jsonObject = jsonArrayResult.getJSONObject(i);
            String desc = jsonObject.getString("desc");
            long carId = Long.parseLong(desc);
            String tid = jsonObject.getString("tid");

            terminalResponse.setCarId(carId);
            terminalResponse.setTid(tid);

            terminalResponseList.add(terminalResponse);
        }
        return ResponseResult.success(terminalResponseList);
    }
}
