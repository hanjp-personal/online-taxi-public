package com.mashibing.internalcommon.util;

public class RedisPrefixUtils {
    /**
     * 验证码前缀
     */
    public static String verificationCodeprefix = "verification-code-";
    /**
     * token前缀
     */
    public static String tokenPrefix = "token-";
    /**
     * 黑名单前缀
     */
    public static String BlackDeviceCodePrefix = "black-device-";

    /**
     * 生成key
     * @param passengerPhone
     * @return
     */
    public static String generatorKeyByPhone(String passengerPhone ,String identity){
        return verificationCodeprefix + identity+ "-" +passengerPhone;
    }

    /**
     * 生成tokenkey
     * @param passenger
     * @param identity
     * @return
     */
    public static String generatorTokenKey(String passenger,String identity,String tokenType){
        return tokenPrefix + passenger + "-" + identity + "-" + tokenType;
    }
}
