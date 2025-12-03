package com.trinity.courierapp.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderCreationResponseDto {
    private Integer id;

    private String address;

    private com.trinity.courierapp.DTO.Route Route;



}
