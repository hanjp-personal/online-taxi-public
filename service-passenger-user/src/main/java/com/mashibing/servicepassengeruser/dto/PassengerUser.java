package com.mashibing.servicepassengeruser.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class PassengerUser {

    private Long id;

    private String passengerPhone;

    private String passengerName;

    private byte passengerGender;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    private byte state;
}
