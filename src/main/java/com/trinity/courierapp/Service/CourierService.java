package com.trinity.courierapp.Service;

import com.trinity.courierapp.Entity.Courier;
import com.trinity.courierapp.Repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    public Courier findNeareastCourier(Courier courier) {

    }


}
