package com.trinity.courierapp.Controller;

import com.trinity.courierapp.Repository.UserRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




}
