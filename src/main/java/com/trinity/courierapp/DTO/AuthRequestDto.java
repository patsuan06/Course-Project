package com.trinity.courierapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequestDto {
    private String email;
    private String password;
}
