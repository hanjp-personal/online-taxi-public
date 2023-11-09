package com.mashibing.internalcommon.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author hanjp
 * @since 2023-11-02
 */
@Data
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 乘客ID
     */
    private Long passengerId;

    /**
     * 乘客手机号
     */
    private String passengerPhone;

    /**
     * 司机ID
     */
    private Long driverId;

    /**
     * 司机手机号
     */
    private String driverPhone;

    /**
     * 车辆ID
     */
    private Long carId;

    /**
     * 发起地行政区划代码
     */
    private String address;

    /**
     * 订单发起时间
     */
    private LocalDateTime orderTime;

    /**
     * 预计用车时间
     */
    private LocalDateTime departTime;

    /**
     * 预计出发点的详细地址
     */
    private String departure;

    /**
     * 出发点的经度
     */
    private String depLongitude;

    /**
     * 出发点的纬度
     */
    private String depLatitude;

    /**
     * 目的地
     */
    private String destination;

    /**
     * 目的地的经度
     */
    private String destLongitude;

    /**
     * 目的地的纬度
     */
    private String destLatitude;

    /**
     * 坐标加密标识
     */
    private Integer encrypt;
    /**
     * 运价编码类型
     */
    private String fareType;
    /**
     * 运价版本
     */
    private Integer fareVersion;
    /**
     * 接单时车辆的经度
     */
    private String receiveOrderCarLongitude;

    /**
     * 接单时车辆的维度
     */
    private String receiveOrderCarLatitude;

    /**
     * 接单时间、派单时间
     */
    private LocalDateTime receiveOrderTime;

    /**
     * 机动车驾驶证号
     */
    private String licenseId;

    /**
     * 车牌号
     */
    private String vehicleNo;

    /**
     * 司机去接乘客的出发时间
     */
    private LocalDateTime toPickUpPassengerTime;

    /**
     * 去接乘客时司机的经度
     */
    private String toPickUpPassengerLongitude;

    /**
     * 去接乘客时司机的纬度
     */
    private String toPickUpPassengerLatitude;

    /**
     * 去接乘客时司机的地点
     */
    private String toPickUpPassengerAddress;

    /**
     * 司机到达上车地点
     */
    private LocalDateTime driverArrivedDepartureTime;

    /**
     * 乘客上车的时间
     */
    private LocalDateTime pickUpPassengerTime;

    /**
     * 乘客上车的经度
     */
    private String pickUpPassengerLongitude;

    /**
     * 乘客上车的纬度
     */
    private String pickUpPassengerLatitude;

    /**
     * 乘客下车的时间
     */
    private LocalDateTime passengerGetoffTime;

    /**
     * 乘客下车的经度
     */
    private String passengerGetoffLongitude;

    /**
     * 乘客下车的纬度
     */
    private String passengerGetoffLatitude;

    /**
     * 订单取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 订单取消源，1：乘客；2：司机；3:平台公司
     */
    private Integer cancelOperator;

    /**
     * 撤销类型代码，1:乘客提前取消；2:司机提前取消；3:平台公司撤销；4:乘客违约撤销；5:司机违约撤销
     */
    private Integer cancelTypeCode;

    /**
     * 载客里程（米）
     */
    private Long driverMile;

    /**
     * 载客时间（分钟）
     */
    private Long driverTime;

    /**
     * 订单状态，1:订单开始；2:司机接单；3:去接乘客；4:司机到达上车点；5:乘客上车，司机开始行程；6:到达目的地，行程结束，未支付；7:发起收款；8:支付完成；9:订单取消
     */
    private Integer orderStatus;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

}
