package com.trinity.courierapp.Service;

import com.trinity.courierapp.DTO.OrderCreationRequestDto;
import com.trinity.courierapp.DTO.OrderCreationResponseDto;
import com.trinity.courierapp.Entity.Order;
import com.trinity.courierapp.Repository.CourierRepository;
import com.trinity.courierapp.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourierRepository courierRepository;

}
