package com.trinity.courierapp.DTO;


import com.trinity.courierapp.Entity.CourierStatusEnum;
import com.trinity.courierapp.Entity.UserTypeEnum;
import com.trinity.courierapp.Entity.VehicleTypeEnum;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Setter
@Getter
public class CourierRegistrationRequestDto {

    private String fullName;

    private String phoneNumber;

    private String password;

    @Email(message = "Enter a valid email")
    private String email;

    private UserTypeEnum userType = UserTypeEnum.COURIER;

    private String vehicleNumber;

    private VehicleTypeEnum vehicleType;

    private Point courierGps; /////////////// need to add random gps on the streets, and when he gets an order he starts moving.
    /// For the interregional couriers, don't

    private CourierStatusEnum courierStatus = CourierStatusEnum.OFF_DUTY;

}
