package com.trinity.courierapp.Controller;

import com.trinity.courierapp.DTO.OrderInitRequestDto;
import com.trinity.courierapp.Entity.Order;
import com.trinity.courierapp.Entity.User;
import com.trinity.courierapp.Repository.OrderRepository;
import com.trinity.courierapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<Order> cancelOrder(Order order) {
        return ResponseEntity.ok(orderRepository.save(order));
    }

    @PostMapping("/initialize")
    public ResponseEntity<?> initOrder(OrderInitRequestDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email);




    }
}
