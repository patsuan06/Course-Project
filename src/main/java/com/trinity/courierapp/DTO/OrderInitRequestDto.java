package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.OrderStatusEnum;
import com.trinity.courierapp.Entity.VehicleTypeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderInitRequestDto {

    private String recipientFullName;

    private String recipientPhoneNumber;

    private double srcLat;
    private double srcLng;
    private String srcAddress;
    private String srcPlaceId;

    private double destLat;
    private double destLng;
    private String destAddress;
    private String destPlaceId;

    private VehicleTypeEnum vehicleType;

}
