package com.mashibing.internalcommon.constant;

import lombok.Getter;


public enum CommonStatusEnum {
    /**
     * 验证码错误提示 1000～1099
     */
    VERIFICATION_CODE_ERROR(1000,"验证码不正确"),
    /**
     * token错误提示 1100～1199
     */
    TOKEN_ERROR(1100,"token错误"),
    /**
     * 用户提示信息 1200～1299
     */
    USER_NO_EXITS(1200,"用户不存在"),
    /**
     * 计价规则：1300～1399
     */
    PRICE_RULE_EMPTY(1300,"计价规则不存在"),
    /**
     * 请求地图提示信息： 1400～1499
     */
    MAP_DICDISTRICT_ERROR(1400,"请求地图错误"),
    /**
     * 成功
     */
    SUCCESS(1,"success"),
    /**
     * 失败
     */
    FAIL(0,"fail")
    ;
    @Getter
    private int code;
    @Getter
    private String value;

    CommonStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
