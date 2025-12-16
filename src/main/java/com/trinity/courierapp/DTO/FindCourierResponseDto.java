package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.OrderTypeEnum;
import com.trinity.courierapp.Entity.VehicleTypeEnum;
import org.locationtech.jts.geom.Point;

public class FindCourierResponseDto {

    private String orderToken;

    private double newPrice;

    private String courierToARoute;

    private double newDurationMinutes;

    private double newDistanceMeters;

    private Point courierGps;

    private String courierName;

    private String courierPhoneNumber;

    // not necessary for frontend if they save to localstorage, but can be used if needed

    private OrderTypeEnum orderType;

    private String recipientFullName;

    private String recipientPhoneNumber;

    private String srcAddress;

    private String destAddress;

    private VehicleTypeEnum vehicleType;
}
