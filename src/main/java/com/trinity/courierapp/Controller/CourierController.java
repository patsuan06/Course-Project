package com.trinity.courierapp.Controller;

import com.trinity.courierapp.Repository.CourierRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourierController {

    private final CourierRepository courierRepository;

    public CourierController(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }
}
