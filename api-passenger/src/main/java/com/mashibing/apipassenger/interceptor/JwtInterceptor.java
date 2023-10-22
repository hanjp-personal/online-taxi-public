package com.mashibing.apipassenger.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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
        TokenResult tokenResult = null;
        try{
            tokenResult = JwtUtils.parseToken(token);
        }catch(SignatureVerificationException e){
            resultString = "token sign error";
            result = false;
        }catch(TokenExpiredException e){
            resultString = "token time out";
            result = false;
        }catch(AlgorithmMismatchException e){
            resultString = "token AlgorithmMismatchException";
            result = false;
        }catch (Exception e){
            resultString = "token is invalid";
            result = false;
        }
        if (tokenResult == null){
            resultString = "token is invalid";
            result = false;
        }else{
            String phone = tokenResult.getPhone();
            String identity = tokenResult.getIdentity();
            String tokenKey = RedisPrefixUtils.generatorTokenKey(phone, identity);
            //从redis中获取token
            String tokenRedis = stringRedisTemplate.opsForValue().get(tokenKey);
            if(StringUtils.isBlank(tokenRedis)){
                resultString = "token is invalid";
                result = false;
            }else {
                if(!token.trim().equals(tokenRedis.trim())){
                    resultString = "token is invalid";
                    result = false;
                }
            }
        }

        if (!result){
            PrintWriter printWriter = response.getWriter();
            printWriter.print(JSONObject.fromObject(ResponseResult.fail(resultString)).toString());
        }
        return result;
    }
}
