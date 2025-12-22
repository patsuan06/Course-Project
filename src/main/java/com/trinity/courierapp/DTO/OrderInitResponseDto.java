package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderInitResponseDto {

    // this orderId is temporary, used for caching only, the permanent id will be set after final confirmation
    private String orderToken;

    private long price;

    private String route;

    // this is also temporary eta, after the courier is chosen, more real eta can be calculated 
    private double durationMinutes;

    private double distanceMeters;

    private OrderTypeEnum orderType;

    private OrderStatusEnum orderStatus;

    //after finding courier
    private double finalDurationMins;

    private double courierToMins;

    private double courierToARouteMeter;

    private String courierName;

    private String courierPhoneNumber;

    private CourierStatusEnum courierStatus;

    private String vehicleNumber;

    private double courierLat;
    private double courierLng;

    private String courierToARoute;

    //intent creation response status
    private String intentStatus;

    // the following you don't have to take in frontend, it is just for me to store in cache:
    private PaymentMethodEnum paymentMethod;

    private LocalDate orderDate;

    private int paymentDetailId;

    private String paymentMethodId;

    private int courierId;

    private double priceKmRate;

    private String recipientFullName;

    private String recipientPhoneNumber;

    private VehicleTypeEnum vehicleType;

    private double srcLat;
    private double srcLng;
    private String srcAddress;
    private String srcPlaceId;

    private double destLat;
    private double destLng;
    private String destAddress;
    private String destPlaceId;

}
