package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.UserTypeEnum;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientRegistrationRequestDto {

    private String fullName;

    private String phoneNumber;

    private String password;

    @Email(message = "Enter a valid email")
    private String email;

    private UserTypeEnum userType = UserTypeEnum.CLIENT;
}
