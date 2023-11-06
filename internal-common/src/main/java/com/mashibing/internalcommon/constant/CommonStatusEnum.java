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

    PRICE_RULE_EXIST(1301,"计价规则已存在，不允许添加"),

    PRICE_RULE_NO_EDIT(1302,"计价规则未发生改变"),

    PRICE_RULE_CHANGE(1303,"计价规则有变化"),
    /**
     * 请求地图提示信息： 1400～1499
     */
    MAP_DICDISTRICT_ERROR(1400,"请求地图错误"),
    /**
     *司机和车辆状态：1500～1599
     */
    DRIVER_CAR_BIND_NOT_EXIST(1500,"司机和车辆绑定关系不存在"),

    DRIVER_NOT_EXIST(1501,"司机不存在"),

    DRIVER_CAR_BIND_EXIST(1502,"司机和车辆绑定关系已存在，请勿重复绑定"),

    DRIVER_BIND_EXIST(1503,"司机已经被绑定了，请勿重复绑定"),

    CAR_BIND_EXIST(1504,"车辆已经被绑定了，请勿重复绑定"),

    CITY_DRIVER_EMPTY(1505, "当前城市没有可用的司机"),

    AVAILABLE_DRIVER_EMPTY(1506,"可用的司机为空"),
    /**
     * 订单提示信息 1600～1699
     */
    OREDER_ISGONG_ON(1600, "有正在进行的订单"),
    /**
     * 黑名单设备提示信息
     */
    BLACKDEVICE(1601,"该设备超出下单次数"),

    CITY_SERVICE_NOT_SERVICE(1602,"当前城市不提供叫车服务"),
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
