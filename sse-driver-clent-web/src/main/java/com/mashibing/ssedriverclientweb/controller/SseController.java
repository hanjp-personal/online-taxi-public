package com.mashibing.ssedriverclientweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SseController {

    public static Map<String,SseEmitter> stringSseEmitterMap = new HashMap<>();

    /**
     * 建立连接
     * @param driverId
     * @return
     */
    @GetMapping("/connect/{driverId}")
    public SseEmitter connect(@PathVariable String driverId){
        System.out.println("司机ID: "+ driverId);
        SseEmitter sseEmitter = new SseEmitter(0l);

        stringSseEmitterMap.put(driverId,sseEmitter);
        return sseEmitter;
    }

    /**
     * 推送消息
     * @param driverId 消息推送者
     * @param content 消息内容
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam String driverId, @RequestParam String content){
        SseEmitter sseEmitter = stringSseEmitterMap.get(driverId);
        try {
            if (stringSseEmitterMap.containsKey(driverId)){
                sseEmitter.send(content);
            }else {
                return "推送失败";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "给用户"+driverId+", 发送了消息内容："+content;
    }

    @GetMapping("/close/{driverId}")
    public String close(@PathVariable String driverId){
        System.out.println("关闭连接："+driverId);
        if (stringSseEmitterMap.containsKey(driverId)){
            stringSseEmitterMap.remove(driverId);
        }
        return "close success";
    }
}
