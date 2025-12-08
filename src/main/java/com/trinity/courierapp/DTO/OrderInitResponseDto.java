package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.OrderTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderInitResponseDto {

    private String orderId;

    private double price;

    private String route;

    private double durationSecs;

    private double distanceMeters;

    private OrderTypeEnum orderType;

    public void setOrderType(OrderTypeEnum orderType) {
        this.orderType = orderType;
    }

}
