package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.OrderStatusEnum;
import com.trinity.courierapp.Entity.VehicleTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreationRequestDto {

    private String recipientFullName;

    private String recipientPhoneNumber;

    private String addressString;

    private OrderStatusEnum orderStatus;

    private String email;

    private VehicleTypeEnum vehicleType;

}
