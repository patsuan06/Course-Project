package com.trinity.courierapp.Controller;

import com.trinity.courierapp.Repository.ClientRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    private final ClientRepository clientRepository;

    public ClientController(ClientRepository clientRepository) {
        this.clientRepository  = clientRepository;
    }




}
