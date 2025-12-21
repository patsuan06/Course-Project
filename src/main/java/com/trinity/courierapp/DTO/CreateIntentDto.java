package com.trinity.courierapp.DTO;

import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
public class CreateIntentDto {
    private Long amount;
    private String paymentMethodId;
    private UserDetails userDetails;
    private String orderToken;

}
