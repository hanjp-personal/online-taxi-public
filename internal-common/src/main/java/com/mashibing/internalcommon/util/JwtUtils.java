package com.mashibing.internalcommon.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mashibing.internalcommon.dto.TokenResult;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    private static final String SIGN = "ASWDer@#$&**!";

    private static final String JWT_KEY_PHONE = "Phone";

    private static final String JWT_KEY_IDENTITY = "Identity";

    private static final String JWT_TOKEN_TYPE = "TokenType";

    private static final String JWT_TOKEN_TIME = "TokenTime";

    //生成token
    public static String generatorToken(String passengerPhone,String identity,String tokenType){
        Map<String,String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE,passengerPhone);
        map.put(JWT_KEY_IDENTITY,identity);
        map.put(JWT_TOKEN_TYPE,tokenType);
        //避免生成一样的token
        map.put(JWT_TOKEN_TIME,Calendar.getInstance().getTime().toString());

        JWTCreator.Builder builder = JWT.create();
        //整合map,循环迭代
        map.forEach(
                (k,v)->{
                    builder.withClaim(k,v);
                }
        );
        //生成token
        String sign = builder.sign(Algorithm.HMAC256(SIGN));

        return sign;
    }
    /**
     * 解析token
     * @param token
     * @return
     */
    public static TokenResult parseToken(String token){
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();
        TokenResult tokenResult = new TokenResult();
        tokenResult.setPhone(phone);
        tokenResult.setIdentity(identity);
        return tokenResult;

    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static TokenResult checkToken(String token){
        TokenResult tokenResult = null;
        try{
            tokenResult = JwtUtils.parseToken(token);
        }catch (Exception e){

        }
        return tokenResult;
    }

    public static void main(String[] args) {
        String s = generatorToken("15293168155","1","accessToken");
        System.out.println("生成的token "+s);
        System.out.println("解析————————————————————");
        TokenResult tokenResult = parseToken(s);
        System.out.println("解析后的手机号： "+tokenResult.getPhone());
        System.out.println("解析后的身份：" + tokenResult.getIdentity());

    }
}
