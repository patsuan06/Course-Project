package com.trinity.courierapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;

}
