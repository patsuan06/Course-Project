package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.Courier;
import com.trinity.courierapp.Entity.Order;
import com.trinity.courierapp.Entity.UserTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
public class RegistrationRequestDto {

    private String fullName;

    private String phoneNumber;

    private String password;

    @Email(message = "Enter a valid email")
    private String email;

    private UserTypeEnum userType;
}
