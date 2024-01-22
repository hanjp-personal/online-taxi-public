package com.mashibing.internalcommon.constant;

public class OrderConstants {

    public static final int ORDER_INVALID  = 0;

    public static final int ORDER_START = 1;

    public static final int DRIVER_RECEIVE_ORDER = 2;

    public static final int DRIVER_TO_PICK_UP_PASSENGER = 3;

    public static final int DRIVER_ARRIVED_DEPARTURE = 4;

    public static final int PICK_UP_PASSENGER = 5;

    public static final int PASSENGER_GETOFF = 6;

    public static final int TO_START_PAY = 7;

    public static final int SUCCESS_PAY = 8;

    public static final int ORDER_CANCEL = 9;

    /**
     * 乘客提起取消
     */
    public static final int CANCEL_PASSENGER_BEFORE = 1;
    /**
     * 司机提前取消
     */
    public static final int CANCEL_DRIVER_BEFORE = 2;
    /**
     * 平台提前取消
     */
    public static final int CANCEL_PLATFORM_BEFORE = 3;

    /**
     * 乘客违约取消
     */
    public static final int CANCEL_PASSENGER_ILLEGAL = 4;
    /**
     * 司机违约取消
     */
    public static final int CANCEL_DRIVER_ILLEGAL = 5;



}
