package com.trinity.courierapp.DTO;

import com.trinity.courierapp.Entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class GetOrderDto {
    private Order orders;
    private String paymentMethodId;

}
