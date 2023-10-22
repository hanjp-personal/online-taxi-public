package com.mashibing.internalcommon.util;

public class RedisPrefixUtils {
    /**
     * 验证码前缀
     */
    public static String verificationCodeprefix = "passenger-verification-code-";
    /**
     * token前缀
     */
    public static String tokenPrefix = "token-";

    /**
     * 生成key
     * @param passengerPhone
     * @return
     */
    public static String generatorKeyByPhone(String passengerPhone){
        return verificationCodeprefix + passengerPhone;
    }

    /**
     * 生成tokenkey
     * @param passenger
     * @param identity
     * @return
     */
    public static String generatorTokenKey(String passenger,String identity){
        return tokenPrefix + passenger + "-" + identity;
    }
}
