package com.trinity.courierapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderInitResponseDto {

    private String orderId;

    private double price;

    private String srcAddress;

    private String destAddress;

    private String polyline;

    private double durationSecs;

    private double distanceMeters;

}
