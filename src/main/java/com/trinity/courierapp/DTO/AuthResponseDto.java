package com.trinity.courierapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Setter
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;

}
