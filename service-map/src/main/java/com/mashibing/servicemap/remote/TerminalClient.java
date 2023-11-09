package com.mashibing.servicemap.remote;

import com.mashibing.internalcommon.Response.TerminalResponse;
import com.mashibing.internalcommon.Response.TrsearchResponse;
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

    /**
     * 创建终端
     * @param name
     * @param desc
     * @return
     */
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
        System.out.println("调用前的高德创建终端请求："+ urlBuilder.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(urlBuilder.toString(), null, String.class);
        System.out.println("调用后的高德创建终端请求:"+ stringResponseEntity.getBody());
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

    /**
     * 周边搜索
     * @param center
     * @param radius
     * @return
     */
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
        log.info("调用前的高德周边搜索请求："+ urlBuilder.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(urlBuilder.toString(), null, String.class);
        log.info("调用后的高德周边搜索响应:"+ stringResponseEntity.getBody());

        List<TerminalResponse> terminalResponseList = new ArrayList<>();

        //解析获取的数据
        String body = stringResponseEntity.getBody();
        JSONObject result = JSONObject.fromObject(body);
        JSONObject data = result.getJSONObject("data");
        JSONArray jsonArrayResult = data.getJSONArray("results");
        for (int i = 0; i < jsonArrayResult.size(); i++) {
            TerminalResponse terminalResponse =new TerminalResponse();

            JSONObject jsonObject = jsonArrayResult.getJSONObject(i);
            //desc就是车辆ID
            String desc = jsonObject.getString("desc");
            long carId = Long.parseLong(desc);
            String tid = jsonObject.getString("tid");

            JSONObject location = jsonObject.getJSONObject("location");
            String longitude = location.getString("longitude") ;
            String latitude = location.getString("latitude");

            terminalResponse.setCarId(carId);
            terminalResponse.setTid(tid);
            terminalResponse.setLongitude(longitude);
            terminalResponse.setLatitude(latitude);

            terminalResponseList.add(terminalResponse);
        }
        return ResponseResult.success(terminalResponseList);
    }

    /**
     * 轨迹查询
     * @param tid
     * @param starttime
     * @param endtime
     * @return
     */
    public ResponseResult<TrsearchResponse> trsearch(String tid, Long starttime,Long endtime){

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(AmapConfigComstants.TRSEARCH_URL);
        urlBuilder.append("?");
        urlBuilder.append("key="+amapkey);
        urlBuilder.append("&");
        urlBuilder.append("sid="+amapsid);
        urlBuilder.append("&");
        urlBuilder.append("tid=" + tid);
        urlBuilder.append("&");
        urlBuilder.append("starttime=" + starttime);
        urlBuilder.append("&");
        urlBuilder.append("endtime=" + endtime);
        log.info("调用前的高德轨迹查询请求："+ urlBuilder.toString());
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(urlBuilder.toString(), null, String.class);
        log.info("调用后的高德轨迹查询响应:"+ stringResponseEntity.getBody());
        JSONObject data = JSONObject.fromObject(stringResponseEntity.getBody());
        JSONObject result = data.getJSONObject("data");
        int counts = result.getInt("counts");
        if (counts == 0){
            return null;
        }
        JSONArray tracks = result.getJSONArray("tracks");
        long driverMile = 0l;
        long driverTime = 0l;
        for (int i = 0; i < tracks.size(); i++) {
            JSONObject tracksJSONObject = tracks.getJSONObject(i);
            long distance = tracksJSONObject.getLong("distance");
            long time = tracksJSONObject.getLong("time");
            time = time / (1000 * 60);
            driverMile = driverMile + distance;
            driverTime = driverTime + time;
        }
        TrsearchResponse trsearchResponse = new TrsearchResponse();
        trsearchResponse.setDriverMile(driverMile);
        trsearchResponse.setDriverTime(driverTime);
        return ResponseResult.success(trsearchResponse);
    }
}
