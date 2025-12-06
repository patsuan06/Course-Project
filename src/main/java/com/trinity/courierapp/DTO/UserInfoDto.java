package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Entity.UserTypeEnum;
import lombok.Getter;

@Getter
public class UserInfoDto {

    private final String fullName;

    private final String phoneNumber;

    private final String email;

    private final UserTypeEnum userType;

    public UserInfoDto(User user) {
        this.fullName = user.getFullName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.userType = user.getUserType();
    }
}
