package com.mashibing.servicessepush.controller;

import com.mashibing.internalcommon.util.SsePrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class SseController {

    public static Map<String,SseEmitter> stringSseEmitterMap = new HashMap<>();

    /**
     * 建立连接
     * @param userId 用户Id
     * @param identity 身份类型
     * @return
     */
    @GetMapping("/connect")
    public SseEmitter connect(@RequestParam Long userId,@RequestParam String identity){
        log.info("用户："+userId+"身份类型："+identity);
        SseEmitter sseEmitter = new SseEmitter(0l);
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);
        stringSseEmitterMap.put(sseMapKey,sseEmitter);
        return sseEmitter;
    }

    /**
     * 推送消息
     * @param userId 用户Id
     * @param identity 身份类型
     * @param content 内容
     * @return
     */
    @GetMapping("/push")
    public String push(@RequestParam Long userId, @RequestParam String identity, @RequestParam String content){
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);
        SseEmitter sseEmitter = stringSseEmitterMap.get(sseMapKey);
        try {
            if (stringSseEmitterMap.containsKey(sseMapKey)){
                sseEmitter.send(content);
            }else {
                return "推送失败";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "给用户"+sseMapKey+", 发送了消息内容："+content;
    }

    @GetMapping("/close")
    public String close(@RequestParam Long userId,@RequestParam String identity){
        String sseMapKey = SsePrefixUtils.generatorSseKey(userId,identity);
        log.info("关闭连接："+sseMapKey);
        if (stringSseEmitterMap.containsKey(sseMapKey)){
            stringSseEmitterMap.remove(sseMapKey);
        }
        return "close success";
    }
}
