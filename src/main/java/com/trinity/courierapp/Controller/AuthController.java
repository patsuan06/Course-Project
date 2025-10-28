package com.trinity.courierapp.Controller;


import com.trinity.courierapp.Repository.ClientRepository;
import com.trinity.courierapp.Repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CourierRepository courierRepository;



}
