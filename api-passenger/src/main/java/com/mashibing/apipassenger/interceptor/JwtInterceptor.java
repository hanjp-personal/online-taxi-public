package com.mashibing.apipassenger.interceptor;

import com.mashibing.internalcommon.constant.TokenConstants;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.TokenResult;
import com.mashibing.internalcommon.util.JwtUtils;
import com.mashibing.internalcommon.util.RedisPrefixUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean result = true;
        String resultString = "";
        String token = request.getHeader("Authorization");
        TokenResult tokenResult = JwtUtils.checkToken(token);
        if (tokenResult == null){
            resultString = "token is invalid";
            result = false;
        }else{
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();
            String tokenKey = RedisPrefixUtils.generatorTokenKey(phone, identity, TokenConstants.ACCESS_TOKEN_TYPE);
            //从redis中获取token
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);
            if((StringUtils.isBlank(tokenRedis)) || (!token.trim().equals(tokenRedis.trim()))){
                resultString = "token is invalid";
                result = false;
            }
        }
        if (!result){
            PrintWriter printWriter = response.getWriter();
            printWriter.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
        }
        return result;
    }
}
